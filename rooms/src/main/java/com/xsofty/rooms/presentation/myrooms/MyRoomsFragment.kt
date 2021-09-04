package com.xsofty.rooms.presentation.myrooms

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xsofty.rooms.domain.model.entity.RoomEntity
import com.xsofty.rooms.presentation.compose.RoomListItem
import com.xsofty.shared.Result
import com.xsofty.shared.compose.Loader
import com.xsofty.shared.compose.NavBarSpacer
import com.xsofty.shared.compose.VerticalSpacer
import com.xsofty.shared.ext.displayToast
import com.xsofty.shared.nav.AuthResultListener
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.CreateRoomNavContract
import com.xsofty.shared.nav.contracts.RoomDetailsNavContract
import com.xsofty.shared.theme.ColorType
import com.xsofty.shared.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyRoomsFragment : Fragment(), CustomBackPressable {

    private val viewModel: MyRoomsViewModel by viewModels()

    @Inject
    lateinit var createRoomNavContract: CreateRoomNavContract

    @Inject
    lateinit var roomDetailsNavContract: RoomDetailsNavContract

    @Inject
    lateinit var themeManager: ThemeManager

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
            Result.Loading -> {
                Loader(
                    backgroundColor = themeManager.getColor(colorType = ColorType.BACKGROUND),
                    loaderColor = themeManager.getColor(colorType = ColorType.QUATERNARY)
                )
            }
            is Result.Error -> {
            }
        }
    }

    @Composable
    private fun MyRoomsContent(rooms: List<RoomEntity>) {
        LazyColumn(
            modifier = Modifier
                .background(color = themeManager.getColor(colorType = ColorType.BACKGROUND))
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                VerticalSpacer()
                MyRoomsButtons()
                VerticalSpacer()
            }
            items(rooms.size + 1) { index ->
                if (index < rooms.size) {
                    RoomListItem(
                        room = rooms[index],
                        themeManager = themeManager
                    ) { room ->
                        navigateToRoomDetails(room.id)
                    }
                } else {
                    NavBarSpacer()
                }
            }
        }
    }

    @Composable
    private fun MyRoomsButtons() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MyRoomsButton(buttonText = "Create Room") {
                navigateToCreateRoom()
            }
            MyRoomsButton(buttonText = "Sign Out") {
                signOut()
            }
        }
    }

    @Composable
    private fun MyRoomsButton(
        buttonText: String,
        onButtonClicked: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .width(124.dp)
                .height(50.dp)
                .background(
                    color = themeManager.getColor(colorType = ColorType.BACKGROUND),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(2.dp)
                .border(
                    width = 1.dp,
                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    onButtonClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
            )
        }
    }

    private fun signOut() {
        displayToast("Signing out...")
        Handler(Looper.getMainLooper()).postDelayed({
            (requireActivity() as AuthResultListener).signOut()
        }, 2000L)
    }

    private fun navigateToCreateRoom() {
        createRoomNavContract.show(findNavController())
    }

    private fun navigateToRoomDetails(roomId: String) {
        roomDetailsNavContract.show(roomId, findNavController())
    }
}