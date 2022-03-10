package com.example.chatapplication.Models

class Status {

    var imageUrl : String? = null
    var timestamp : String? = null

    constructor(){}
    constructor(imageUrl: String?, timestamp: String?) {
        this.imageUrl = imageUrl
        this.timestamp = timestamp
    }

}