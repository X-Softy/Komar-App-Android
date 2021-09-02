package com.xsofty.rooms.presentation.myrooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.presentation.compose.RoomListItem
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
                MyRoomsView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestMyRooms()
    }

    @Composable
    private fun MyRoomsView() {
        when (val rooms = viewModel.myRooms.value) {
            is Result.Success -> {
                MyRoomsContent(rooms.data)
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }

    @Composable
    private fun MyRoomsContent(rooms: List<RoomEntity>) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(rooms) {
                RoomListItem(room = it) { room ->
                    navigateToRoomDetails(room.id)
                }
            }
        }
    }

    private fun navigateToRoomDetails(roomId: String) {
        roomDetailsNavContract.show(roomId, findNavController())
    }
}