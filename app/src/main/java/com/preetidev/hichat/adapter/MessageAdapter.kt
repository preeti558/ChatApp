package com.preetidev.hichat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.preetidev.hichat.R
import com.preetidev.hichat.Utils
import com.preetidev.hichat.modal.Messages

class MessageAdapter(val context: Context,val messageList:ArrayList<Messages>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
        }else{
            val viewHolder=holder as ReceiveViewHolder
        }

    }

    class SentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         val sentMessage=itemView.findViewById<TextView>(R.id.sent)
    }

    class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.received)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}

