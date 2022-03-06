package com.example.chatapplication.Models

import java.util.*

class Message {

//    lateinit var messageId : String
//    lateinit var senderId : String
//    lateinit var message : String
//    var feeling : Int = 0

    var message : String? = null
    var senderId : String? = null
    var messageId : String? = null
    var time : String? = null

    constructor(){}

    constructor(message: String?, senderId: String?, time : String?) /*timeStamp: String?*/ {
        this.message = message
        this.senderId = senderId
        this.time = time
//        this.timeStamp = timeStamp
    }
}