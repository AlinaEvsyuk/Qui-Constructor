package com.evsyuk.quizconstructor.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.evsyuk.quizconstructor.R
import com.evsyuk.quizconstructor.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavController()
    }

    private fun setupNavController() {
        val fragmentManager: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_container_view) as NavHostFragment
        val navController = fragmentManager.navController

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            navViewSetVisibility(destination.id)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_screen,
                R.id.navigation_quiz_list_screen,
                R.id.navigation_profile,
                R.id.navigation_about,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationBar: BottomNavigationView = binding.bottomNavigationBar
        bottomNavigationBar.setupWithNavController(navController)
    }

    private fun navViewSetVisibility(destinationId: Int){
        when (destinationId) {
            R.id.navigation_create_quiz_screen -> hideBottomBar()
            R.id.navigation_playQuiz_screen -> hideBottomBar()
            R.id.navigation_result_screen -> hideBottomBar()
            else -> showBottomBar()
        }
    }

    private fun showBottomBar() {
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun hideBottomBar() {
        binding.bottomNavigationBar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.home_container_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}