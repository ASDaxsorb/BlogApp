package com.axellsolis.blogapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.axellsolis.blogapp.R
import com.axellsolis.blogapp.databinding.ActivityMainBinding
import com.axellsolis.blogapp.ui.home.HomeFragment
import com.axellsolis.blogapp.utils.gone
import com.axellsolis.blogapp.utils.show

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.createPostFragment,
            R.id.profileFragment,
            R.id.loginFragment,
        ).build()

        navController = navHostFragment.navController
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    val home = (navHostFragment.childFragmentManager.fragments[0]) as HomeFragment
                    home.scrollToTop()
                }
            }
        }
        onDestinationChange()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun onDestinationChange() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationView.gone()
                }
                R.id.registerFragment -> {
                    binding.bottomNavigationView.gone()
                }
                R.id.setupProfileFragment -> {
                    binding.bottomNavigationView.gone()
                }
                else -> {
                    binding.bottomNavigationView.show()
                }
            }
        }
    }

}