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
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.kidsandseoul.BuildConfig
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.DialogLoginBinding
import com.ki960213.kidsandseoul.presentation.common.base.KasBottomSheetDialogFragment

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
        binding.onGoogleLoginButtonClick = ::handleGoogleButtonClick
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
