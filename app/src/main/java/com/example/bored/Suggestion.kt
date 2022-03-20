package com.example.bored

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bored.api.APIService
import com.example.bored.api.BoredResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.ProgressDialog
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach


class Suggestion : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_suggestion)

    val participants = intent.getStringExtra("participants")
    val activityTitle = intent.getStringExtra("activity")

    // Checks for nullity
    if (participants == null || activityTitle == null) return

    searchByTypeAndParticipants(activityTitle.lowercase(), participants)

    val tryAnotherBtn = findViewById<Button>(R.id.button)
    tryAnotherBtn.setOnClickListener {
      searchByTypeAndParticipants(activityTitle.lowercase(), participants)
    }
  }


  private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://www.boredapi.com/api/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private fun searchByTypeAndParticipants(type:String, participants:String){
    showLoadingScreen()
    CoroutineScope(Dispatchers.IO).launch {
      val call = getRetrofit()
        .create(APIService::class.java)
        .getBoredCall("activity?type=$type&participants=$participants")

      val response: BoredResponse? = call.body()
      runOnUiThread {
        hideLoadingScreen()
        if(call.isSuccessful && response != null){
          updateView(response.type, response.activity, response.participants, response.price)
        } else showError()
      }
    }
  }

  private fun updateView(type: String?, activity: String?, participants: Int?, price: Float?) {
    if (type != null) findViewById<TextView>(R.id.activity_title).text = type
    if (participants != null) findViewById<TextView>(R.id.participantsValue).text = participants.toString()
    if (activity != null) findViewById<TextView>(R.id.activity_recomendation).text = activity
    if (price != null) {
//      println(price)
      val priceText: String =
        when {
          price > 0.6f -> "High"
          price > 0.3f -> "Medium"
          price > 0 -> "Low"
          else -> "Free"
        }
      findViewById<TextView>(R.id.moneyValue).text = priceText
    }
  }

  private fun showLoadingScreen() {
    val mainLayout = findViewById<ConstraintLayout>(R.id.complete_layaut)

    // Hide all in the layout
    mainLayout.forEach { child ->
      child.visibility = View.INVISIBLE
    }

    // Show loading icon
    val loadIcon = findViewById<ProgressBar>(R.id.load_icon)
    loadIcon.visibility = View.VISIBLE
  }

  private fun hideLoadingScreen() {
    val mainLayout = findViewById<ConstraintLayout>(R.id.complete_layaut)
    // Hide all in the layout
    mainLayout.forEach { child ->
      child.visibility = View.VISIBLE
    }

    // Show loading icon
    val loadIcon = findViewById<ProgressBar>(R.id.load_icon)
    loadIcon.visibility = View.GONE
  }

  private fun showError() {
    Toast.makeText(this, "Error al comunicarse con la API", Toast.LENGTH_SHORT).show()
  }
}