package com.example.gatherpoint

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.gatherpoint.network.ApiInterface
import com.example.gatherpoint.network.RetrofitHelper
import com.example.gatherpoint.utils.Prefs
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var canExitSplashScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {

        //splash screen object, which you can optionally use to customize animation or
        // keep the splash screen on screen for a longer duration
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        processBootData()

        splashScreen.setKeepOnScreenCondition { !canExitSplashScreen }

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            startSplashScreenExitAnimation(splashScreenView)
        }

        val api = RetrofitHelper.getInstance()
        // launching a new coroutine
        GlobalScope.launch {
            val response = api.login()
            if (response.isSuccessful) {
                launch(Dispatchers.Main) {
                    Log.d("xddd", "onCreate: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun processBootData() = lifecycleScope.launch(Dispatchers.IO) {
        setNavigationGraph(
            if (Prefs(this@MainActivity).userLoggedPref) {
                R.id.dashboardFragment
            } else {
                R.id.loginFragment
            }
        )
        canExitSplashScreen = true
    }

    private suspend fun setNavigationGraph(startDestination: Int) = withContext(Dispatchers.Main) {
        val navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph
    }

    private fun startSplashScreenExitAnimation(splashScreenView: SplashScreenViewProvider) {
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView.view,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.view.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = 800L

        slideUp.doOnEnd { splashScreenView.remove() }

        slideUp.start()
    }
}