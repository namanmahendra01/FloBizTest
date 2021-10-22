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
import com.naman.flobizstack.models.Item
import java.util.*
import java.text.SimpleDateFormat


class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(val binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        if(position==0||position%4==0){
//            for ads at every 4th index
            holder.binding.adIv.visibility= View.VISIBLE
            holder.binding.parentRl.visibility= View.GONE

        }
        else{
            holder.binding.adIv.visibility= View.GONE
            holder.binding.parentRl.visibility= View.VISIBLE


            val ques = differ.currentList[position]
            Log.d(TAG, "onBindViewHolder: "+ques)
            holder.binding.apply {

                Glide.with(root.context).load(ques.owner?.profile_image).into(profileIv)
                questionTv.text=ques.title
                username.text=ques.owner?.display_name

                val date: String = getDateTime(ques.creation_date.toString())
                dateTv.text="POSTED ON: ${date}"
                root.setOnClickListener {  onItemClickListener?.let {
                    it(ques)
                }}

            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Item) -> Unit)? = null
    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }
    private fun getDateTime(s: String): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}
