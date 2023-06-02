package com.example.app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.databinding.RamzanimageslistBinding

class RamzanAdapter(val clickListener:(RamzanAlbum)->Unit ):RecyclerView.Adapter<RamzanAdapter.RamzanImageViewHolder>() {
    lateinit var context:Context
    var ramzanImages= ArrayList<RamzanAlbum>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RamzanImageViewHolder {
        context=parent.context
        val binding=RamzanimageslistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RamzanImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ramzanImages.size

    }

    override fun onBindViewHolder(holder: RamzanImageViewHolder, position: Int) {
        val currentItem=ramzanImages[position]
        Glide.with(context).load(currentItem.url).into(holder.binding.Ramzanimg1)

        holder.binding.Ramzanimg1.setOnClickListener {
            clickListener.invoke(currentItem)
        }
    }



    fun getDetails(list: ArrayList<RamzanAlbum>)
    {
        ramzanImages.clear()
        ramzanImages.addAll(list)
        notifyDataSetChanged()
    }



    inner class RamzanImageViewHolder(val binding: RamzanimageslistBinding):RecyclerView.ViewHolder(binding.root)
}