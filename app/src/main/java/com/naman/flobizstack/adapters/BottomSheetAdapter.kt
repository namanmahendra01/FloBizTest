package com.naman.flobizstack.adapters

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.text.format.DateFormat.format
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.naman.flobizstack.databinding.QuestionItemBinding
import com.naman.flobizstack.databinding.TagItemBinding
import com.naman.flobizstack.models.Item
import java.util.*
import java.text.SimpleDateFormat


class BottomSheetAdapter(var list :List<String>) : RecyclerView.Adapter<BottomSheetAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(val binding: TagItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        holder.binding.tagTv.text=list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position).hashCode().toLong()
    }

    private var onItemClickListener: ((Item) -> Unit)? = null
    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }

}
