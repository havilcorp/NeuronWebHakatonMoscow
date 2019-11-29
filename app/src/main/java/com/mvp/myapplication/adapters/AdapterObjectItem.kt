package com.mvp.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvp.myapplication.R

/**
 * Created by havil on 25.03.2018.
 */

open class AdapterObjectItem(val subcategory: List<String>, private val listener: (String) -> Unit) : RecyclerView.Adapter<AdapterObjectItem.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, listener: (String) -> Unit) = with(itemView) {
            //itemView.rest_item_name.text = item.name
            setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.adapter_object_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(subcategory[position], listener)

    override fun getItemCount() = subcategory.size

}
