package com.openclassrooms.realestatemanager.ui.main.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.main.detail.DetailFragment
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.permissionNameForUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel by viewModels<MapsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapsBinding.inflate(inflater, container, false)

        viewModel.getViewState().observe(viewLifecycleOwner) {
            displayCurrentPosition(it.currentLat, it.currentLng)
            displayMarker(it.listEstate)
        }

        viewModel.isSearching.observe(viewLifecycleOwner){
            if (it){
                binding.btMapClear.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { viewModel.clearSearch() }
                }
            }else {
                binding.btMapClear.visibility = View.GONE
            }
        }

        if(Utils.isInternetAvailable(requireContext())) {
            viewModel.assertAllEstateHadLatLng()
        }

        return binding.root
    }

    private fun displayCurrentPosition(currentLat: Double, currentLng: Double) {
        val currentLocation = LatLng(currentLat, currentLng)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true
            }else {
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    Toast.makeText(
                        requireContext(), "${
                            permissionNameForUser(Manifest.permission.ACCESS_FINE_LOCATION)
                        } is required for use map", Toast.LENGTH_LONG
                    ).show()

            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13F))
            googleMap.uiSettings.isZoomControlsEnabled = true
        }
    }



    private fun displayMarker(list: List<MapsEstateViewState>?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            googleMap.clear()

            if (list != null) {
                for (estate in list) {
                    val estatePosition = LatLng(estate.lat, estate.lng)
                    val marker = googleMap.addMarker(MarkerOptions().position(estatePosition))
                    marker!!.tag = estate.id
                }


                googleMap.setOnMarkerClickListener { marker ->
                    viewModel.isCurrentEstate(marker.tag!!.toString().toLong())

                    parentFragmentManager.commit {
                        replace<DetailFragment>(R.id.map)
                        setReorderingAllowed(true)
                        addToBackStack("detail")
                    }
                    true
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.main_menu_edit).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
}