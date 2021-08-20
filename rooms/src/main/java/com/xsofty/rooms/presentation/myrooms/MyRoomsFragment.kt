package com.xsofty.rooms.presentation.myrooms

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xsofty.shared.Result
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.RoomDetailsNavContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyRoomsFragment : Fragment(), CustomBackPressable {

    private val viewModel: MyRoomsViewModel by viewModels()

    @Inject
    lateinit var roomDetailsNavContract: RoomDetailsNavContract

    override fun onBackPressed() {
        requireActivity().finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DisplayMyRooms()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestMyRooms()
    }

    @Composable
    private fun DisplayMyRooms() {
        when (val rooms = viewModel.myRooms.value) {
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
                        TextButton(onClick = { navigateToRoomDetails(room.id) }) {
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

    private fun navigateToRoomDetails(roomId: String) {
        roomDetailsNavContract.show(roomId, findNavController())
    }
}