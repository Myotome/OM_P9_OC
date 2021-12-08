package com.openclassrooms.realestatemanager.activity.main.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos.AOEPhotoFragment.Companion.TAG
import com.openclassrooms.realestatemanager.activity.main.detail.DetailFragment
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import com.openclassrooms.realestatemanager.utilsforinstrutest.Utils
import com.openclassrooms.realestatemanager.utilsforinstrutest.permissionNameForUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel by viewModels<MapsViewModel>()

    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapsBinding.inflate(inflater, container, false)
//        Log.d(TAG, "onCreateView: I'm here")

        viewModel.getViewState().observe(viewLifecycleOwner) {
//            Log.d(TAG, "onCreateView:  viewModelObserve is call")
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
            Log.d(TAG, "onCreateView: i'm here")
            viewModel.assertAllEstateHadLatLng()
        }

        return binding.root
    }

    private fun displayCurrentPosition(currentLat: Double, currentLng: Double) {
//        Log.d(TAG, "displayCurrentPosition: is call")
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
//        Log.d(TAG, "displayMarker: I'm here")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            googleMap.clear()

            if (list != null) {
                for (estate in list) {
//                    Log.d(TAG, "displayMarker: value of Lat ${estate.lat} and lng ${estate.lng}")
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