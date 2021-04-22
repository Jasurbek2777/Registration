package com.example.registration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.registration.databinding.CameraBottomSheetBinding
import com.example.registration.databinding.FragmentRegisterBinding
import com.example.registration.db.MyDbHelper
import com.example.registration.models.Contact
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
lateinit var bindingReg: FragmentRegisterBinding
lateinit var myDbHelper: MyDbHelper
lateinit var userPicture: ByteArray

class RegisterFragment : Fragment() {
    private lateinit var list: ArrayList<String>
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userPicture= ByteArray(0)
        bindingReg = FragmentRegisterBinding.inflate(inflater, container, false)
        myDbHelper = MyDbHelper(requireContext())
        list = ArrayList()
        list.add("Uzbekistan")
        list.add("America")
        list.add("England")
        list.add("Russia")
        list.add("Italy")

        bindingReg.imageAdd.setOnClickListener {
            val btnsheet = CameraBottomSheetBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(btnsheet.root)
            btnsheet.camera.setOnClickListener {
                fromCamera()

                dialog.dismiss()
            }
            btnsheet.folder.setOnClickListener {
                fromFolder()

                dialog.dismiss()
            }

            dialog.show()

        }

        val adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, list)
        bindingReg.spinner.adapter = adapter
        bindingReg.regBtn.setOnClickListener {
            val name = bindingReg.nameEt.text.toString()
            val number = bindingReg.numberEt.text.toString()
            val adress = bindingReg.adresEt.text.toString()
            val pass = bindingReg.passwordEt.text.toString()

            if (name.isNotEmpty() && number.isNotEmpty() && adress.isNotEmpty() && pass.isNotEmpty()) {
                if (userPicture.isNotEmpty()) {
                    myDbHelper.add(Contact(name, number, "Uzbekistan", adress, pass, userPicture))

                    var bundle = Bundle()
                    bundle.putString("param1", number)
                    bundle.putString("param2", pass)
                    findNavController().popBackStack()

                    findNavController().navigate(R.id.homeFragment, bundle)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Iltimos rasmingizni belgilang",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Iltimos malumotlarni to'ldiring",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return bindingReg.root
    }


    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == -1) {
            val bitmap = data?.extras?.get("data") as Bitmap
            bindingReg.userImage.setImageBitmap(bitmap)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            userPicture = byteArray
        }
        else if (requestCode == 1 && resultCode == -1) {
                var bit: Bitmap? = null
                var uri = data?.data!!
                try {
                    bit = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri) as Bitmap
                    bindingReg.userImage.setImageBitmap(bit)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            val byteArrayOutputStream = ByteArrayOutputStream()
            bit?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            userPicture = byteArrayOutputStream.toByteArray()
        }
    }

    private fun fromFolder() {

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    val CODE_REQUEST = 1
                    startActivityForResult(intent, CODE_REQUEST)


                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            })
            .check();


    }

    private fun fromCamera() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            })
            .check();

    }


}