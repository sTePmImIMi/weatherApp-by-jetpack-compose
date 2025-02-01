package com.example.weatherbyjetpackcompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherbyjetpackcompose.data.WeatherModel
import com.example.weatherbyjetpackcompose.ui.theme.BlueLight

@Composable
fun mainList(list: List<WeatherModel>, current_day: MutableState<WeatherModel>)
{
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(list)

        { _, item ->
            weatherListItem(item, current_day)

        }
    }
}

@Composable
fun weatherListItem(item: WeatherModel, current_day: MutableState<WeatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp)
            .clickable {
                current_day.value = item
            },
        colors = CardDefaults.cardColors(
            containerColor = BlueLight
        ),
        elevation = CardDefaults.cardElevation( //тень
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(5.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.padding(start = 8.dp, top = 5.dp, bottom = 5.dp))
            {
                Text(
                    text = item.time,
                    color = Color.White
                )
                Text(
                    text = item.condition,
                    color = Color.White
                )

            }
            Text(
                text = item.currentTemp.ifEmpty { "${item.maxTemp}/${item.minTemp}" },
                color = Color.White,
                style = TextStyle(fontSize = 25.sp),
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "im5",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(35.dp)
            )
        }
    }
}