package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ContactItemBinding
import com.google.android.material.motion.MotionUtils

class ContactsAdapter : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
        // Apply enter animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 50f
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(MotionUtils.resolveThemeDuration(
                holder.itemView.context,
                com.google.android.material.R.attr.motionDurationMedium2,
                300
            ).toLong())
            .setStartDelay((position * 50L))
            .start()
    }

    class ContactViewHolder(
        private val binding: ContactItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                contactNameTextView.text = contact.name
                phoneNumberTextView.text = contact.phoneNumber

                // Load contact photo or default icon
                Glide.with(contactImageView)
                    .load(contact.photoUri ?: R.drawable.ic_person)
                    .circleCrop()
                    .into(contactImageView)

                // Setup click listeners
                callButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${contact.phoneNumber}")
                    }
                    itemView.context.startActivity(intent)
                }

                messageButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:${contact.phoneNumber}")
                    }
                    itemView.context.startActivity(intent)
                }

                // Add ripple effect to the card
                root.setOnClickListener { }
            }
        }
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}