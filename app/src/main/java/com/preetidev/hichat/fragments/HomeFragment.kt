
package com.preetidev.hichat.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth
import com.preetidev.hichat.R
import com.preetidev.hichat.activities.SignInActivity
import com.preetidev.hichat.adapter.RecentChatAdapter
import com.preetidev.hichat.adapter.UserAdapter
import com.preetidev.hichat.databinding.FragmentHomeBinding
import com.preetidev.hichat.modal.RecentChats
import com.preetidev.hichat.modal.Users


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var rv: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var recentChatAdapter: RecentChatAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private var loggedInUserId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseAuth = FirebaseAuth.getInstance()
        binding.lifecycleOwner = viewLifecycleOwner

        //Toolbar
        binding.logOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
        }

        binding.tlImage.setOnClickListener {
            startActivity(Intent(requireContext(), SettingFragment::class.java))
        }
        // Initialize ProgressDialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Products")
        progressDialog.show()

        // Initialize RecyclerView and related components
        recyclerView = binding.rvUsers
        rv = binding.rvRecentChats

        recyclerView.setHasFixedSize(true)
        rv.setHasFixedSize(true)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = LinearLayoutManager(requireContext())

        userAdapter = UserAdapter(requireContext(), mutableListOf())
        recentChatAdapter = RecentChatAdapter(requireContext(), mutableListOf())

        recyclerView.adapter = userAdapter
        rv.adapter = recentChatAdapter


        // Initialize Firebase components
        db = FirebaseFirestore.getInstance()
        // Set up Firestore event listener
        eventChangeListener()

        //set the item click listener for RecentChatAdapter
        recentChatAdapter.setOnItemClickListener(object : RecentChatAdapter.OnItemClickListener {
            override fun onChatSelected(position: Int, chat: RecentChats) {
                // Handle item click, navigate to ChatFragment
                val chatFragment = ChatFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.flFragment, chatFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })


    }

    // Function to set up Firestore event listener
    private fun eventChangeListener() {
        db.collection("Users")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Log.e("No Internet Connection", error.message.toString())
                    return@addSnapshotListener
                }

                // Process document changes
                val usersList = mutableListOf<Users>()// Create a list to store Users objects

                for (dc in value?.documentChanges ?: emptyList()) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        // Add new product to the list
                        val user = dc.document.toObject(Users::class.java)
                        if (user.userid != loggedInUserId) {
                            // Exclude the logged-in user
                            usersList.add(user)
                        }

                    }
                }
                // Notify adapter about changes and dismiss ProgressDialog
                userAdapter.setList(usersList)
                progressDialog.dismiss()


            }
//        Similar logic for RecentChats collection to update recentChatAdapter

        db.collection("Users")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val recentChatsList = mutableListOf<RecentChats>()
                for (dc in value?.documentChanges ?: emptyList()) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val recentChat = dc.document.toObject(RecentChats::class.java)
                        if (recentChat.userid != loggedInUserId) {
                            // Exclude the logged-in user
                            recentChatsList.add(recentChat)
                        }
                    }

                    recentChatAdapter.setList(recentChatsList)
                }

            }


    }
}










