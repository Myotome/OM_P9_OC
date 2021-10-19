package com.openclassrooms.realestatemanager.activity.addoredit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.address.AOEAddressFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.interest.AOEInterestFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone.AOEPartOneFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo.AOEPartTwoFragment
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment
import com.openclassrooms.realestatemanager.activity.main.MainActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddBinding
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

        val fragments = arrayListOf(
            AOEPartOneFragment(),
            AOEPartTwoFragment(),
            AOEAddressFragment(),
            AOEInterestFragment(),
            AOEPhotoFragment()
        )
//        val title = arrayListOf("One", "Two", "Three", "Four", "Five")

        val adapter = AOEPagerAdapter(fragments, this)
        binding.vpAddPager.adapter = adapter
//        TabLayoutMediator(binding.tbAddTabs, binding.vpAddPager){
//            tab, position ->
//            tab.text = title[position]
//        }.attach()

    }

    override fun onBackPressed() {
        when(binding.vpAddPager.currentItem){
            0-> super.onBackPressed()
            else-> binding.vpAddPager.currentItem -= 1
        }
    }

    internal fun clickToRightOrLeft(int: Int){
        when(int){
            0->onBackPressed()
            1-> binding.vpAddPager.currentItem-= 1
            2-> binding.vpAddPager.currentItem+= 1
            3->{Snackbar.make(binding.root, "Estate created", Snackbar.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }
    }
}
