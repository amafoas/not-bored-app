package com.example.bored

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val startBtn = findViewById<Button>(R.id.startBtn)
    val textInput = findViewById<EditText>(R.id.textInput)

    startBtn.setOnClickListener {
      val intent = Intent(this, Activities::class.java)
      intent.putExtra("participants", textInput.text.toString())
      startActivity(intent)
    }

    fun enableOrDisableStartBtn(newState: Boolean) {
      startBtn.isEnabled = newState
    }

    textInput.doOnTextChanged { text, _, _, _ ->
      val valid: Boolean = text?.matches("\\d+".toRegex()) ?: false
      val participants = if (valid) text.toString().toInt() else 0
      enableOrDisableStartBtn(valid && participants != 0)
    }

    val termsBtn = findViewById<TextView>(R.id.termsBtn)
    termsBtn.setOnClickListener {
      val intent: Intent = Intent(this, TermsAndConditions::class.java)
      startActivity(intent)
    }
  }
}