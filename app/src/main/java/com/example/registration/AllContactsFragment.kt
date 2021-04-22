package com.example.registration

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.registration.R.*
import com.example.registration.adapters.RvAdapter
import com.example.registration.databinding.BottomSheetBinding
import com.example.registration.databinding.FragmentAllContactsBinding
import com.example.registration.db.MyDbHelper
import com.example.registration.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

lateinit var adapter: RvAdapter
lateinit var bindingAll: FragmentAllContactsBinding
lateinit var myDb: MyDbHelper
lateinit var list: ArrayList<Contact>
lateinit var btnsheet: BottomSheetBinding

class AllContactsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        myDb = MyDbHelper(requireContext())
        list = myDb.allContacts()
        adapter = RvAdapter(list, object : RvAdapter.setOnClick {
            @SuppressLint("ResourceType")
            override fun itemOnCLick(contact: Contact, position: Int) {

                askPermission(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS) {
                    btnsheet = BottomSheetBinding.inflate(
                        LayoutInflater.from(requireContext()),
                        null,
                        false
                    )
                    val dialog =
                        BottomSheetDialog(requireContext())
                    dialog.setContentView(btnsheet.root)
                    dialog.setCancelable(true)
                    btnsheet.itemName.setText(contact.name)
                    btnsheet.itemNumber.setText(contact.number)
                    val image = contact.image
                    var bitmap = image?.size?.let { BitmapFactory.decodeByteArray(image, 0, it) }
                    btnsheet.itemImage.setImageBitmap(bitmap)
                    btnsheet.root.setBackgroundResource(Color.TRANSPARENT)
                    btnsheet.call.setOnClickListener {

                        val uri = "tel:" + contact.number
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.setData(Uri.parse(uri))
                        binding.root.context.startActivity(intent)
                        dialog.cancel()
                    }

                    btnsheet.sendSms.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("param1", contact.name)
                        bundle.putString("param2", contact.number)
                        dialog.dismiss()
                        findNavController().navigate(R.id.smsFragment, bundle)
                    }
                    dialog.show()


                }.onDeclined { e ->
                    if (e.hasDenied()) {
                        e.denied.forEach {
                        }

                        AlertDialog.Builder(requireContext())
                            .setMessage("Please accept our permissions")
                            .setPositiveButton("yes") { dialog, which ->
                                e.askAgain();
                            }
                            .setNegativeButton("no") { dialog, which ->
                                dialog.dismiss();
                            }
                            .show();
                    }

                    if (e.hasForeverDenied()) {
                        e.foreverDenied.forEach {
                        }
                        e.goToSettings();
                    }
                }


            }
        })
        bindingAll = FragmentAllContactsBinding.inflate(inflater, container, false)
        bindingAll.rv.adapter = adapter



        return bindingAll.root
    }


}