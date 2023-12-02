package com.preetidev.hichat.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.preetidev.hichat.Utils
import com.preetidev.hichat.modal.Users

class UsersRepo {

    private val firestore =FirebaseFirestore.getInstance()

    fun getUsers():LiveData<List<Users>> {
        val users=MutableLiveData<List<Users>>()
        firestore.collection("Users").addSnapshotListener{snapshot,
        exception->
            if (exception!=null){
                return@addSnapshotListener
            }
            val userList= mutableListOf<Users>()
            snapshot?.documents?.forEach{document->
                val user=document.toObject(Users::class.java)

                if(user!!.userid!=Utils.getUidLoggedIn()){
                    user.let {
                        userList.add(it)
                    }
                }
                users.value=userList

            }

        }
        return users
    }

}