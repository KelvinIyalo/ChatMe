package com.example.chatme.module

import android.icu.text.SimpleDateFormat
import com.example.chatme.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.leftrow.view.*
import kotlinx.android.synthetic.main.rightrow.view.*

class ChatFromUser(val chatMessage: ChatMessage): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.Message_left.text = chatMessage.text
        time(viewHolder)
     //  Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.userImage_newMessage)
    }
    fun time(viewHolder: ViewHolder){
        val dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.itemView.MessageLeft_time.text = dateFormat.format(chatMessage.time)
    }

    override fun getLayout(): Int {
        return R.layout.leftrow
    }

}