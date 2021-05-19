package com.example.chatme.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatme.messages.MessageBodyActivity
import com.example.chatme.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
signin_btn.setOnClickListener {
val email = signin_email.text.toString().trim()
    val password = sign_password.text.toString().trim()
    if(email.isEmpty() || password.isEmpty()){
        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show()

        return@setOnClickListener
    }
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
        .addOnCompleteListener(){
            if (it.isSuccessful)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MessageBodyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

            return@addOnCompleteListener
        }
        .addOnFailureListener(){
            Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
            signin_email.text.clear()
            sign_password.text.clear()
        }

}
    }
    fun Signup(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
