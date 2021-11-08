package com.openclassrooms.realestatemanager.activity.main


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
import com.openclassrooms.realestatemanager.activity.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.activity.main.list.ListFragment
import com.openclassrooms.realestatemanager.activity.main.maps.MapsFragment
import com.openclassrooms.realestatemanager.activity.main.querysearch.QuerySearchFragment
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.utils.appPerms
import com.openclassrooms.realestatemanager.utils.permissionNameForUser
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        checkForPermissions()

        val listFragment = ListFragment()
        val mapFragment = MapsFragment()


        displayCurrentFragment(listFragment)


        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_list -> {
                    displayCurrentFragment(listFragment)
                    true
                }
                R.id.bottom_menu_map -> {
                    displayCurrentFragment(mapFragment)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }


//        requestedOrientation = when (resources.getBoolean(R.bool.portrait_only)) {
//            true -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            else -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//
//        }

    }

    private fun displayCurrentFragment(fragment: Fragment) {
        val mId = when (resources.getBoolean(R.bool.portrait_only)) {
            true -> R.id.cl_estate_list
            false -> R.id.fl_estate_detail_container
        }

        supportFragmentManager.commit {
            supportFragmentManager.findFragmentById(mId)?.let { remove(it) }
            replace(R.id.frameLayout, fragment)
            setReorderingAllowed(true)
//            addToBackStack("main")
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
                displayCurrentFragment(QuerySearchFragment())
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
