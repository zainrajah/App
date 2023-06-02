package com.example.app

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.databinding.IslamlistimagesBinding

class IslAdapter(val clickListener: (IslAlbum)->Unit):RecyclerView.Adapter<IslAdapter.IslViewHolder>() {
    lateinit var context:Context
    var islamImages=ArrayList<IslAlbum>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IslViewHolder {
        context=parent.context
        val binding=IslamlistimagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IslViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return islamImages.size
    }

    override fun onBindViewHolder(holder: IslViewHolder, position: Int) {
        val currentItem=islamImages[position]
        Glide.with(context).load(currentItem.url).into(holder.binding.islimg1)
        holder.binding.islimg1.setOnClickListener {
            clickListener.invoke(currentItem)
        }
    }
    fun getDetailsIsl(list: List<IslAlbum>)
    {
        islamImages.clear()
        islamImages.addAll(list)
        notifyDataSetChanged()
    }
    inner class IslViewHolder(val binding: IslamlistimagesBinding ):RecyclerView.ViewHolder(binding.root)
}