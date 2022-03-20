package com.example.bored

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyListAdapter(private val context: Activity, private val arrayList: ArrayList<String>)
  : ArrayAdapter<String>(context, R.layout.list_item, arrayList) {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val inflater: LayoutInflater = LayoutInflater.from(context)
    val view: View = inflater.inflate(R.layout.list_item, null)

    val titleView: TextView = view.findViewById(R.id.activity_title)


    titleView.text = arrayList[position]

    return view
  }
}