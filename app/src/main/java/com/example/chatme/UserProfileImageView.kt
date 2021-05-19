package com.example.chatme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatme.messages.ChatActivity
import com.example.chatme.module.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile_image_view.*

class UserProfileImageView : AppCompatActivity() {
    var toUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_image_view)

        val user = intent.getParcelableExtra<User>(ChatActivity.USER_KEY)
        supportActionBar?.setTitle(user.username)
        Picasso.get().load(user.profileImageUrl).into(userImageView_profile)
    }
}
