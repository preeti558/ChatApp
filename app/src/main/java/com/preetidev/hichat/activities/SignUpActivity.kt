@file:Suppress("DEPRECATION")
package com.preetidev.hichat.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.preetidev.hichat.R
import com.preetidev.hichat.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    ////Declare properties for user information ,Firebase authentication, FirebaseFirestore and progress dialog
    lateinit var binding:ActivitySignUpBinding
    lateinit var pd:ProgressDialog
    lateinit var auth:FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var name:String
    lateinit var email:String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize data binding
       binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        //Initialise Firebase authentication and FireStore
        auth=FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()

        //Initialize progress dialog
        pd= ProgressDialog(this)

        //set click listener for "SignIn" TextView to navigate to SignInActivity
        binding.signUpTextToSignIn.setOnClickListener{
           startActivity(Intent(this, SignInActivity::class.java))
        }

        //set click listener for "Sign Up" button
        binding.signupBtn.setOnClickListener {
            //Get user input for name ,email and password
            name=binding.signUpEtName.text.toString()
            email=binding.signUpEmail.text.toString()
            password=binding.signUpPassword.text.toString()

            //Check if name , email or password is empty, show again Toast if true
            if(binding.signUpEtName.text.isEmpty()){
                Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpEmail.text.isEmpty()){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpPassword.text.isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpEtName.text.isEmpty() && binding.signUpEmail.text.isEmpty() && binding.signUpPassword.text.isEmpty()){
                Toast.makeText(this,"Enter All given details",Toast.LENGTH_SHORT).show()
            }

            //If name ,email and password are not empty, proceed with account creation
            if(binding.signUpEtName.text.isNotEmpty() && binding.signUpEmail.text.isNotEmpty()
                && binding.signUpPassword.text.isNotEmpty()){
                createAnAccount(name,password,email)
            }
        }

    }
    //function to create a new user account
    private fun createAnAccount(name:String,password:String,email:String){
        pd.show()
        pd.setMessage("Registering User")

        //Attempt to create a user account with provided email and password
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if(task.isSuccessful){
                //if account creation successful, update Firestore with user data
                val user=auth.currentUser
                val dataHashMap= hashMapOf("userid" to user!!.uid!!,"username" to name,
                    "useremail" to email ,"status" to "default",
                    "imageUrl" to "https://www.pngarts.com/files/6/User-Avatar-in-Suit-PNG.png")

                firestore.collection("Users").document(user.uid).set(dataHashMap)
                pd.dismiss()
                startActivity(Intent(this, SignInActivity::class.java))

            }else {
                // If the user creation fails, log the error
                Log.e("SignUpActivity", "User creation failed: ${task.exception}")
                pd.dismiss()
                Toast.makeText(this, "User creation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}