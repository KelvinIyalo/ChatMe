package com.example.chatme.module

import com.example.chatme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_ows.view.*

class MessageBodyRow(val chatMessage: ChatMessage): Item<ViewHolder>(){
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
     viewHolder.itemView.messageBody_lastMessage.text = chatMessage.text
      viewHolder.itemView.messageBody_time.text = chatMessage.time.toString()

        val chatPatnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPatnerId = chatMessage.toId
        }else{
            chatPatnerId = chatMessage.fromId
        }
            val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPatnerId")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(User::class.java)
                    val adapter = GroupAdapter<ViewHolder>()
                    viewHolder.itemView.messageBody_userName.text = chatPartnerUser?.username
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).placeholder(R.drawable.ic_profile_pic).into(viewHolder.itemView.messageBody_ImageView)

                }


                override fun onCancelled(p0: DatabaseError) {
                }

            })


    }

    override fun getLayout(): Int {
        return R.layout.message_ows
    }

}