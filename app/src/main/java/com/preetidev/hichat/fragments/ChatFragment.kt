package com.preetidev.hichat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.preetidev.hichat.R
import com.preetidev.hichat.databinding.FragmentChatBinding
import com.preetidev.hichat.databinding.FragmentHomeBinding


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the bottom navigation bar from the hosting activity
        val bottomNavigationBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set the visibility of the bottom navigation bar to View.GONE
        bottomNavigationBar.visibility = View.GONE

        fun onDestroyView() {
            super.onDestroyView()

            // Access the bottom navigation bar from the hosting activity
            val bottomNavigationBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            // Set the visibility of the bottom navigation bar to View.VISIBLE
            bottomNavigationBar.visibility = View.VISIBLE
        }
        binding.chatBackBtn.setOnClickListener {
            navigateToHomeFragment()
        }




    }
    private fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()

        // Replace the current fragment with HomeFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, homeFragment)
            .addToBackStack(null) // Add to back stack if you want to enable the back button to navigate back
            .commit()
        onDestroyView()
    }




}

