package com.example.chatme.messages

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.chatme.R
import com.example.chatme.UserProfileActivity
import com.example.chatme.module.*
import com.example.chatme.registration.LogInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_meesage_body.*
import kotlinx.android.synthetic.main.newusers.view.*
import java.util.*

class ChatActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(toolbar)



     val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
    //supportActionBar?.setTitle(user.username)
       // supportActionBar?.setIcon(R.drawable.ic_applogo)
        chat_userName.text = user.username
        Picasso.get().load(user.profileImageUrl).placeholder(R.drawable.ic_profile_pic).into(chat_userImage)
        chatRecyclerView.adapter = adapter
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
     pick()
        val chatText = etMessage_chat.text.toString()


        etMessage_chat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                    sendChat_btn.setImageResource(R.drawable.ic_send)
                

               }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })

        sendChat_btn.setOnClickListener {

            Log.d("ChatActivity", "Message sending")
            PeformMessage()
        }


ListenForMessages()


    }
    companion object{
        val USER_KEY = "USER_KEY"
    }

   fun ListenForMessages(){

       val toId = toUser?.uid
       val fromId = FirebaseAuth.getInstance().uid
       val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
       ref.addChildEventListener(object: ChildEventListener {
           override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val chatMessage = p0.getValue(ChatMessage::class.java)

if (chatMessage != null){
    Log.d("ChatActivity", chatMessage.text)
    if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
        adapter.add(ChatFromMe(chatMessage))

    }else{
        adapter.add(ChatFromUser(chatMessage))
    }


}


//MessageBody_RecyclerView.scrollToPosition(adapter.itemCount -1)

           }

           override fun onCancelled(p0: DatabaseError) {
           }

           override fun onChildMoved(p0: DataSnapshot, p1: String?) {
           }

           override fun onChildChanged(p0: DataSnapshot, p1: String?) {
           }


           override fun onChildRemoved(p0: DataSnapshot) {
           }
       })


    }
    fun UserProfile(view: View){
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val userItem =  user
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(USER_KEY, userItem)
        startActivity(intent)

    }

    fun PeformMessage(){


        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid




        if (fromId == null) return
        val text = etMessage_chat.text.toString().trim()
        val FromReference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val ToReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage = ChatMessage(FromReference.key!!, text, toId, fromId, Calendar.getInstance().time)
        val time = System.currentTimeMillis()/1000
        val FromLatestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val ToLatestMessageReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        FromReference.setValue(chatMessage)
        chatRecyclerView.scrollToPosition(adapter.itemCount -1)
        FromLatestMessageReference.setValue(chatMessage)
        ToLatestMessageReference.setValue(chatMessage)
            .addOnSuccessListener {
                val mPlayer = MediaPlayer.create(this,
                    R.raw.insight
                )
                sendChat_btn.setImageResource(R.drawable.ic_mic)
                mPlayer.start()
                Log.d("ChatActivity", "Message sent: ${FromReference.key}")
            }

        ToReference.setValue(chatMessage)
        etMessage_chat.text.clear()

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.video_call ->{
                Toast.makeText(this, "This is Experiemental",  Toast.LENGTH_SHORT).show()
            }
            R.id.audio_call ->{
                Toast.makeText(this, "This is Experiemental",  Toast.LENGTH_SHORT).show()
                } }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_icon, menu)
        return super.onCreateOptionsMenu(menu)
    }
    fun pick(){
        image_attachment.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="*/*"
            startActivityForResult(intent,1)
        }
    }
    fun camera(){
        image_camera.setOnClickListener {
            val intent = Intent(Intent.ACTION_CAMERA_BUTTON)
            intent.type="*/*"
            startActivityForResult(intent,2)
        }
    }

    fun UserProfile_back(view: View) {
        val intent = Intent(this, MessageBodyActivity::class.java)
        startActivity(intent)
        finish()
    }

}
