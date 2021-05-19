package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.chatme.messages.ChatActivity
import com.example.chatme.messages.NewMessageActivity
import com.example.chatme.module.ChatMessage
import com.example.chatme.module.User
import com.example.chatme.module.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_meesage_body.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
//var firebaseUser: ui? = null
    var refUser: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setTitle("Settings")

        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){

            val uri = user.photoUrl
            signedUser_profileUserName.text = user.displayName
        //    signedUser_profileUserImage"
            Picasso.get().load(uri).placeholder(R.drawable.ic_profile_pic).into(signedUser_profileUserImage)
        }


//fetchUsers()

    }

  fun  fetchUsers(){
        var firbaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().reference.child("/users").child(firbaseUserId)
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {

                    Log.d("SettingsActivity", it.toString())
                    it.toString()

                    if (p0.exists()){
                        val user = it.getValue(User::class.java)

                        signedUser_profileUserName.text = user!!.username

                    }

                }


            }


            override fun onCancelled(p0: DatabaseError) {
            }

        })
    }

//    private fun getUsers() {
//        var firbaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
//        val refUser = FirebaseDatabase.getInstance().getReference("/users").child(firbaseUserId)
//refUser!!.addValueEventListener(object: ValueEventListener{
//
//    override fun onCancelled(p0: DatabaseError) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onDataChange(p0: DataSnapshot) {
//      if (p0.exists()) {
//          val users = p0.getValue(User::class.java)
//          signedUser_profileUserName.text = users!!.username
//          Log.d("SettingsActivity", "${users.username}")
//          Picasso.get().load(users.profileImageUrl).placeholder(R.drawable.ic_profile_pic)
//              .into(signedUser_profileUserImage)
//      } }
//
//})
//
//
//
//
//
//    }

    fun  SignedUserProfile(view: View){
        val intent = Intent(this, SignedInUserProfile::class.java)
     startActivity(intent)
    }
}
