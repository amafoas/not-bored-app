package com.example.bored

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bored.api.APIService
import com.example.bored.api.BoredResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.view.View
import android.widget.*
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
    showLoadingScreen(true)
    CoroutineScope(Dispatchers.IO).launch {
      val call = getRetrofit()
        .create(APIService::class.java)
        .getBoredCall("activity?type=$type&participants=$participants")

      val response: BoredResponse? = call.body()
      runOnUiThread {
        showLoadingScreen(false)
        if(call.isSuccessful && response != null){
          if(response.type == null) {
            // no activity found with the specified parameters
            showActivityNotFound()
          } else {
            updateView(response.type, response.activity, response.participants, response.price)
          }
        } else showError()
      }
    }
  }

  private fun updateView(type: String?, activity: String?, participants: Int?, price: Float?) {
    if (type != null) findViewById<TextView>(R.id.activity_title).text = type
    if (participants != null) findViewById<TextView>(R.id.participantsValue).text = participants.toString()
    if (activity != null) findViewById<TextView>(R.id.activity_recomendation).text = activity
    if (price != null) {
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

  private fun showActivityNotFound() {
    val loadingScreen = findViewById<LinearLayout>(R.id.activity_loading_screen)
    val completeLayout = findViewById<ConstraintLayout>(R.id.complete_layaut)
    val notFound = findViewById<LinearLayout>(R.id.activity_not_found_layout)
    val goBackButton = findViewById<Button>(R.id.goBack_btn)

    /// hide the other views
    loadingScreen.visibility = View.GONE
    completeLayout.visibility = View.GONE

    /// shows the not found screen
    notFound.visibility = View.VISIBLE

    /// set the button to finish the activity
    goBackButton.setOnClickListener { finish() }
  }

  private fun showLoadingScreen(show: Boolean) {
    val completeLayout = findViewById<ConstraintLayout>(R.id.complete_layaut)
    val loadingScreen = findViewById<LinearLayout>(R.id.activity_loading_screen)

    if (show) {
      completeLayout.visibility = View.GONE
      loadingScreen.visibility = View.VISIBLE
    } else {
      completeLayout.visibility = View.VISIBLE
      loadingScreen.visibility = View.GONE
    }
  }

  /// display an error if unable to connect to api
  private fun showError() {
    Toast.makeText(this, "Error al comunicarse con la API", Toast.LENGTH_SHORT).show()
  }
}