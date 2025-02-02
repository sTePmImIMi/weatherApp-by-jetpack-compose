package com.example.weatherbyjetpackcompose

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherbyjetpackcompose.data.WeatherModel
import com.example.weatherbyjetpackcompose.screens.DialogSearch
import com.example.weatherbyjetpackcompose.screens.main_card
import com.example.weatherbyjetpackcompose.screens.tab_layout
import com.example.weatherbyjetpackcompose.ui.theme.WeatherByJetpackComposeTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherByJetpackComposeTheme {

                val dialogState = remember {
                    mutableStateOf(false)
                }
                val days_list = remember {
                    mutableStateOf(listOf<WeatherModel>())
                }
                val current_day = remember {
                    mutableStateOf(
                        WeatherModel(
                            "",
                            "",
                            "0.0",
                            "",
                            "",
                            "0.0",
                            "0.0",
                            "",
                        )
                    )
                }

                if (dialogState.value) {
                    DialogSearch(dialogState, onSubmit = {
                        getData(it, this, days_list, current_day)
                    })
                }

                getData("London", this, days_list, current_day)

                Image(
                    painter = painterResource(R.drawable.weather_bg),
                    contentDescription = "im1",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    contentScale = ContentScale.FillBounds,

                    )

                Column {
                    main_card(current_day, onClickSync = {
                        getData("London", this@MainActivity, days_list, current_day)
                    },
                        onClickSearch = {
                            dialogState.value = true
                        })
                    tab_layout(days_list, current_day)
                }
            }
        }
    }
}

private fun getData(
    city: String,
    context: Context,
    days_list: MutableState<List<WeatherModel>>,
    current_day: MutableState<WeatherModel>
) {

    val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
            "5ad4ce260e334e2a9c2140735252701" +
            "&q=$city" +
            "&days=3" +
            "&aqi=no&alerts=no"

    val queue = Volley.newRequestQueue(context)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            Log.d("WeatherAPI", "Query: $response")
            val list = getWeatherByDays(response)
            current_day.value = list[0]
            days_list.value = list
        },
        {
            Log.d("dopdop", "VolleyError: $it")
        }
    )

    queue.add(request)
}

private fun getWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf()

    val list = ArrayList<WeatherModel>()

    val main_object = JSONObject(response)
    val city = main_object.getJSONObject("location").getString("name")
    val days = main_object.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )
    }
    list[0] = list[0].copy(
        time = main_object.getJSONObject("current").getString("last_updated"),
        currentTemp = main_object.getJSONObject("current").getString("temp_c"),
    )

    return list
}