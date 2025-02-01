package com.example.weatherbyjetpackcompose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherbyjetpackcompose.R
import com.example.weatherbyjetpackcompose.data.WeatherModel
import com.example.weatherbyjetpackcompose.ui.theme.BlueLight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun main_card(current_day: MutableState<WeatherModel>, onClickSync: () -> Unit, onClickSearch: () -> Unit) {

    Column(
        modifier = Modifier
            .padding(5.dp)
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.5.dp),
            colors = CardDefaults.cardColors(
                containerColor = BlueLight

            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(10.dp)
        )
        {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,

                )
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                )
                {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = current_day.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White,
                    )
                    AsyncImage(
                        model = "https:" + current_day.value.icon,
                        contentDescription = "im2",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )
                }
                Text(
                    text = current_day.value.city,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    text =
                    if (current_day.value.currentTemp.isNotEmpty())
                        current_day.value.currentTemp.toFloat().toInt().toString() + "℃"
                    else
                        "${current_day.value.maxTemp.toFloat().toInt()}℃/${current_day.value.minTemp.toFloat().toInt()}℃",

                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = current_day.value.condition,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    IconButton(
                        onClick =
                        {
                            onClickSearch.invoke()
                        },
                    )
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "im3",
                            tint = Color.White,
                        )
                    }
                    Text(
                        text = "${current_day.value.maxTemp.toFloat().toInt()}℃/${current_day.value.minTemp.toFloat().toInt()}℃",
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )

                    IconButton(
                        onClick =
                        {
                            onClickSync.invoke()
                        },
                    )
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.sync),
                            contentDescription = "im4",
                            tint = Color.White,
                        )
                    }
                }
            }


        }

    }
}

// ------------------------------------------------------

@OptIn(ExperimentalPagerApi::class)
@Composable
fun tab_layout(days_list: MutableState<List<WeatherModel>>, current_day: MutableState<WeatherModel>) {
    val pagerState = rememberPagerState()
    val titles = listOf("HOURS", "DAYS")
    val coroutineScope = rememberCoroutineScope()
    Column {
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            TabRow(
                containerColor = BlueLight,
                selectedTabIndex = pagerState.currentPage,
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        },
                    )
                }
            }

        }
        HorizontalPager(
            count = titles.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f),
        )
        { index ->
            val list = when(index)
            {
                0 -> getWeatherByHours(current_day.value.hours)
                1 -> days_list.value
                else -> days_list.value
            }
            mainList(list, current_day)
        }
    }
}

private fun getWeatherByHours(hours: String): List<WeatherModel>
{
    if (hours.isEmpty()) return listOf()

    val hours_array = JSONArray(hours)

    val list = ArrayList<WeatherModel>()
    for (i in 0 until hours_array.length())
    {
        val item = hours_array[i] as JSONObject
        list.add(WeatherModel(
            "",
            item.getString("time"),
            item.getString("temp_c").toFloat().toInt().toString() + "℃",
            item.getJSONObject("condition").getString("text"),
            item.getJSONObject("condition").getString("icon"),
            "",
            "",
             "",
        ))
    }
    return list
}
