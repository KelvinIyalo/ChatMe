package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import com.example.chatme.messages.ChatActivity
import com.example.chatme.messages.MessageBodyActivity
import com.example.chatme.messages.NewMessageActivity
import com.example.chatme.module.User
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfileActivity : AppCompatActivity() {
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar_profile)
       // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = intent.getParcelableExtra<User>(ChatActivity.USER_KEY)
       supportActionBar?.setTitle(user.username)
        Picasso.get().load(user.profileImageUrl).into(toolbar_profileImage)

    }
    companion object{
        val USER_KEY = "USER_KEY"
    }
    fun UserProfileImageView(view: View){
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val userItem =  user
        val intent = Intent(this, UserProfileImageView::class.java)
        intent.putExtra(ChatActivity.USER_KEY, userItem)
        startActivity(intent)

    }

    fun UserProfile_back1(view: View) {
      // val intent = Intent(this, ChatActivity::class.java)
      // startActivity(intent)
      // finish()
    }

}
