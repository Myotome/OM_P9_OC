package com.openclassrooms.realestatemanager.activity.addoredit

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AOEPagerAdapter (private val items: ArrayList<Fragment>, activity: AppCompatActivity) : FragmentStateAdapter(activity){

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = items[position]

}