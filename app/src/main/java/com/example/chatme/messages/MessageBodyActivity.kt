package com.example.chatme.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatme.R
import com.example.chatme.SettingsActivity
import com.example.chatme.module.*
import com.example.chatme.registration.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_meesage_body.*

class MessageBodyActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    val LatestMessageMap = HashMap<String, ChatMessage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meesage_body)
        supportActionBar?.setTitle("Message Body")
       Log.d("MessageBodyActivity", "it got to this point")
        newmessage()

        ListenForLatestMessages()
        MessageBody_RecyclerView.adapter = adapter
        MessageBody_RecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
           // val userItem = item as UserItem
            val intent = Intent(this, ChatActivity::class.java )
            val row = item as MessageBodyRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }


    }
   fun RefreshRecylerView(){
       adapter.clear()
       LatestMessageMap.values.forEach {
           adapter.add(MessageBodyRow(it))
       }

   }

    fun ListenForLatestMessages(){

        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                LatestMessageMap[p0.key!!] = chatMessage
                RefreshRecylerView()
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                LatestMessageMap[p0.key!!] = chatMessage
                RefreshRecylerView()
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }




            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }






    private fun newmessage(){
        newmessage_btn.setOnClickListener(){
            val intent = Intent (this, NewMessageActivity::class.java)
            startActivity(intent) } }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent (this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "LogOut Successfull",  Toast.LENGTH_SHORT).show()

            }
            R.id.settings_id ->{
                val intent = Intent (this, SettingsActivity::class.java)
                // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent) } }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_icon, menu)
        return super.onCreateOptionsMenu(menu)
    }



}
