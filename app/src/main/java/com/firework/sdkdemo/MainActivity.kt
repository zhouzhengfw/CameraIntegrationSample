package com.firework.sdkdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.firework.sdkdemo.databinding.ActivityMainBinding
import com.loopnow.library.auth.activity.LoginWebViewActivity
import com.loopnow.library.auth.api.Api
import com.loopnow.library.auth.util.FWEventName
import com.loopnow.library.auth.util.Helper
import com.loopnow.library.auth.util.ProviderUtil
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val REQUEST_RESULT = 1001

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.apply {

            if (this.resultCode == REQUEST_RESULT) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        val credential = Api.credential
                        if (credential != null && !credential.accessToken.isNullOrBlank()) {
                            lifecycleScope.launch {
                                try {
                                    Api.syncCurrentOrganization()
                                    ProviderUtil.trackVisitorEvents(FWEventName.SESSION_LOGGED_IN)
//                                                launchHome()
                                } catch (e: Exception) {
                                    Helper.showToast(
                                        this@MainActivity, this@MainActivity.getString(
                                            com.loopnow.library.auth.R.string.login_failed
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Helper.showToast(
                        this@MainActivity,
                        this@MainActivity.getString(com.loopnow.library.auth.R.string.login_failed)
                    )
                }
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, LoginWebViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//            requireActivity().startActivityForResult(intent, 0)
            laucher.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}