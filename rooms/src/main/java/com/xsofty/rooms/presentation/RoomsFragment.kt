package com.xsofty.rooms.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomsFragment : BaseFragment() {

    private val viewModel: RoomsViewModel by viewModels()

    private val args: RoomsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DisplayRooms()
            }
        }
    }

    @Composable
    private fun DisplayRooms() {
        when (val rooms = viewModel.rooms.value) {
            is Result.Success -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ) {
                    for (room in rooms.data) {
                        TextButton(onClick = {  }) {
                            Text(text = room.title)
                        }
                    }
                }
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }
}