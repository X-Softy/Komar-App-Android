package com.xsofty.categories.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.shared.Result
import com.xsofty.shared.firebase.FirebaseStorageManager
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.RoomsNavContract
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CustomBackPressable {

    private val viewModel: CategoriesViewModel by viewModels()
    private val firebaseStorageManager = FirebaseStorageManager()

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
        when (val categories = viewModel.categories.value) {
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
        Column {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
    }

    @Preview
    @Composable
    private fun CategoryListItem(
        category: CategoryEntity = CategoryEntity("", "Category", ""),
        onCategoryClicked: (CategoryEntity) -> Unit = {}
    ) {
        val categoryImageUrl: MutableState<String?> = remember { mutableStateOf(null) }
        firebaseStorageManager.imageIdToUrl(category.imageId) { fetchedImageUrl ->
            categoryImageUrl.value = fetchedImageUrl
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(16.dp)
                )
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
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = RoundedCornerShape(16.dp)
                    ),
                painter = rememberImagePainter(categoryImageUrl.value),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = Color.Green,
                        shape = RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp,
                            bottomStart = 16.dp, bottomEnd = 16.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(16.dp)
                )
                Text(
                    text = category.title,
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                )
            }
        }
    }

    private fun navigateToRooms(categoryId: String) {
        roomsNavContract.show(categoryId, findNavController())
    }
}