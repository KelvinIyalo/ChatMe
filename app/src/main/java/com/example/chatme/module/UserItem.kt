package com.example.chatme.module

import com.example.chatme.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.newusers.view.*

class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
      viewHolder.itemView.username_newMessage.text = user.username
       Picasso.get().load(user.profileImageUrl).placeholder(R.drawable.ic_profile_pic).into(viewHolder.itemView.userImage_newMessage)
    }

    override fun getLayout(): Int {
        return R.layout.newusers
    }

}