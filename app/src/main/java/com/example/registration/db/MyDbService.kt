package com.example.registration.db

import android.content.Context
import com.example.registration.models.Contact

interface MyDbService {

    fun add(contact: Contact)
    fun allContacts(): ArrayList<Contact>

}