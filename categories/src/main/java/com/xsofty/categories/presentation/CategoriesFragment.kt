package com.xsofty.categories.presentation

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
import androidx.navigation.fragment.findNavController
import com.xsofty.shared.Result
import com.xsofty.shared.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.xsofty.categories.R

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {

    private val viewModel: CategoriesViewModel by viewModels()

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
                        .fillMaxHeight(),
                ) {
                    for (category in categories.data) {
                        TextButton(onClick = { navigateToRooms(category.id) }) {
                            Text(text = category.title)
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

    private fun navigateToRooms(categoryId: String) {
        findNavController().navigate(
            CategoriesFragmentDirections.actionToRooms(categoryId)
        )
    }
}