package com.xsofty.rooms.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xsofty.rooms.R
import com.xsofty.rooms.domain.model.entity.RoomEntity

@Composable
fun RoomListItem(
    room: RoomEntity,
    onRoomClicked: (RoomEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(
                color = Color.Blue,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onRoomClicked(room)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoomOverView(Modifier.weight(3f), room)
        RoomChevronButton(Modifier.weight(1f))
    }
}

@Composable
private fun RoomOverView(
    modifier: Modifier,
    room: RoomEntity
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxHeight()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = room.title,
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Text(
                text = room.description,
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun RoomChevronButton(modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(start = 0.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
            .fillMaxHeight()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chevron),
            contentDescription = null,
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
        )
    }
}