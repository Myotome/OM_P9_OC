package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.activity.addoredit.AOEActivity
import com.openclassrooms.realestatemanager.activity.addoredit.fragment.parttwo.AOEPartTwoViewModel
import com.openclassrooms.realestatemanager.databinding.FragmentAddPhotoBinding
import dagger.hilt.android.AndroidEntryPoint
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

        val adapter = AOEPhotoAdapter {}
        binding.rvAddPhoto.adapter = adapter

        viewModel.listPhotoLive().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.btAddTakePhoto.setOnClickListener { dispatchTakePictureIntent() }
        binding.btAddGallery.setOnClickListener { dispatchGalleryPhoto() }

        binding.btPhotoFinish.setOnClickListener{viewModel.getAllData()}

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditTwoEvent.collect { event->
                when (event){
                    is AOEPhotoViewModel.AddEditPhotoEvent.NavigateResult -> {
                        (activity as AOEActivity).clickToRightOrLeft(event.result)
                    }
                }
            }
        }
        return binding.root
    }

    private fun dispatchGalleryPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        Log.d(Companion.TAG, "dispatchTakePictureIntent: called")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: is call")
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data?.data != null) {
                Log.d(TAG, "onActivityResult: ${data.data!!}")
                popupName(data.data!!)
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult from take picture is call")
            val takenImage = data?.extras?.get("data") as Bitmap

            val bytes = ByteArrayOutputStream()
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context?.contentResolver,
                takenImage,
                "Title",
                null
            )
            val image = Uri.parse(path.toString())

            popupName(image)
        }
    }

    private fun popupName(imageUri: Uri) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("What is that ?")

        val input = EditText(context)
        input.hint = "Enter name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Ok") { _, _ ->
            viewModel.addPhoto(input.text.toString(), imageUri)
        }
        builder.setNegativeButton("Cancel") { _, _ -> }

        builder.show()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 75321
        const val REQUEST_IMAGE_GALLERY = 954268
        const val TAG = "DEBUGKEY"
    }

}