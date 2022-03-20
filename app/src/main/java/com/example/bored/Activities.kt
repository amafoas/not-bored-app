package com.example.bored

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.bored.databinding.ActivityActivitiesBinding

class Activities : AppCompatActivity() {
  private lateinit var binding: ActivityActivitiesBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityActivitiesBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val participants = intent.getStringExtra("participants")
    val options: ArrayList<String> = resources.getStringArray(R.array.categories).toList() as ArrayList<String>

    val listView: ListView = findViewById(R.id.category_list)
    listView.adapter = MyListAdapter(this, options)

    listView.setOnItemClickListener() { _, _, position, _ ->
      val act = options[position]
      val intent: Intent = Intent(this, Suggestion::class.java)
      intent.putExtra("participants", participants)
      intent.putExtra("activity", act)
      startActivity(intent)
    }
  }
}