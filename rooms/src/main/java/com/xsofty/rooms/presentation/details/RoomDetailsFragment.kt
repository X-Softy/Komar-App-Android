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
import com.xsofty.shared.firebase.FirebaseStorageManager
import com.xsofty.shared.storage.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomDetailsFragment : Fragment() {

    @Inject
    lateinit var appPreferences: AppPreferences

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
                Loader()
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
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            CategoryWithButton(roomDetails.category) { status ->
                if (status == RoomStatus.INACTIVE) {
                    // Go Back
                } else {
                    viewModel.changeRoomStatus(roomDetails.id, status)
                }
            }
            RoomOverView(roomDetails.title, roomDetails.description)
            Text(
                text = stringResource(R.string.room_comments),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            when (val comments = viewModel.comments.value) {
                is Result.Success -> {
                    if (comments.data.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = Color.DarkGray,
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
                    color = Color.Blue,
                    shape = RoundedCornerShape(16.dp)
                )
                .height(180.dp)
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
                        .padding(16.dp)
                        .width(120.dp)
                        .height(50.dp)
                        .background(
                            color = when (newStatus.data) {
                                RoomStatus.CREATOR -> Color.Red
                                RoomStatus.JOINED -> Color.LightGray.copy(alpha = 0.5f)
                                RoomStatus.NOT_JOINED -> Color.Green
                                RoomStatus.INACTIVE -> Color.LightGray //TODO
                            },
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
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.room_description),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(
                        top = 8.dp, bottom = 0.dp,
                        start = 8.dp, end = 8.dp
                    )
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(8.dp),
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
                .height(80.dp)
                .padding(vertical = 8.dp)
                .background(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textFieldText.value,
                onValueChange = { textFieldText.value = it },
                singleLine = true,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(2.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 0.dp, top = 2.dp, end = 2.dp, bottom = 2.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (isEnabled) Color.Green else Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onCommentAdded(textFieldText.value)
                        textFieldText.value = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chevron),
                    contentDescription = null,
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                )
            }
        }
    }

    @Composable
    private fun CommentListItem(comment: CommentEntity) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = comment.userId,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.padding(4.dp),
            )
            Text(
                text = comment.text,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(
                    top = 0.dp, bottom = 4.dp,
                    start = 4.dp, end = 4.dp
                )
            )
        }
    }
}