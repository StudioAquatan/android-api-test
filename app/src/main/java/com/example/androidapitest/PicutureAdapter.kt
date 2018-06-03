package com.example.androidapitest

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.androidapitest.R.layout.grid_items
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.DateFormat
import java.util.*

class PicutureAdapter(layout: Int, names: MutableList<String>, dates: MutableList<Date>, pictures: MutableList<String>) : BaseAdapter(){

    val layout = layout
    val names: MutableList<String> = names
    val dates: MutableList<Date> = dates
    val pictures: MutableList<String> = pictures

    inner class ViewHolder(cell: View) {
        val date = cell.findViewById<TextView>(R.id.date)
        val name = cell.findViewById<TextView>(R.id.name)
        val picture = cell.findViewById<ImageView>(R.id.imageView)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            null-> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(R.layout.grid_items, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        viewHolder.date.text = android.text.format.DateFormat.format("yyyy/MM/dd", dates[position])
        viewHolder.name.text = names[position]
        Glide.with(viewHolder.picture).load(pictures[position]).into(viewHolder.picture)

        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return names.size
    }



}