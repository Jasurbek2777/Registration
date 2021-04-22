package com.example.registration

import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.registration.databinding.FragmentSmsBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var bindingSMS: FragmentSmsBinding

class SmsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        bindingSMS = FragmentSmsBinding.inflate(inflater, container, false)
        bindingSMS.name.setText(param1)
        bindingSMS.number.setText(param2)
        bindingSMS.send.setOnClickListener {
            val text = bindingSMS.text.text.toString()

            if (text.isNotEmpty()) {
                var smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    param2.toString(),
                    null,
                    text,
                    null,
                    null
                )
                Toast.makeText(requireContext(), "Habar jo'natildi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
        return bindingSMS.root
    }


}