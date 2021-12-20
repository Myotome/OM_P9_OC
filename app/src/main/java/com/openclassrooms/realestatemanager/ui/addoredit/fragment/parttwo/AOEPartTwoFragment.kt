package com.openclassrooms.realestatemanager.ui.addoredit.fragment.parttwo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_PREVIOUS_RESULT
import com.openclassrooms.realestatemanager.ui.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.databinding.FragmentAddPartTwoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

/**
 * Fragment view for get data in create or edition of estate
 * Use view state to show correct data with live data
 * Use flow channel for show event or change page on viewPager
 */

@DelicateCoroutinesApi
@AndroidEntryPoint
@FlowPreview
@ExperimentalCoroutinesApi
class AOEPartTwoFragment : Fragment() {

    private val viewModel by viewModels<AOEPartTwoViewModel>()


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddPartTwoBinding.inflate(inflater, container, false)

         viewModel.currentEstate?.observe(viewLifecycleOwner){vs->
             if(vs!=null){
                 binding.apply {
                     etAddBedrooms.setText(vs.bedroom?.toString()?: "")
                     etAddBathrooms.setText(vs.bathroom?.toString()?:"")
                     etAddDescription.setText(vs.description)
                     etAddRealtor.setText(vs.realtor)
                     viewModel.estateId = vs.id
                     viewModel.entryDate = vs.dateEntry
                     viewModel.firestoreId = vs.firestoreId
                 }
             }

         }

         binding.apply {
             etAddBedrooms.addTextChangedListener{viewModel.bedrooms = (it.toString().toIntOrNull())}
             etAddBathrooms.addTextChangedListener{viewModel.bathrooms = (it.toString().toIntOrNull())}
             etAddDescription.addTextChangedListener{viewModel.description = (it.toString())}
             etAddRealtor.addTextChangedListener{viewModel.realtor = (it.toString())}

             btTwoNext.setOnClickListener { viewModel.onSaveClick() }
             btTwoBack.setOnClickListener {
                 (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_PREVIOUS_RESULT)
             }
         }

         viewLifecycleOwner.lifecycleScope.launchWhenCreated {
             viewModel.addEditTwoEvent.collect { event->
                 when (event){
                     is AOEPartTwoViewModel.AddEditTwoEvent.NavigateResult -> {
                         (activity as AOEActivity).clickToRightOrLeft(event.result)
                     }
                 }
             }
         }

        return binding.root
    }
}