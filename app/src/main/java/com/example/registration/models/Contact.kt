package com.example.registration.models

class Contact {
    var id: Int? = null
    var name: String? = null
    var number: String? = null
    var country: String? = null
    var adress: String? = null
    var password: String? = null
    var image: ByteArray? = null


    constructor()
    constructor(
        id: Int?,
        name: String?,
        number: String?,
        country: String?,
        adress: String?,
        password: String?,
        image: ByteArray?
    ) {
        this.id = id
        this.name = name
        this.number = number
        this.country = country
        this.adress = adress
        this.password = password
        this.image = image
    }

    constructor(
        name: String?,
        number: String?,
        country: String?,
        adress: String?,
        password: String?,
        image: ByteArray?
    ) {
        this.name = name
        this.number = number
        this.country = country
        this.adress = adress
        this.password = password
        this.image = image
    }
}