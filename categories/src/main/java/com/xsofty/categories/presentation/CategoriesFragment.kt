package com.xsofty.categories.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.shared.Result
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.RoomsNavContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CustomBackPressable {

    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var roomsNavContract: RoomsNavContract

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
                CategoriesView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestCategories()
    }

    @Composable
    private fun CategoriesView() {
        val categories = produceState<Result<List<CategoryEntity>>>(initialValue = Result.Loading) {
            value = viewModel.categories.value
        }.value

        when (categories) {
            is Result.Success -> {
                CategoriesContent(categories.data)
            }
            is Result.Error -> {
            }
            Result.Loading -> {
            }
        }
    }

    @Composable
    private fun CategoriesContent(categories: List<CategoryEntity>) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(categories) {
                CategoryListItem(category = it) { category ->
                    navigateToRooms(category.id)
                }
            }
        }
    }

    @Preview
    @Composable
    private fun CategoryListItem(
        category: CategoryEntity = CategoryEntity("", "Category"),
        onCategoryClicked: (CategoryEntity) -> Unit = {}
    ) {
        Box(modifier = Modifier
            .background(Color.Blue) // remove
            .height(160.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Magenta,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onCategoryClicked(category)
            }
        ) {
            Text(
                text = category.title,
                modifier = Modifier
                    .background(Color.Gray)
                    .height(44.dp)
                    .fillMaxWidth()
                    .border(
                        width = 0.dp,
                        color = Color.Magenta,
                        shape = RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp,
                            bottomStart = 16.dp, bottomEnd = 16.dp
                        )
                    )
            )
//            Card(
//                modifier = Modifier
//                    .background(Color.Gray)
//                    .height(44.dp)
//                    .fillMaxWidth(),
//                shape = RoundedCornerShape(
//                    topStart = 0.dp, topEnd = 0.dp,
//                    bottomStart = 16.dp, bottomEnd = 16.dp
//                )
//            ) {
//            }
        }
    }

    private fun navigateToRooms(categoryId: String) {
        roomsNavContract.show(categoryId, findNavController())
    }
}