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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.shared.Result
import com.xsofty.shared.compose.Loader
import com.xsofty.shared.compose.NavBarSpacer
import com.xsofty.shared.compose.VerticalSpacer
import com.xsofty.shared.firebase.FirebaseStorageManager
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.RoomsNavContract
import com.xsofty.shared.theme.ColorType
import com.xsofty.shared.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment(), CustomBackPressable {

    private val viewModel: CategoriesViewModel by viewModels()
    private val firebaseStorageManager = FirebaseStorageManager()

    @Inject
    lateinit var roomsNavContract: RoomsNavContract

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
            Result.Loading -> {
                Loader(
                    backgroundColor = themeManager.getColor(colorType = ColorType.BACKGROUND),
                    loaderColor = themeManager.getColor(colorType = ColorType.PRIMARY)
                )
            }
            is Result.Error -> {
            }
        }
    }

    @Composable
    private fun CategoriesContent(categories: List<CategoryEntity>) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(color = themeManager.getColor(colorType = ColorType.BACKGROUND))
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item { VerticalSpacer() }
            items(categories.size + 1) { index ->
                if (index < categories.size) {
                    CategoryListItem(category = categories[index]) { category ->
                        navigateToRooms(category.id)
                    }
                } else {
                    NavBarSpacer()
                }
            }
        }
    }

    @Composable
    private fun CategoryListItem(
        category: CategoryEntity,
        onCategoryClicked: (CategoryEntity) -> Unit
    ) {
        val categoryImageUrl: MutableState<String?> = remember { mutableStateOf(null) }
        firebaseStorageManager.imageIdToUrl(category.imageId) { fetchedImageUrl ->
            categoryImageUrl.value = fetchedImageUrl
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .background(
                    color = themeManager.getColor(colorType = ColorType.SECONDARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .height(190.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = themeManager.getColor(colorType = ColorType.SECONDARY),
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
                        color = themeManager.getColor(colorType = ColorType.PRIMARY),
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
                    color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                    fontSize = 20.sp,
                )
            }
        }
    }

    private fun navigateToRooms(categoryId: String) {
        roomsNavContract.show(categoryId, findNavController())
    }
}