package com.ki960213.kidsandseoul.presentation.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.kidsandseoul.BuildConfig
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.DialogLoginBinding
import com.ki960213.kidsandseoul.presentation.common.base.KasBottomSheetDialogFragment
import com.ki960213.kidsandseoul.presentation.common.extension.showToast

class LoginDialog : KasBottomSheetDialogFragment<DialogLoginBinding>(R.layout.dialog_login) {

    private val viewModel: MainViewModel by activityViewModels()

    private val googleClient: GoogleSignInClient by lazy { getGoogleSignInClient() }
    private val googleAuthLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            .getResult(ApiException::class.java)
        val googleToken = account.idToken.toString()
        loginAndDismiss(Authentication.Google(googleToken))
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onKakaoLoginButtonClick = ::handleKakaoButtonClick
        binding.onGoogleLoginButtonClick = ::handleGoogleButtonClick
    }

    private fun handleKakaoButtonClick() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = ::handleOAuth)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = ::handleOAuth)
        }
    }

    private fun handleOAuth(token: OAuthToken?, error: Throwable?) {
        if (token != null) {
            tryToLoginWithKakao()
            return
        }
        if (error == null) return
        val errorMessage = when (error.toString()) {
            AuthErrorCause.AccessDenied.toString() -> "접근이 거부됨(동의 취소)"
            AuthErrorCause.InvalidClient.toString() -> "유효하지 않은 앱"
            AuthErrorCause.InvalidGrant.toString() -> "인증 수단이 유효하지 않아 인증할 수 없는 앱"
            AuthErrorCause.InvalidRequest.toString() -> "요청 파라미터 오류"
            AuthErrorCause.InvalidScope.toString() -> "유효하지 않은 scope ID"
            AuthErrorCause.Misconfigured.toString() -> "설정이 올바르지 않음(android key hash)"
            AuthErrorCause.ServerError.toString() -> "서버 내부 에러"
            AuthErrorCause.Unauthorized.toString() -> "앱이 요청 권한이 없음"
            else -> "기타 에러"
        }
        context?.showToast(errorMessage)
    }

    private fun tryToLoginWithKakao() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                context?.showToast("카카오 이메일을 확인할 수 없음")
                return@me
            }
            val email = user?.kakaoAccount?.email.toString()
            val password = user?.id.toString()
            loginAndDismiss(Authentication.Kakao(email, password))
        }
    }

    private fun loginAndDismiss(authentication: Authentication) {
        viewModel.login(authentication)
        dismiss()
    }

    private fun handleGoogleButtonClick() {
        googleClient.signOut()
        val signInIntent = googleClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }
}
