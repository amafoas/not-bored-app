package com.example.bored

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TermsAndConditions : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_terms_and_conditions)

    val exitBtn = findViewById<Button>(R.id.exitBtn);
    exitBtn.setOnClickListener {
      finish()
    }
  }
}