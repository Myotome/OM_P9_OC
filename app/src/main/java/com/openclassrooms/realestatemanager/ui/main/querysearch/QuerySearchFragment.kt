package com.openclassrooms.realestatemanager.ui.main.querysearch

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentQuerySearchBinding
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment view for get data
 * Use view state to show correct data with live data
 */

@AndroidEntryPoint
class QuerySearchFragment : Fragment() {

    lateinit var binding: FragmentQuerySearchBinding
    private val viewModel by viewModels<QuerySearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuerySearchBinding.inflate(inflater, container, false)

        binding.apply {
            cbSearchOnSale.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSale = isChecked
            }
            cbSearchSold.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sold = isChecked
                btSearchSoldDate.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            btSearchType.setOnClickListener {
                tlQueryType.visibility =
                    if (tlQueryType.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            cbQueryPenthouse.setOnCheckedChangeListener { _, isChecked -> viewModel.penthouse = isChecked }
            cbQueryHouse.setOnCheckedChangeListener { _, isChecked -> viewModel.house = isChecked }
            cbQueryLoft.setOnCheckedChangeListener { _, isChecked -> viewModel.loft = isChecked }
            cbQueryApartment.setOnCheckedChangeListener { _, isChecked -> viewModel.apartment = isChecked }
            cbQueryCastle.setOnCheckedChangeListener { _, isChecked -> viewModel.castle = isChecked }
            cbQueryMansion.setOnCheckedChangeListener { _, isChecked -> viewModel.mansion = isChecked }

            btSearchDistrict.setOnClickListener {
                tilQueryDistrict.visibility =
                    if(tilQueryDistrict.visibility == View.VISIBLE)View.GONE else View.VISIBLE
            }
            etSearchDistrict.addTextChangedListener {
                viewModel.district = it.toString()
            }

            btSearchPrice.setOnClickListener {
                clSearchPrice.visibility =
                    if (clSearchPrice.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchPriceMin.addTextChangedListener {
                viewModel.priceMin = (it.toString().toIntOrNull())
            }
            etSearchPriceMax.addTextChangedListener {
                viewModel.priceMax = (it.toString().toIntOrNull())
            }

            btSearchSurface.setOnClickListener {
                clSearchSurface.visibility =
                    if (clSearchSurface.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchSurfaceMin.addTextChangedListener {
                viewModel.surfaceMin = (it.toString().toDoubleOrNull())
            }
            etSearchSurfaceMax.addTextChangedListener {
                viewModel.surfaceMax = (it.toString().toDoubleOrNull())
            }

            btSearchRoom.setOnClickListener {
                clSearchRoom.visibility =
                    if (clSearchRoom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchRoomMin.addTextChangedListener {
                viewModel.roomMin = (it.toString().toIntOrNull())
            }
            etSearchRoomMax.addTextChangedListener {
                viewModel.roomMax = (it.toString().toIntOrNull())
            }

            btSearchBedroom.setOnClickListener {
                clSearchBedroom.visibility =
                    if (clSearchBedroom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchBedroomMin.addTextChangedListener {
                viewModel.bedroomMin = (it.toString().toIntOrNull())
            }
            etSearchBedroomMax.addTextChangedListener {
                viewModel.bedroomMax = (it.toString().toIntOrNull())
            }

            btSearchBathroom.setOnClickListener {
                clSearchBathroom.visibility =
                    if (clSearchBathroom.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchBathroomMin.addTextChangedListener {
                viewModel.bathroomMin = it.toString().toIntOrNull()
            }
            etSearchBathroomMax.addTextChangedListener {
                viewModel.bathroomMax = it.toString().toIntOrNull()
            }

            btSearchLandsize.setOnClickListener {
                clSearchLandsize.visibility =
                    if (clSearchLandsize.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchLandsizeMin.addTextChangedListener {
                viewModel.landSizeMin = it.toString().toDoubleOrNull()
            }
            etSearchLandsizeMax.addTextChangedListener {
                viewModel.landSizeMax = it.toString().toDoubleOrNull()
            }

            btSearchInterest.setOnClickListener {
                tlSearchInterest.visibility =
                    if (tlSearchInterest.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            cbSearchSchool.setOnCheckedChangeListener { _, isChecked ->
                viewModel.school = isChecked
            }
            cbSearchStore.setOnCheckedChangeListener { _, isChecked -> viewModel.store = isChecked }
            cbSearchPark.setOnCheckedChangeListener { _, isChecked -> viewModel.park = isChecked }
            cbSearchRestaurant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.restaurant = isChecked
            }
            cbSearchMovie.setOnCheckedChangeListener { _, isChecked -> viewModel.movie = isChecked }
            cbSearchTheatre.setOnCheckedChangeListener { _, isChecked ->
                viewModel.theatre = isChecked
            }
            cbSearchSubway.setOnCheckedChangeListener { _, isChecked ->
                viewModel.subway = isChecked
            }
            cbSearchNightlife.setOnCheckedChangeListener { _, isChecked ->
                viewModel.nightlife = isChecked
            }


            btSearchEntryDate.setOnClickListener {
                clSearchEntryDate.visibility =
                    if (clSearchEntryDate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            rgSearchEntry.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_search_entry_before -> viewModel.entryDateCode = 0
                    R.id.rb_search_entry_after -> viewModel.entryDateCode = 1
                }
            }
            dpSearchEntryDate.init(2021,0,1) { _, years, month, day ->
                viewModel.entryDateText = Utils.getLongFormatDate(years, month+1, day)
            }

            btSearchSoldDate.setOnClickListener {
                clSearchSoldDate.visibility =
                    if (clSearchSoldDate.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            rgSearchSold.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_search_sold_before -> viewModel.soldDateCode = 0
                    R.id.rb_search_sold_after -> viewModel.soldDateCode = 1
                }
            }
            dpSearchDateSold.init(2022,8,1){
                _,years,month, day->
                viewModel.soldDateText = Utils.getLongFormatDate(years, month+1, day)
            }


            btSearchPhoto.setOnClickListener {
                tilQueryPhotoMin.visibility =
                    if (tilQueryPhotoMin.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            etSearchPhotoMin.addTextChangedListener {
                viewModel.photoMin = it.toString().toIntOrNull()
            }

            btSearchSearch.setOnClickListener {
                viewModel.search()
                activity?.supportFragmentManager?.popBackStack()
            }

            btSearchCancel.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        }


        return binding.root
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