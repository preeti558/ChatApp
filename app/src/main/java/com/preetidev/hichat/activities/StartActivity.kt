package com.preetidev.hichat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.preetidev.hichat.R
import com.preetidev.hichat.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize data binding
        binding= DataBindingUtil.setContentView(this, R.layout.activity_start)

        binding.startBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}