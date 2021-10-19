package com.openclassrooms.realestatemanager.activity.main.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.EstateDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class DetailFragment : Fragment() {

    lateinit var binding: EstateDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = EstateDetailBinding.inflate(inflater, container, false)

        val adapter = DetailAdapter{}
        binding.rvDetailPhoto.adapter = adapter

        viewModel.detailLiveData.observe(viewLifecycleOwner) {

            binding.apply {
                tvDetailFastDescription.text =
                    "${it.type}, ${it.rooms ?: "-"} rooms, ${it.surface} m2 "
                tvDetailPrice.text = "${DecimalFormat("#,###").format(it.price)} $"
                tvDetailDescription.text = it.description
                tvDetailBedrooms.text = "Bedrooms : ${it.bedrooms ?: "-"}"
                tvDetailBathrooms.text = "Bathrooms : ${it.bathrooms ?: "-"}"
                tvDetailSurface.text = "Surface : ${it.surface}"
                tvDetailRooms.text = "Rooms : ${it.rooms ?: "-"}"
                tvDetailLandSize.text = "Land size : ${it.landSize ?: "-"}"

                tvDetailSchool.visibility = if (it.school) View.VISIBLE else View.GONE
                tvDetailStore.visibility = if(it.store)View.VISIBLE else View.GONE
                tvDetailPark.visibility = if (it.park) View.VISIBLE else View.GONE
                tvDetailRestaurant.visibility = if(it.restaurant)View.VISIBLE else View.GONE
                tvDetailMovie.visibility = if (it.movie) View.VISIBLE else View.GONE
                tvDetailTheatre.visibility = if(it.theatre)View.VISIBLE else View.GONE
                tvDetailSubway.visibility = if (it.subway) View.VISIBLE else View.GONE
                tvDetailNightlife.visibility = if(it.nightlife)View.VISIBLE else View.GONE
                adapter.submitList(it.photoList)

            }


        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.main_menu_edit).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }
}