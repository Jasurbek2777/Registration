package com.example.registration.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.registration.models.Contact

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1), MyDbService {
    companion object {
        const val DB_NAME = "contact_db"
        const val TABLE_NAME = "contact_table"
        const val ID = "id"
        const val NAME = "name"
        const val NUMBER = "number"
        const val COUNTRY = "country"
        const val ADRESS = "adress"
        const val PASSWORD = "password"
        const val IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table $TABLE_NAME(id integer not null primary key autoincrement,name text not null " +
                    ",number text not null,country text not null,adress text not null,password text not null,image blob not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun add(contact: Contact) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, contact.name)
        contentValues.put(NUMBER, contact.number)
        contentValues.put(COUNTRY, contact.country)
        contentValues.put(ADRESS, contact.adress)
        contentValues.put(PASSWORD, contact.password)
        contentValues.put(IMAGE, contact.image)
        database.insert(TABLE_NAME, null, contentValues)
        database.close()
    }

    override fun allContacts(): ArrayList<Contact> {
        val database = this.readableDatabase
        val list = ArrayList<Contact>()
        val cursor = database.rawQuery("select *from $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)
                val country = cursor.getString(3)
                val adress = cursor.getString(4)
                val pass = cursor.getString(5)
                val image = cursor.getBlob(6)
                list.add(Contact(id, name, number, country, adress, pass, image))
            } while (cursor.moveToNext())
        }
        return list
    }
}