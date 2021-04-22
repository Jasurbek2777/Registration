package com.example.registration

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.registration.R
import com.example.registration.bindingReg
import com.example.registration.databinding.FragmentHomeBinding
import com.example.registration.db.MyDbHelper
import com.example.registration.models.Contact
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var binding: FragmentHomeBinding

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var list: ArrayList<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        list = ArrayList()
        myDbHelper = MyDbHelper(requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        if (param1?.isNotEmpty() == true && param2?.isNotEmpty()!!) {
            binding.number.setText(param1)
            binding.password.setText(param2)
        }
        binding.enterBtn.setOnClickListener {
            Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val number = binding.number.text.toString()
                        val pass = binding.password.text.toString()
                        if (number.isNotEmpty() && pass.isNotEmpty()) {
                            var a = true

                            list = myDbHelper.allContacts()
                            for (contact in list) {
                                if (contact.password == pass && number == contact.number) {
                                    a = true
                                    findNavController().navigate(R.id.allContactsFragment)
                                    break
                                } else {
                                    a = false
                                }
                            }
                            if (!a) {
                                Toast.makeText(
                                    requireContext(),
                                    "Raqam yoki parol xato !!!",
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
                        binding.number.text.clear()
                        binding.password.text.clear()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {


                        Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()

                    }

                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                        p1?.continuePermissionRequest()
                    } })
                .check();

        }
        return binding.root
    }


}