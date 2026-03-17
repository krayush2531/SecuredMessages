package com.mdtauhid.securedmessages.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mdtauhid.securedmessages.databinding.ItemMessageBinding
import com.mdtauhid.securedmessages.model.Message

class MessageAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = messages[position]

        holder.binding.senderText.text = message.sender
        holder.binding.bodyText.text = message.body
        holder.binding.categoryText.text = message.category.name
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}