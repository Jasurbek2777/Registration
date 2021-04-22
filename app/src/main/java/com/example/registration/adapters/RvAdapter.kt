package com.example.registration.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.registration.databinding.ItemBinding
import com.example.registration.models.Contact

class RvAdapter(var list: ArrayList<Contact>, var itemCLick: setOnClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(contact: Contact, position: Int) {
            itemBinding.itemName.setText(contact.name)
            itemBinding.itemNumber.setText(contact.number)
            val image = contact.image
            var bitmap = image?.size?.let { BitmapFactory.decodeByteArray(image, 0, it) }
            itemBinding.itemImage.setImageBitmap(bitmap)

            itemBinding.itemMoreImage.setOnClickListener {
                itemCLick.itemOnCLick(contact, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
    interface setOnClick {
        fun itemOnCLick(contact: Contact, position: Int)
    }
}