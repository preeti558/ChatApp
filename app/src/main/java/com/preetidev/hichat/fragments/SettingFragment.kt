package com.preetidev.hichat.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.preetidev.hichat.R
import com.preetidev.hichat.Utils
import com.preetidev.hichat.databinding.FragmentSettingBinding
import java.io.ByteArrayOutputStream
import java.util.*


class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    private lateinit var storageRef: StorageReference
    lateinit var storage: FirebaseStorage
    var uri: Uri? = null
    lateinit var bitmap: Bitmap
    var imageUrl:String=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        binding.lifecycleOwner = viewLifecycleOwner

        // Other setup code...

        // Set click listener for settingBack_btn
        binding.settingBackBtn.setOnClickListener {
            // Navigate to HomeFragment
            navigateToHomeFragment()
        }
        binding.settingUpdateButton.setOnClickListener {
            updateProfile()
        }
        binding.settingUpdateImage.setOnClickListener {

            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your profile picture")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {

                        takePhotoWithCamera()


                    }

                    options[item] == "Choose from Gallery" -> {
                        pickImageFromGallery()
                    }

                    options[item] == "Cancel" -> dialog.dismiss()
                }
            }
            builder.show()
        }
        // Load the image only if there is a valid URL
        // This assumes you have a valid imageUrl variable
        // and it is set after uploading the image
        if (imageUrl.isNotBlank()) {
            loadImage(imageUrl)
        }

    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery() {

        val pickPictureIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickPictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(pickPictureIntent, Utils.REQUEST_IMAGE_PICK)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoWithCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE)


    }

    private fun updateProfile() {
        val context = requireContext()

        val hashMapUser =
            hashMapOf<String, Any>(
                "username" to binding.settingUpdateName.text.toString(),
                "imageUrl" to "YourImageURL"
            )

        FirebaseFirestore.getInstance().collection("Users")
            .document(Utils.getUidLoggedIn())
            .update(hashMapUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

    }


    private fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()

        // Replace the current fragment with HomeFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, homeFragment)
            .addToBackStack(null) // Add to back stack if you want to enable the back button to navigate back
            .commit()
    }

    private fun loadImage(imageUrl: String) {

        Glide.with(requireContext()).load(imageUrl).placeholder(R.drawable.person).dontAnimate()
            .into(binding.settingUpdateImage)


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                Utils.REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    uploadImageToFirebaseStorage(imageBitmap)
                }

                Utils.REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    val imageBitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                    uploadImageToFirebaseStorage(imageBitmap)
                }
            }
        }


    }

    private fun uploadImageToFirebaseStorage(imageBitmap: Bitmap?) {

        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        bitmap = imageBitmap!!

        binding.settingUpdateImage.setImageBitmap(imageBitmap)

        val storagePath = storageRef.child("Photos/${UUID.randomUUID()}.jpg")
        val uploadTask = storagePath.putBytes(data)
        uploadTask.addOnSuccessListener {


            val task = it.metadata?.reference?.downloadUrl

            task?.addOnSuccessListener {

                uri = it

            }

            Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload image!", Toast.LENGTH_SHORT).show()
        }
    }



}