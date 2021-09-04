package com.xsofty.rooms.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.rooms.R
import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.rooms.domain.model.entity.RoomDetailsEntity
import com.xsofty.rooms.domain.model.entity.RoomStatus
import com.xsofty.shared.Result
import com.xsofty.shared.compose.Loader
import com.xsofty.shared.compose.VerticalSpacer
import com.xsofty.shared.firebase.FirebaseStorageManager
import com.xsofty.shared.storage.AppPreferences
import com.xsofty.shared.theme.ColorType
import com.xsofty.shared.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomDetailsFragment : Fragment() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var themeManager: ThemeManager

    private val viewModel: RoomDetailsViewModel by viewModels()
    private val args: RoomDetailsFragmentArgs by navArgs()
    private val firebaseStorageManager = FirebaseStorageManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RoomDetailsView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestRoomDetails(args.roomId)
        viewModel.requestComments(args.roomId)
    }

    @Composable
    private fun RoomDetailsView() {
        when (val roomDetails = viewModel.roomDetails.value) {
            is Result.Success -> {
                RoomDetailsContent(roomDetails.data)
            }
            Result.Loading -> {
                Loader(
                    backgroundColor = themeManager.getColor(colorType = ColorType.BACKGROUND),
                    loaderColor = themeManager.getColor(colorType = ColorType.QUATERNARY)
                )
            }
            is Result.Error -> {
            }
        }
        when (viewModel.addCommentStatus.value) {
            is Result.Success -> {
                viewModel.requestComments(args.roomId)
            }
            else -> {
                // Could not add comment
            }
        }
    }

    @Composable
    private fun RoomDetailsContent(
        roomDetails: RoomDetailsEntity
    ) {
        Column(
            modifier = Modifier
                .background(color = themeManager.getColor(colorType = ColorType.BACKGROUND))
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VerticalSpacer()
            CategoryWithButton(roomDetails.category) { status ->
                if (status == RoomStatus.INACTIVE) {
                    // TODO
                } else {
                    viewModel.changeRoomStatus(roomDetails.id, status)
                }
            }
            RoomOverView(roomDetails.title, roomDetails.description)
            Text(
                text = stringResource(R.string.room_comments),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY)
            )

            when (val comments = viewModel.comments.value) {
                is Result.Success -> {
                    if (comments.data.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .background(
                                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = themeManager.getColor(colorType = ColorType.TERTIARY),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            for (comment in comments.data) {
                                CommentListItem(comment = comment)
                            }
                        }
                    }
                }
                is Result.Error -> {
                }
            }
            val isEnabled = when (val roomStatus = viewModel.roomStatus.value) {
                is Result.Success -> {
                    when (roomStatus.data) {
                        RoomStatus.CREATOR, RoomStatus.JOINED -> true
                        else -> false
                    }
                }
                else -> false
            }
            CommentTextField(isEnabled) {
                viewModel.addComment(
                    roomId = args.roomId,
                    text = it
                )
            }
            VerticalSpacer()
        }
    }

    @Composable
    private fun CategoryWithButton(
        category: CategoryEntity,
        onUserStatusButtonClicked: (RoomStatus) -> Unit
    ) {
        val categoryImageUrl: MutableState<String?> = remember { mutableStateOf(null) }
        firebaseStorageManager.imageIdToUrl(category.imageId) { fetchedImageUrl ->
            categoryImageUrl.value = fetchedImageUrl
        }

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .background(
                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .height(192.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(16.dp)
                    ),
                painter = rememberImagePainter(categoryImageUrl.value),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            UserStatusButton(onUserStatusButtonClicked)
        }
    }

    @Composable
    private fun UserStatusButton(
        onUserStatusButtonClicked: (RoomStatus) -> Unit
    ) {
        when (val newStatus = viewModel.roomStatus.value) {
            is Result.Success -> {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(128.dp)
                        .height(44.dp)
                        .background(
                            color = getUserButtonColor(newStatus.data),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            onUserStatusButtonClicked(newStatus.data)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = when (newStatus.data) {
                            RoomStatus.CREATOR -> stringResource(id = R.string.room_delete)
                            RoomStatus.JOINED -> stringResource(id = R.string.room_unjoin)
                            RoomStatus.NOT_JOINED -> stringResource(id = R.string.room_join)
                            RoomStatus.INACTIVE -> "Go Back"
                        },
                        color = Color.White
                    )
                }
            }
            is Result.Error -> {

            }
        }
    }

    @Composable
    private fun RoomOverView(
        title: String,
        description: String
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = themeManager.getColor(colorType = ColorType.SECONDARY),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = themeManager.getColor(colorType = ColorType.TERTIARY),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.room_description),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                    modifier = Modifier.padding(
                        top = 12.dp, bottom = 12.dp,
                        start = 12.dp, end = 12.dp
                    )
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = themeManager.getColor(colorType = ColorType.TEXT_SECONDARY),
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
    }

    @Composable
    private fun CommentTextField(
        isEnabled: Boolean,
        onCommentAdded: (String) -> Unit
    ) {

        val textFieldText = remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = themeManager.getColor(colorType = ColorType.TERTIARY),
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textFieldText.value,
                onValueChange = { textFieldText.value = it },
                modifier = Modifier
                    .weight(0.85f)
                    .padding(4.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                    backgroundColor = themeManager.getColor(colorType = ColorType.SECONDARY),
                    cursorColor = themeManager.getColor(colorType = ColorType.PRIMARY),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
            )
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .padding(start = 0.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (isEnabled) {
                            themeManager.getColor(colorType = ColorType.TERTIARY)
                        } else {
                            themeManager.getColor(colorType = ColorType.NATIVE_BUTTON_DISABLED)
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        if (isEnabled && textFieldText.value.isNotBlank()) {
                            onCommentAdded(textFieldText.value)
                            textFieldText.value = ""
                        }
                    },
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
    }

    @Composable
    private fun CommentListItem(comment: CommentEntity) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .background(
                    color = themeManager.getColor(colorType = ColorType.SECONDARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = themeManager.getColor(colorType = ColorType.TERTIARY),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = comment.userId,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = themeManager.getColor(colorType = ColorType.TEXT_SECONDARY),
                modifier = Modifier.padding(12.dp),
            )
            Text(
                text = comment.text,
                fontSize = 14.sp,
                color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                modifier = Modifier.padding(
                    top = 0.dp, bottom = 12.dp,
                    start = 12.dp, end = 12.dp
                )
            )
        }
    }

    private fun getUserButtonColor(roomStatus: RoomStatus): Color {
        return when (roomStatus) {
            RoomStatus.CREATOR -> Color(0xFFfe3c30)
            RoomStatus.JOINED -> Color(0xFF37c45b)
            else -> Color.LightGray.copy(alpha = 0.5f)
        }
    }
}