package com.ki960213.kidsandseoul.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ActivityMainBinding
import com.ki960213.kidsandseoul.presentation.NetworkMonitor
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.join.JoinFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
    }

    val navController: NavController by lazy { navHostFragment.navController }

    private val exitCallback: SafeExitCallback by lazy { SafeExitCallback(this) }

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        checkAppUpdate()

        setupDataBinding()
        setupWindowInsetListener()
        setupBackPressedDispatcher()

        observeNetworkState()
        observeUiEvent()
    }

    private fun checkAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                showUpdateInfoDialog()
            }
        }
    }

    private fun showUpdateInfoDialog() {
        UpdateInfoDialog().show(supportFragmentManager, null)
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
    }

    private fun setupWindowInsetListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val destination = navController.currentDestination
                ?: return@setOnApplyWindowInsetsListener insets
            val bottomPadding =
                if (destination.id == R.id.firstScreenFragment) 0 else systemBars.bottom

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
    }

    private fun setupBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this) { handleBackPressed() }
    }

    private fun handleBackPressed() {
        val destination = navController.currentDestination ?: return
        if (destination.id == R.id.firstScreenFragment) {
            exitCallback.handleOnBackPressed()
        } else {
            navController.navigateUp()
        }
    }

    private fun observeNetworkState() {
        repeatOnStarted { networkMonitor.isOnline.distinctUntilChanged().collect(::handleIsOnline) }
    }

    private fun handleIsOnline(isOnline: Boolean) {
        if (!isOnline) {
            showNotOnlineDialog()
            return
        }
        val notOnlineDialog = supportFragmentManager.findFragmentByTag(NotOnlineDialog.TAG) as NotOnlineDialog?
        notOnlineDialog?.dismiss()
    }

    private fun showNotOnlineDialog() {
        NotOnlineDialog().show(supportFragmentManager, NotOnlineDialog.TAG)
    }

    private fun observeUiEvent() {
        repeatOnStarted { viewModel.uiEvent.collect(::handleUiEvent) }
    }

    private fun handleUiEvent(uiEvent: MainUiEvent) {
        when (uiEvent) {
            is MainUiEvent.NavigateToJoin -> navigateToJoin(uiEvent.authentication)
            MainUiEvent.ShowLoginDialog -> showLoginDialog()
            MainUiEvent.LoginFail -> showToast(R.string.all_login_fail)
        }
    }

    private fun navigateToJoin(authentication: Authentication) {
        val args = JoinFragment.getArgs(authentication)
        navController.navigate(R.id.joinFragment, args)
    }

    private fun showLoginDialog() {
        LoginDialog().show(supportFragmentManager, null)
    }

    override fun attachBaseContext(newBase: Context) {
        val newConfiguration = Configuration(newBase.resources.configuration)
        newConfiguration.fontScale = 1.0f
        newConfiguration.densityDpi = DisplayMetrics.DENSITY_XXHIGH
        applyOverrideConfiguration(newConfiguration)
        super.attachBaseContext(newBase)
    }
}
