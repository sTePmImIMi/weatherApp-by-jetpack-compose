package com.example.weatherbyjetpackcompose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.weatherbyjetpackcompose.ui.theme.BlueLight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun main_card() {

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
                        text = "20 Jun 2025 13:00",
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White,
                    )
                    AsyncImage(
                        model = "https://cdn.weatherapi.com/weather/64x64/day/302.png",
                        contentDescription = "im2",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )
                }
                Text(
                    text = "London",
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    text = "23°C",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = "Sunny",
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
                        text = "23°C/12°C",
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White
                    )

                    IconButton(
                        onClick =
                        {

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
fun TabLayout() {
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
        HorizontalPager(                                                                            //для возможности прокрутки
            count = titles.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f),
        )
        { index ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            )
            {
                items(15)
                {
                    WeatherListItem()
                }
            }
        }
    }
}

