package com.example.chatme.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chatme.messages.MessageBodyActivity
import com.example.chatme.messages.NewMessageActivity
import com.example.chatme.R
import com.example.chatme.module.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

performregister()
selectphoto_btn.setOnClickListener {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type="image/*"
    startActivityForResult(intent,0)
}

    }
    private fun performregister(){
        register_btn.setOnClickListener {
            val emailRegister = register_email.text.toString().trim()
            val passwordRegister = register_password.text.toString().trim()
            val NameRegister = register_name.text.toString().trim()
            if(emailRegister.isEmpty() || passwordRegister.isEmpty() ||NameRegister.isEmpty() ){
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegister, passwordRegister)
                .addOnFailureListener{
                    // Toast.makeText(this, "Failed to Create user ${it.message}", Toast.LENGTH_LONG).show()
                    AlertDialog.Builder(this).setMessage("${NameRegister} User Failed to Create  ${it.message}")
                        .setPositiveButton("OK"){
                                p0,p1 ->
                            register_email.text.clear()
                            register_name.text.clear()
                            register_password.text.clear()
                        }.create().show()
                    return@addOnFailureListener
                }
                .addOnCompleteListener{
                    if (!it.isSuccessful)return@addOnCompleteListener
                    AlertDialog.Builder(this).setMessage("${NameRegister} By cliking Accept means you agree to our Terms and conditions carefully before proceeding")
                        .setPositiveButton("Agree"){
                                p0,p1 ->
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MessageBodyActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            uploadImageToFirebaseStorage()



                        }.setNegativeButton("Decline"){
                                p0,p1 ->
                            register_email.text.clear()
                            register_name.text.clear()
                            register_password.text.clear()
                        }.create().show() }

        }
    }

    var selectedPhotoUri: Uri?  = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphoto_btn.alpha=0f
            // val BitmapDrawable = BitmapDrawable(bitmap)
            //btnSelect_Photo.setBackgroundDrawable(BitmapDrawable)
            selectphoto_img.setImageBitmap(bitmap)

        }
    }
    //Uploading Image to FireBase Data Base
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully Uploaded Imaege: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "file Location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener(){

            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, register_name.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Data Uploaded To the Database")
                val intent = Intent(this, NewMessageActivity:: class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener(){
                Toast.makeText(this, "Failed to Create Database: ${it.message}",  Toast.LENGTH_SHORT).show()

            }
    }




    fun Signin(view: View){
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
}
