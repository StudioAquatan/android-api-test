package com.example.androidapitest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.DateFormat

class PicutureAdapter(data: OrderedRealmCollection<Picture>?) : RealmBaseAdapter<Picture>(data) {

    inner class ViewHolder(cell: View) {
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val name = cell.findViewById<TextView>(android.R.id.text2)
        val picture = cell.findViewById<ImageView>(android.R.id.background)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            null-> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run {
            val picture = get(position)
            viewHolder.date.text = android.text.format.DateFormat.format("yyyy/MM/dd", picture.date)
            viewHolder.name.text = picture.name
        }

        return view

    }

}