package com.xsofty.rooms.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.xsofty.rooms.domain.model.entity.CommentEntity
import com.xsofty.shared.Result
import com.xsofty.shared.base.NestedFragment
import com.xsofty.shared.ext.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomDetailsFragment : NestedFragment() {

    private val viewModel: RoomDetailsViewModel by viewModels()
    private val args: RoomDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DisplayRoomDetails()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestRoomDetails(args.roomId)
    }

    @Composable
    private fun DisplayRoomDetails() {
        when (val roomDetails = viewModel.roomDetails.value) {
            is Result.Success -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DisplayTexts(
                            category = roomDetails.data.categoryId,
                            title = roomDetails.data.title,
                            description = roomDetails.data.description
                        )
                        DisplayButtons()
                    }
                    DisplayComments(roomDetails.data.comments)
                    DisplayAddComment()
                }
                // move to function
                when (viewModel.deleteRoomStatus.value) {
                    is Result.Success -> displayToast("Room deleted successfully!")
                    is Result.Error -> displayToast("Error in deletion")
                    is Result.Loading -> {
                    }
                }
                when (viewModel.joinRoomStatus.value) {
                    is Result.Success -> displayToast("Room join successfully!")
                    is Result.Error -> displayToast("Error in joining")
                    is Result.Loading -> {
                    }
                }
                when (viewModel.unjoinRoomStatus.value) {
                    is Result.Success -> displayToast("Room unjoined successfully!")
                    is Result.Error -> displayToast("Error in unjoining")
                    is Result.Loading -> {
                    }
                }
                when (viewModel.addCommentStatus.value) {
                    is Result.Success -> displayToast("Comment added successfully!")
                    is Result.Error -> displayToast("Error in adding comment")
                    is Result.Loading -> {
                    }
                }
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }

    @Composable
    private fun DisplayTexts(
        category: String,
        title: String,
        description: String
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = "Category: $category")
            Text(text = "Title: $title")
            Text(text = "Description: $description")
        }
    }

    @Composable
    private fun DisplayButtons() {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
        ) {
            Button(onClick = { viewModel.joinRoom(args.roomId) }) {
                Text(text = "Join")
            }
            Button(onClick = { viewModel.unjoinRoom(args.roomId) }) {
                Text(text = "Unjoin")
            }
            Button(onClick = { viewModel.deleteRoom(args.roomId) }) {
                Text(text = "Delete")
            }
        }
    }

    @Composable
    private fun DisplayComments(comments: List<CommentEntity>) {
        for (comment in comments) {
            Text(text = comment.text)
        }
    }

    @Composable
    private fun DisplayAddComment() {
        var newCommentText by remember { mutableStateOf("") }
        TextField(
            value = newCommentText,
            onValueChange = { newCommentText = it },
            label = { Text(text = "New Comment") }
        )
        Button(onClick = { viewModel.addComment(args.roomId, newCommentText) }) {
            Text(text = "Add Comment")
        }
    }
}