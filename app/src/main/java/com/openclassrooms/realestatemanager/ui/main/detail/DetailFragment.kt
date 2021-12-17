package com.openclassrooms.realestatemanager.ui.main.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.EstateDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DecimalFormat

/**
 * Fragment view for get data
 * Use view state to show correct data with live data
 */

@ExperimentalCoroutinesApi
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

        val adapter = DetailAdapter()
        binding.rvDetailPhoto.adapter = adapter

        viewModel.detailLiveData.observe(viewLifecycleOwner) {


            binding.apply {
                tvDetailFastDescription.text =
                    "${it.type}, ${it.rooms ?: "-"} rooms, ${it.surface} m² "
                tvDetailPrice.text = "${DecimalFormat("#,###").format(it.price)} $"
                tvDetailDescription.text = it.description
                tvDetailBedrooms.text = "Bedrooms : ${it.bedrooms ?: "-"}"
                tvDetailBathrooms.text = "Bathrooms : ${it.bathrooms ?: "-"}"
                tvDetailSurface.text = "Surface : ${it.surface} m²"
                tvDetailRooms.text = "Rooms : ${it.rooms ?: "-"}"
                tvDetailLandSize.text = "Land size : ${it.landSize ?: "-"} m²"

                ivDetailMap.load(it.formattedAddress){
                    transformations(RoundedCornersTransformation(radius = 20.5F))
                }

                tvDetailSchool.visibility = if (it.school) View.VISIBLE else View.GONE
                tvDetailStore.visibility = if (it.store) View.VISIBLE else View.GONE
                tvDetailPark.visibility = if (it.park) View.VISIBLE else View.GONE
                tvDetailRestaurant.visibility = if (it.restaurant) View.VISIBLE else View.GONE
                tvDetailMovie.visibility = if (it.movie) View.VISIBLE else View.GONE
                tvDetailTheatre.visibility = if (it.theatre) View.VISIBLE else View.GONE
                tvDetailSubway.visibility = if (it.subway) View.VISIBLE else View.GONE
                tvDetailNightlife.visibility = if (it.nightlife) View.VISIBLE else View.GONE
                tvDetailSold.visibility = if (it.onSale) View.GONE else View.VISIBLE

                tvDetailMapAddress.text = it.address
                tvDetailRealtor.text = "Realtor : ${it.realtor}"
                tvDetailEntryDate.text = "Entry date : ${it.entryDate}"

                tvDetailModDate.visibility =
                    if (it.modificationDate.isNotBlank()) View.VISIBLE else View.GONE
                tvDetailModDate.text = "Modification date : ${it.modificationDate}"

                tvDetailSoldDate.visibility =
                    if (it.soldDate.isNotBlank()) View.VISIBLE else View.GONE
                tvDetailSoldDate.text = "Sold date : ${it.soldDate}"

                adapter.submitList(it.photoList)

            }

            if (resources.getBoolean(R.bool.portrait_only)) {
                binding.apply {
                    btDetailDetail?.setOnClickListener {
                        tlDetailDetail?.visibility =
                            if (tlDetailDetail?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }
                    btDetailTitleDescription.setOnClickListener {
                        tvDetailDescription.visibility =
                            if (tvDetailDescription.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }
                    btDetailInterest?.setOnClickListener {
                        tlDetailInterest.visibility =
                            if (tlDetailInterest.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }
                    btDetailMap?.setOnClickListener {
                        clDetailMap?.visibility =
                            if (clDetailMap?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }
                    btDetailInformation?.setOnClickListener {
                        clDetailInformation?.visibility =
                            if (clDetailInformation?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                    }
                }
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
        menu.findItem(R.id.main_menu_search).isVisible = !resources.getBoolean(R.bool.portrait_only)
        super.onCreateOptionsMenu(menu, inflater)
    }

}