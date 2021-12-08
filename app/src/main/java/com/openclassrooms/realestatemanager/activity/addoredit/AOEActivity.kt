package com.openclassrooms.realestatemanager.activity.addoredit

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.address.AOEAddressFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.interest.AOEInterestFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone.AOEPartOneFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo.AOEPartTwoFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.activity.main.MainActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddBinding
import com.openclassrooms.realestatemanager.utilsforinstrutest.appPerms
import com.openclassrooms.realestatemanager.utilsforinstrutest.permissionNameForUser
import dagger.hilt.android.AndroidEntryPoint

const val ADD_EDIT_BACK_RESULT = 0
const val ADD_EDIT_PREVIOUS_RESULT = 1
const val ADD_EDIT_NEXT_RESULT = 2
const val ADD_EDIT_FINISH_RESULT = 3

@AndroidEntryPoint
class AOEActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForPermissions()

        val fragments = arrayListOf(
            AOEPartOneFragment(),
            AOEPartTwoFragment(),
            AOEAddressFragment(),
            AOEInterestFragment(),
            AOEPhotoFragment()
        )


        val adapter = AOEPagerAdapter(fragments, this)
        binding.vpAddPager.adapter = adapter
        binding.vpAddPager.isUserInputEnabled = false

    }

    override fun onBackPressed() {
        when (binding.vpAddPager.currentItem) {
            0 -> super.onBackPressed()
            else -> binding.vpAddPager.currentItem -= 1
        }
    }

    internal fun clickToRightOrLeft(int: Int) {
        Log.d(
            TAG,
            "clickToRightOrLeft: int : $int, currentItem : ${binding.vpAddPager.currentItem}"
        )
        when (int) {
            0 -> onBackPressed()
            1 -> binding.vpAddPager.currentItem -= 1
            2 -> binding.vpAddPager.currentItem += 1
            3 -> {
                Snackbar.make(binding.root, "Estate created", Snackbar.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

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
