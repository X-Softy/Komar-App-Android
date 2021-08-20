package com.xsofty.categories.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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
import com.xsofty.shared.nav.BottomNavigationHandler
import com.xsofty.shared.nav.contracts.CreateRoomNavContract
import com.xsofty.shared.nav.contracts.MyRoomsNavContract
import com.xsofty.shared.nav.contracts.RoomsNavContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CustomBackPressable {

    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var roomsNavContract: RoomsNavContract

    @Inject
    lateinit var createRoomNavContract: CreateRoomNavContract

    @Inject
    lateinit var myRoomsNavContract: MyRoomsNavContract

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as BottomNavigationHandler).showNavigation()
    }

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
                DisplayCategories()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestCategories()
    }

    @Composable
    private fun DisplayCategories() {
        when (val categories = viewModel.categories.value) {
            is Result.Success -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    for (category in categories.data) {
                        TextButton(onClick = { navigateToRooms(category.id) }) {
                            Text(text = category.title)
                        }
                    }
                    Button(onClick = { navigateToMyRooms() }) {
                        Text(text = "My rooms")
                    }
                    Button(onClick = { navigateToCreateRoom() }) {
                        Text(text = "Create room")
                    }
                }
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }

    private fun navigateToMyRooms() {
        myRoomsNavContract.show(findNavController())
    }

    private fun navigateToRooms(categoryId: String) {
        roomsNavContract.show(categoryId, findNavController())
    }

    private fun navigateToCreateRoom() {
        createRoomNavContract.show(findNavController())
    }
}