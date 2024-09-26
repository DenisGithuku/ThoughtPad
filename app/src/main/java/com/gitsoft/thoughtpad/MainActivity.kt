package com.gitsoft.thoughtpad

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.gitsoft.thoughtpad.databinding.ActivityMainBinding
import com.gitsoft.thoughtpad.model.ThemeConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when (it.themeConfig) {
                        ThemeConfig.LIGHT -> AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )

                        ThemeConfig.DARK -> AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )

                        ThemeConfig.SYSTEM -> AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        )
                    }
                }
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController)
        setContentView(binding.root)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}