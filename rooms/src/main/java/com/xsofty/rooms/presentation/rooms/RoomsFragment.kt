package com.xsofty.rooms.presentation.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.presentation.compose.RoomListItem
import com.xsofty.shared.Result
import com.xsofty.shared.nav.contracts.RoomDetailsNavContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : Fragment() {

    private val viewModel: RoomsViewModel by viewModels()
    private val args: RoomsFragmentArgs by navArgs()

    @Inject
    lateinit var roomDetailsNavContract: RoomDetailsNavContract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RoomsView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestRooms(args.categoryId)
    }

    @Composable
    private fun RoomsView() {
        when (val rooms = viewModel.rooms.value) {
            is Result.Success -> {
                RoomsContent(rooms.data)
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }

    @Composable
    private fun RoomsContent(rooms: List<RoomEntity>) {
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