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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xsofty.rooms.R
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.shared.theme.ColorType
import com.xsofty.shared.theme.ThemeManager

@Composable
fun RoomListItem(
    room: RoomEntity,
    themeManager: ThemeManager,
    onRoomClicked: (RoomEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(
                color = themeManager.getColor(colorType = ColorType.PRIMARY),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onRoomClicked(room)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoomOverView(Modifier.weight(4f), themeManager, room)
        RoomChevronButton(Modifier.weight(1f), themeManager)
    }
}

@Composable
private fun RoomOverView(
    modifier: Modifier,
    themeManager: ThemeManager,
    room: RoomEntity
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxHeight()
            .background(
                color = themeManager.getColor(colorType = ColorType.SECONDARY),
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
                color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Text(
                text = room.description,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = themeManager.getColor(colorType = ColorType.TEXT_SECONDARY),
                fontWeight = FontWeight.Normal,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun RoomChevronButton(
    modifier: Modifier,
    themeManager: ThemeManager
) {
    Box(
        modifier = modifier
            .padding(start = 0.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
            .fillMaxHeight()
            .background(
                color = themeManager.getColor(colorType = ColorType.TERTIARY),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chevron),
            contentDescription = null,
            modifier = Modifier
                .width(36.dp)
                .height(36.dp)
        )
    }
}