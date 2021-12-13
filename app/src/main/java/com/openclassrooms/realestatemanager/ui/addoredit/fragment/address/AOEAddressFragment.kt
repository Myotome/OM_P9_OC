package com.openclassrooms.realestatemanager.ui.addoredit.fragment.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.ui.addoredit.ADD_EDIT_PREVIOUS_RESULT
import com.openclassrooms.realestatemanager.ui.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.databinding.FragmentAddAddressBinding
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect


@DelicateCoroutinesApi
@AndroidEntryPoint
class AOEAddressFragment : Fragment() {

    private val viewModel by viewModels<AOEAddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddAddressBinding.inflate(inflater, container, false)

        viewModel.currentEstate?.observe(viewLifecycleOwner) { vs ->
            if (vs != null) {
                binding.apply {
                    etAddNumber.setText(vs.number.toString())
                    etAddComplement.setText(vs.complement)
                    etAddStreet.setText(vs.street)
                    etAddDistrict.setText(vs.district)
                    etAddCity.setText(vs.city)
                }
                viewModel.lat = vs.lat
                viewModel.lng = vs.lng
            }
        }

        binding.apply {
            etAddDistrict.addTextChangedListener { viewModel.district = (it.toString()) }
            etAddNumber.addTextChangedListener { viewModel.number = (it.toString().toIntOrNull()) }
            etAddComplement.addTextChangedListener { viewModel.complement = (it.toString()) }
            etAddStreet.addTextChangedListener { viewModel.street = (it.toString()) }
            etAddCity.addTextChangedListener { viewModel.city = (it.toString()) }

            btAddressNext.setOnClickListener {
                if(Utils.isInternetAvailable(requireContext())) viewModel.internetAvailable = true
                viewModel.onSaveClick() }
            btAddressBack.setOnClickListener {
                (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_PREVIOUS_RESULT)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditAddressEvent.collect { event ->
                when (event) {
                    is AOEAddressViewModel.AddEditAddressEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AOEAddressViewModel.AddEditAddressEvent.NavigateWithResult -> {
                        (activity as AOEActivity).clickToRightOrLeft(event.result)
                    }
                }
            }
        }

        return binding.root
    }

}