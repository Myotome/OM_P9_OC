package com.openclassrooms.realestatemanager.ui.addoredit.fragment.partone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_BACK_RESULT
import com.openclassrooms.realestatemanager.ui.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.databinding.FragmentAddPartOneBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect

@DelicateCoroutinesApi
@AndroidEntryPoint
class AOEPartOneFragment : Fragment() {

    private val viewModel by viewModels<AOEPartOneViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddPartOneBinding.inflate(inflater, container, false)

        viewModel.currentEstate?.observe(viewLifecycleOwner) { vs ->
            if (vs != null) {
                binding.apply {
                    swOneSale.isChecked = vs.onSale
                    swOneSale.jumpDrawablesToCurrentState()
                    spAddType.setSelection(
                        (spAddType.adapter as ArrayAdapter<String>).getPosition(
                            vs.type
                        )
                    )
                    etAddPrice.setText(vs.price.toString())
                    etAddSurface.setText(vs.surface.toString())
                    etAddRooms.setText(vs.room?.toString() ?: "")
                    etAddLandsize.setText(vs.landSize?.toString() ?: "")
                }
            }
        }

        binding.apply {
            swOneSale.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSale = isChecked
            }

            spAddType.apply {
                adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(R.array.type)
                )
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.type = parent?.getItemAtPosition(position).toString()
                    }
                }
            }
            etAddPrice.addTextChangedListener { viewModel.price = (it.toString().toIntOrNull()) }
            etAddSurface.addTextChangedListener { viewModel.surface = (it.toString().toDoubleOrNull()) }
            etAddRooms.addTextChangedListener { viewModel.rooms = (it.toString().toIntOrNull()) }
            etAddLandsize.addTextChangedListener {
                viewModel.landsize = (it.toString().toDoubleOrNull())
            }

            btOneNext.setOnClickListener { viewModel.onSaveClick() }
            btOneBack.setOnClickListener {
                (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_BACK_RESULT)
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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