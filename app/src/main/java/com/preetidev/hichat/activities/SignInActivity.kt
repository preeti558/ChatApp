@file:Suppress("DEPRECATION")
package com.preetidev.hichat.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.preetidev.hichat.MainActivity
import com.preetidev.hichat.R
import com.preetidev.hichat.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    //Declare properties for user information ,Firebase authentication, and progress dialog
    lateinit var name:String
    lateinit var email:String
    lateinit var password :String
    private lateinit var fbauth: FirebaseAuth
    private lateinit var pds : ProgressDialog
   //Declare a property for data binding
    lateinit var binding:ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize data binding
       binding=DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        //Initialize firebase Authentication
       fbauth =FirebaseAuth.getInstance()

        //check if a user is already signed in ,If yes , redirect to MainActivity
        if(fbauth.currentUser!=null){

           binding.signinBtn.setOnClickListener {
               startActivity(Intent(this,MainActivity::class.java))
           }

        }

        //Initialize progress Dialog
        pds= ProgressDialog(this)

        //Set click Listener fo SignUp TextView to navigate to SignUpActivity
       binding.signInTextToSignUp.setOnClickListener {
           startActivity(Intent(this,SignUpActivity::class.java))
       }

        //set click listener for Sign In button
        binding.signinBtn.setOnClickListener {
            //Get User Input for email and password
            email=binding.loginetemail.text.toString()
            password=binding.loginetpassword.text.toString()

            //check if email or password is empty, show a Toast if true
            if(binding.loginetemail.text.isEmpty()){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()
            }
            if(binding.loginetpassword.text.isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show()
            }

            //If email and password are not empty , Proceed with sign-in
            if(binding.loginetemail.text.isNotEmpty() && binding.loginetpassword.text.isNotEmpty()){
                signIn(password,email)
            }

        }
    }

    //Function to handle user sign-in
    private fun signIn(password:String,email:String){
        pds.show()
        pds.setMessage("Signing In")

        //Attempt to sign in with provided email and password
        fbauth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                //Dismiss progress dialog on successful ign-in
                pds.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                //Dismiss progress dialog and show a toast on sign-in failure
                pds.dismiss()
                Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_SHORT).show()

            }
        }.addOnFailureListener { exception->
            //Handle specific sign-in failure scenarios
            when (exception){
                is FirebaseAuthInvalidCredentialsException->{
                    Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()

                }
                else->{
                    Toast.makeText(applicationContext,"Auth Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Override onBackPressed to dismiss progress dialog and finish activity
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        pds.dismiss()
        finish()
    }

    //Override onDestroy to dismiss progress dialog when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        pds.dismiss()
    }
}