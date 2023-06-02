package com.example.app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.databinding.EidimageslistBinding

class EidAdapter(val clickListener: (EidAlbum)->Unit):RecyclerView.Adapter<EidAdapter.EidViewHolder>() {

    lateinit var context:Context
    var EidImages=ArrayList<EidAlbum>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EidViewHolder {
        context=parent.context
        val binding=EidimageslistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EidViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return EidImages.size
    }
    override fun onBindViewHolder(holder: EidViewHolder, position: Int) {
        val currentItem=EidImages[position]
        Glide.with(context).load(currentItem.url).into(holder.binding.Eidimg1)
        holder.binding.Eidimg1.setOnClickListener {
            clickListener.invoke(currentItem)
        }
    }
    fun getdetailsEid(list: List<EidAlbum>)
    {
        EidImages.clear()
        EidImages.addAll(list)
        notifyDataSetChanged()
    }
    inner class EidViewHolder(val binding:EidimageslistBinding):RecyclerView.ViewHolder(binding.root)
}