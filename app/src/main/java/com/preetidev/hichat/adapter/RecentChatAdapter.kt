package com.preetidev.hichat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.preetidev.hichat.R
import com.preetidev.hichat.modal.RecentChats
import de.hdodenhof.circleimageview.CircleImageView
import com.bumptech.glide.Glide
import com.preetidev.hichat.modal.Users

class RecentChatAdapter(private val context: Context,  listOfChats: MutableList<RecentChats>) : RecyclerView.Adapter<RecentHolder>() {

    private var listOfChats = listOf<RecentChats>()
    private var listener: OnItemClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recentchatlist, parent, false)
        return RecentHolder(view)

    }

    override fun getItemCount(): Int {

        return listOfChats.size



    }

    override fun onBindViewHolder(holder: RecentHolder, position: Int) {

        val chat = listOfChats[position]

        val name = chat.username
        holder.profileName.setText(name)


        Glide.with(holder.itemView.context).load(chat.imageUrl).into(holder.imageProfile)

        holder.itemView.setOnClickListener {
            listener?.onChatSelected(position, chat)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RecentChats>){
        this.listOfChats = list
        notifyDataSetChanged()
    }

    fun setOnClickListener(){
        this.listener = listener
    }

 interface OnItemClickListener{
     fun onChatSelected(position: Int,chats: RecentChats)
 }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener=listener
    }


}

class RecentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val profileName: TextView = itemView.findViewById(R.id.recentChatTextName)
    val imageProfile : CircleImageView = itemView.findViewById(R.id.recentChatImageView)

    private var listOfChats = listOf<RecentChats>()
    private var listener: RecentChatAdapter.OnItemClickListener? = null

    init {
        itemView.setOnClickListener {
            listener?.onChatSelected(adapterPosition, listOfChats[adapterPosition])
        }
    }



}




