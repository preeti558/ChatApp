package com.preetidev.hichat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.preetidev.hichat.databinding.ActivityMainBinding
import com.preetidev.hichat.fragments.HomeFragment
import com.preetidev.hichat.fragments.SettingFragment

class MainActivity : AppCompatActivity() {

    //declare the binding variable
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize the binding
       binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment=HomeFragment()
        val settingFragment= SettingFragment()

        setCurrentFragment(homeFragment)

       binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.peoples->setCurrentFragment(homeFragment)
                R.id.my_account->setCurrentFragment(settingFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
            commit()
    }
}

