package com.example.chatme.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.chatme.R
import com.example.chatme.module.User
import com.example.chatme.module.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.setTitle("New User")
       fetchUsers()

    }
    companion object{
        val USER_KEY = "USER_KEY"
    }
    private fun fetchUsers(){
        var firbaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
Log.d("NewMessageActivity", it.toString())
                    it.toString()
                    val user = it.getValue(User::class.java)
                    val fromId = FirebaseAuth.getInstance().uid
                    if (!(user!!.uid).equals(firbaseUserId)){
                        adapter.add(UserItem(user))
                        progressBar.visibility = View.GONE

                   }

                }
                adapter.setOnItemClickListener { item, view ->
                 val userItem = item as UserItem
                 val intent = Intent(view.context, ChatActivity::class.java )
                   intent.putExtra(USER_KEY, userItem.user)
                   startActivity(intent)
                  finish() }
                newMessage_recyclerView.adapter = adapter

            }


            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.search_newUser ->{
               // Toast.makeText(this, "This is Experiemental",  Toast.LENGTH_SHORT).show()
            }
         }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_icon_newuser, menu)
        return super.onCreateOptionsMenu(menu)
    }

}

