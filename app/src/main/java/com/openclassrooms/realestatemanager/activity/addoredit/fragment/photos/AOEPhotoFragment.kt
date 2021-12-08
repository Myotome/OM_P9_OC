package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activity.addoredit.ADD_EDIT_PREVIOUS_RESULT
import com.openclassrooms.realestatemanager.activity.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.database.uriToString
import com.openclassrooms.realestatemanager.databinding.FragmentAddPhotoBinding
import com.openclassrooms.realestatemanager.model.Photo
import com.openclassrooms.realestatemanager.utilsforinstrutest.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AOEPhotoFragment : Fragment() {

    private val viewModel by viewModels<AOEPhotoViewModel>()
    lateinit var binding: FragmentAddPhotoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPhotoBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: photo fragment is call")


        binding.apply {
            btAddTakePhoto.setOnClickListener { dispatchTakePictureIntent() }
            btAddGallery.setOnClickListener {
                dispatchGalleryPhoto()
            }
            btPhotoFinish.setOnClickListener { viewModel.getAllData() }
            btPhotoBack.setOnClickListener {
                (activity as AOEActivity).clickToRightOrLeft(ADD_EDIT_PREVIOUS_RESULT)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditTwoEvent.collect { event ->
                when (event) {
                    is AOEPhotoViewModel.AddEditPhotoEvent.NavigateResult -> {
                        (activity as AOEActivity).clickToRightOrLeft(event.result)
                    }
                    is AOEPhotoViewModel.AddEditPhotoEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        return binding.root
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = AOEPhotoAdapter {
            popupName(it, null)
        }
        binding.rvAddPhoto.adapter = adapter

        viewModel.listPhotoLive().observe(viewLifecycleOwner) {
            adapter.submitList(it.listPhoto)
            if (it.id != null) binding.btPhotoFinish.text = "Update"
        }

    }

    private fun dispatchGalleryPhoto() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        Log.d(Companion.TAG, "dispatchTakePictureIntent: called")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            data!!.data?.let { uri ->
                context?.contentResolver?.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                popupName(uriString = uriToString(uri))

            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap

            val bytes = ByteArrayOutputStream()
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context?.contentResolver,
                takenImage,
                Utils.getTodayDate(),
                null
            )

            popupName(uriString = path)
        }
    }


    private fun popupName(photo: Photo? = null, uriString: String?) {

        val builder = AlertDialog.Builder(context)
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.alert_dialog_photo, null)
        val image = view.findViewById<ImageView>(R.id.iv_popup_photo)
        image.load(photo?.storageUriString?:uriString)
        builder.setView(view)
        val inputText = view.findViewById<EditText>(R.id.et_popup_name)
        builder.setCancelable(true)
        builder.setPositiveButton("Ok"){_,_->

            viewModel.addPhoto( internet = Utils.isInternetAvailable(requireContext()),
                name = inputText.text.toString(),
                path = uriString?:photo!!.image,
                storagePhotoId = photo?.storageId)
        }
        builder.setNegativeButton("Cancel"){_,_->}
        if(photo != null) {
            builder.setNeutralButton("Remove") { _, _ ->
                viewModel.removePhoto(photo)
            }
        }
        builder.show()


//        val builder = AlertDialog.Builder(context)
//        builder.setMessage("What is that ?")
//
//        val input = EditText(context)
//        input.hint = "Enter name"
//        input.inputType = InputType.TYPE_CLASS_TEXT
//        builder.setView(input)
//
//        builder.setPositiveButton("Ok") { _, _ ->
//            val isInternetAvailable = Utils.isInternetAvailable(requireContext())
//            viewModel.addPhoto(input.text.toString(), uriString, isInternetAvailable)
//        }
//        builder.setNegativeButton("Cancel") { _, _ -> }
//
//        builder.show()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 75321
        const val REQUEST_IMAGE_GALLERY = 954268
        const val TAG = "DEBUGKEY"
    }

}