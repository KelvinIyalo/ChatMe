package com.example.chatme.module

import java.util.*

class ChatMessage(val id: String, val text: String, val toId:String, val fromId: String, val time:Date){
    constructor():this("", "","", "", Date(0))
}
