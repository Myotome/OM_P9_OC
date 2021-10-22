package com.openclassrooms.realestatemanager.activity.addoredit.fragment.partone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_BACK_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.databinding.FragmentAddPartOneBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AOEPartOneFragment : Fragment() {

    private val viewModel by viewModels<AOEPartOneViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddPartOneBinding.inflate(inflater, container, false)

        viewModel.currentEstate.observe(viewLifecycleOwner) { vs ->
            if (vs != null) {
                binding.apply {
                    swOneSale.isChecked = vs.onSale
                    swOneSale.jumpDrawablesToCurrentState()
                    etAddType.setText(vs.type)
                    etAddPrice.setText(vs.price.toString())
                    etAddSurface.setText(vs.surface.toString())
                    etAddRooms.setText(vs.room?.toString()?:"")
                    etAddLandsize.setText(vs.landSize?.toString()?:"")
                }
            }
        }

        binding.apply {
            swOneSale.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSale = isChecked  }
            etAddType.addTextChangedListener { viewModel.type = (it.toString()) }
            etAddPrice.addTextChangedListener { viewModel.price = (it.toString().toInt()) }
            etAddSurface.addTextChangedListener { viewModel.surface = (it.toString().toDouble()) }
            etAddRooms.addTextChangedListener { viewModel.rooms = (it.toString().toIntOrNull()) }
            etAddLandsize.addTextChangedListener {
                viewModel.landsize = (it.toString().toDoubleOrNull())
            }

            btOneNext.setOnClickListener { viewModel.onSaveClick() }
            btOneBack.setOnClickListener {
                (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_BACK_RESULT)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditOneEvent.collect { event ->
                when (event) {
                    is AOEPartOneViewModel.AddEditOneEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AOEPartOneViewModel.AddEditOneEvent.NavigateWithResult -> {
                        (activity as AOEActivity).clickToRightOrLeft(event.result)
                    }
                }
            }
        }

        return binding.root
    }


}