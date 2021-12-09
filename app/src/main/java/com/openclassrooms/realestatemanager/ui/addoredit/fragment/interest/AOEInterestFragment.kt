package com.openclassrooms.realestatemanager.ui.addoredit.fragment.interest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_PREVIOUS_RESULT
import com.openclassrooms.realestatemanager.ui.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.ui.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.databinding.FragmentAddInterestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AOEInterestFragment : Fragment() {

    private val viewModel by viewModels<AOEInterestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddInterestBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: Interest Fragment is call")

        viewModel.currentEstate?.observe(viewLifecycleOwner){vs->
            binding.apply {
                if (vs != null) {
                    cbInterestSchool.isChecked = vs.school
                    cbInterestSchool.jumpDrawablesToCurrentState()
                    cbInterestStore.isChecked = vs.store
                    cbInterestStore.jumpDrawablesToCurrentState()
                    cbInterestPark.isChecked = vs.park
                    cbInterestPark.jumpDrawablesToCurrentState()
                    cbInterestRestaurant.isChecked = vs.restaurant
                    cbInterestRestaurant.jumpDrawablesToCurrentState()
                    cbInterestMovie.isChecked = vs.movie
                    cbInterestMovie.jumpDrawablesToCurrentState()
                    cbInterestTheatre.isChecked = vs.theatre
                    cbInterestTheatre.jumpDrawablesToCurrentState()
                    cbInterestSubway.isChecked = vs.subway
                    cbInterestSubway.jumpDrawablesToCurrentState()
                    cbInterestNightlife.isChecked = vs.nightlife
                    cbInterestNightlife.jumpDrawablesToCurrentState()
                }

            }
        }

        binding.apply {
            cbInterestSchool.setOnCheckedChangeListener { _, isChecked ->
                viewModel.school = isChecked
            }
            cbInterestStore.setOnCheckedChangeListener { _, isChecked ->
                viewModel.store = isChecked
            }
            cbInterestPark.setOnCheckedChangeListener { _, isChecked ->
                viewModel.park = isChecked
            }
            cbInterestRestaurant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.restaurant = isChecked
            }
            cbInterestMovie.setOnCheckedChangeListener { _, isChecked ->
                viewModel.movie = isChecked
            }
            cbInterestTheatre.setOnCheckedChangeListener { _, isChecked ->
                viewModel.theatre = isChecked
            }
            cbInterestSubway.setOnCheckedChangeListener { _, isChecked ->
                viewModel.subway = isChecked
            }
            cbInterestNightlife.setOnCheckedChangeListener { _, isChecked ->
                viewModel.nightlife = isChecked
            }
            btInterestNext.setOnClickListener {
                viewModel.onSaveClick()
            }
            btInterestBack.setOnClickListener {
                (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_PREVIOUS_RESULT)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditAddressEvent.collect { event ->
                when (event) {
                    is AOEInterestViewModel.AddEditInterestEvent.NavigateWithResult -> {
                        (activity as AOEActivity).clickToRightOrLeft(event.result)
                    }
                }
            }
        }
        return binding.root
    }


}