package com.xsofty.rooms.presentation.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xsofty.shared.Result
import com.xsofty.shared.ext.displayToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRoomFragment : Fragment() {

    private val viewModel: CreateRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CreateRoomView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestCategories()
    }

    @Composable
    private fun CreateRoomView() {
        var titleText by remember { mutableStateOf("") }
        var descriptionText by remember { mutableStateOf("") }
        var chosenCategoryId by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            TextField(
                value = titleText,
                onValueChange = { titleText = it },
                label = { Text(text = "Title") }
            )
            TextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text(text = "Description") }
            )

            when (val categories = viewModel.categories.value) {
                is Result.Success -> {
                    for (category in categories.data) {
                        TextButton(onClick = { chosenCategoryId = category.id }) {
                            Text(text = category.title)
                        }
                    }
                }
                is Result.Error -> {
                }
                Result.Loading -> {
                }
            }

            Button(
                onClick = {
                    viewModel.createRoom(
                        categoryId = chosenCategoryId,
                        title = titleText,
                        description = descriptionText
                    )
                },
                enabled = chosenCategoryId.isNotBlank() && titleText.isNotBlank() && descriptionText.isNotBlank()
            ) {
                Text(text = "Create Room")
            }

            // move to function
            when (viewModel.createRoomStatus.value) {
                is Result.Success -> displayToast("Room created Successfully!")
                is Result.Error -> displayToast("Error in room creation")
                Result.Loading -> {
                }
            }
        }
    }
}