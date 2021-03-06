package com.openclassrooms.realestatemanager.ui.main


import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.ui.main.list.ListFragment
import com.openclassrooms.realestatemanager.ui.main.maps.MapsFragment
import com.openclassrooms.realestatemanager.ui.main.querysearch.QuerySearchFragment
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.utils.appPerms
import com.openclassrooms.realestatemanager.utils.permissionNameForUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Main activity to support toolbar, bottom view, master/detail screen
 * Assert or ask for permission first time
 * Main view model is using for synchronise and update databases
 * so in this activity, the method is observe forever
 */


@ExperimentalCoroutinesApi
@FlowPreview
@DelicateCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        checkForPermissions()
        viewModel.clearSearch()
        viewModel.synchroniseAllDatabase().observeForever{}

        val listFragment = ListFragment()
        val mapFragment = MapsFragment()


        displayCurrentFragment(listFragment, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_list -> {
                    displayCurrentFragment(listFragment, false)
                    true
                }
                R.id.bottom_menu_map -> {
                    displayCurrentFragment(mapFragment,false)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

    }

    private fun displayCurrentFragment(fragment: Fragment, isQuery: Boolean) {
        val mId = when (resources.getBoolean(R.bool.portrait_only)) {
            true -> R.id.cl_estate_list
            false -> R.id.fl_estate_detail_container
        }

        supportFragmentManager.commit {
            supportFragmentManager.findFragmentById(mId)?.let { remove(it) }
            replace(R.id.frameLayout, fragment)
            setReorderingAllowed(true)
            if(isQuery)addToBackStack("query")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.main_menu_add -> {
                viewModel.clearCurrentEstate()
                startActivity(Intent(this, AOEActivity::class.java))
                true
            }
            R.id.main_menu_edit -> {
                startActivity(Intent(this, AOEActivity::class.java))
                true
            }
            R.id.main_menu_search -> {
                displayCurrentFragment(QuerySearchFragment(), true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you really quit ?")
            builder.setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in appPerms)
                when {
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED -> {
                    }
                    shouldShowRequestPermissionRationale(permission) -> {
                        Toast.makeText(
                            this, "${
                                permissionNameForUser(permission)
                            } is required for complete usage", Toast.LENGTH_SHORT
                        ).show()
                        ActivityCompat.requestPermissions(this, appPerms, 1245)
                    }
                    else -> ActivityCompat.requestPermissions(this, appPerms, 1245)
                }
        }
    }


}
