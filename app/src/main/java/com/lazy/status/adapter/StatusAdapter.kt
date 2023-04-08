package com.lazy.status.adapter

import android.content.Context
import android.net.Uri
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.belazy.lazystatus.R
import com.lazy.status.models.modelclasss

class StatusAdapter(private val context: Context,private var modelClass:ArrayList<modelclasss>,private var check:Boolean ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onitemclick:((modelclasss)->Unit)?=null
    var onshareclick:((modelclasss)->Unit)?=null
        class StatusViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
        {
            val ivstatus:ImageView=itemView.findViewById(R.id.status)
            val ivvideoicon:ImageView=itemView.findViewById(R.id.iv_video_icon)
            val cvvideocard:CardView=itemView.findViewById(R.id.cv_video_card)

        }
    class  imageviewholder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val viewimages:ImageView=itemView.findViewById(R.id.imageview)
        val save:ImageButton=itemView.findViewById(R.id.save)
        val share:ImageButton=itemView.findViewById(R.id.share)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (check){
            StatusViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.status_item,parent,false)
        )
        }else{
            imageviewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.viewimage,parent,false)
            )
        }
    }

    override fun getItemCount(): Int {
        return  modelClass.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (check) {
            if (modelClass[position].fileUri!!.endsWith(".mp4")) {
                (holder as StatusViewHolder).cvvideocard.visibility = View.VISIBLE
                holder.ivvideoicon.visibility = View.VISIBLE
            } else {
                (holder as StatusViewHolder).cvvideocard.visibility = View.GONE
                holder.ivvideoicon.visibility = View.GONE
            }
            Glide.with(context).load(Uri.parse(modelClass[position].fileUri)).into(holder.ivstatus)
            holder.itemView.setOnClickListener {
                onitemclick?.invoke(modelClass[position])
            }
        }
        else{
            Glide.with(context).load(Uri.parse(modelClass[position].fileUri))
                .into((holder as imageviewholder).viewimages)
            holder.save.setOnClickListener{
                onitemclick?.invoke(modelClass[position])
            }
            holder.share.setOnClickListener {
                onshareclick?.invoke(modelClass[position])
            }
        }
    }
}