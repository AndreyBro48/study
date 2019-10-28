package ru.brovkin.volley.main

import InfoWeather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import ru.brovkin.volley.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.openweathermap.org/data/2.5/weather?q=Lipetsk&appid=80df23bf750f9f7d7ca2fa1acaffed83"
        var gson = Gson()
// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var infoWeather = gson.fromJson<InfoWeather>(response, InfoWeather::class.java)
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${infoWeather.main.temp - 273}"
            },
            Response.ErrorListener { textView.text = "That didn't work!" })
        stringRequest.headers
// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
