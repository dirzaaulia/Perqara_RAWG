package com.dirzaaulia.perqararawg.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dirzaaulia.perqararawg.R
import com.dirzaaulia.perqararawg.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        setupInsetter()
    }

    private fun setupInsetter() {
        binding.root.applyInsetter {
            type(statusBars = true, navigationBars = true) {
                padding()
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.apply {
            this@MainActivity.navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            val navController = this@MainActivity.navHostFragment.navController
            bottomNav.setupWithNavController(navController)

            lifecycleScope.launchWhenResumed {
                navController.addOnDestinationChangedListener { _, destination, _ ->

                    when (destination.id) {
                        R.id.homeFragment -> {
                            binding.apply {
                                toolbar.apply {
                                    title = getString(R.string.home_title)
                                    isTitleCentered = false
                                    menu.getItem(0).isVisible = false
                                }
                                bottomNav.isVisible = true
                            }
                        }
                        R.id.favoriteFragment -> {
                            binding.apply {
                                toolbar.apply {
                                    title = getString(R.string.favorite_title)
                                    isTitleCentered = false
                                    menu.getItem(0).isVisible = false
                                }
                                bottomNav.isVisible = true
                            }
                        }
                        R.id.detailFragment -> {
                            binding.apply {
                                toolbar.apply {
                                    title = getString(R.string.detail_title)
                                    isTitleCentered = true
                                    menu.getItem(0).isVisible = true
                                }
                                bottomNav.isVisible = false
                            }
                        }
                    }


                }
            }
        }
    }
}