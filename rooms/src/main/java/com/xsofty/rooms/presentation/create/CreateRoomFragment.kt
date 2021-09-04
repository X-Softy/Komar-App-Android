package com.xsofty.rooms.presentation.create

import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter
import com.xsofty.categories.domain.model.entity.CategoryEntity
import com.xsofty.shared.Result
import com.xsofty.shared.compose.Loader
import com.xsofty.shared.compose.VerticalSpacer
import com.xsofty.shared.firebase.FirebaseStorageManager
import com.xsofty.shared.theme.ColorType
import com.xsofty.shared.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateRoomFragment : Fragment() {

    private val viewModel: CreateRoomViewModel by viewModels()
    private val firebaseStorageManager = FirebaseStorageManager()

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                when (val categories = viewModel.categories.value) {
                    is Result.Success -> {
                        CreateRoomContent(categories.data)
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
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestCategories()
    }

    @Composable
    private fun CreateRoomContent(categories: List<CategoryEntity>) {
        var chosenCategoryId by remember { mutableStateOf("") }
        var titleText by remember { mutableStateOf("") }
        var descriptionText by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .background(color = themeManager.getColor(colorType = ColorType.BACKGROUND_SECONDARY))
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VerticalSpacer()
            CategoryChooser(
                categories = categories,
                onCategoryChosen = {
                    chosenCategoryId = it.id
                }
            )
            TitleAndInput(title = "TITLE") { title ->
                titleText = title
            }
            TitleAndInput(title = "DESCRIPTION") { description ->
                descriptionText = description
            }
            TextButton(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(
                        color = if (chosenCategoryId.isNotBlank() && titleText.isNotBlank() && descriptionText.isNotBlank()) {
                            themeManager.getColor(colorType = ColorType.TERTIARY)
                        } else {
                            themeManager.getColor(colorType = ColorType.NATIVE_BUTTON_DISABLED)
                        },
                        shape = RoundedCornerShape(16.dp)
                    ),
                onClick = {
                    viewModel.createRoom(
                        categoryId = chosenCategoryId,
                        title = titleText,
                        description = descriptionText
                    )
                },
                enabled = chosenCategoryId.isNotBlank() && titleText.isNotBlank() && descriptionText.isNotBlank()
            ) {
                Text(
                    text = "Create",
                    color = Color.DarkGray
                )
            }
        }
    }

    @Composable
    private fun CategoryChooser(
        categories: List<CategoryEntity>,
        onCategoryChosen: (CategoryEntity) -> Unit
    ) {

        var chosenCategory: CategoryEntity? by remember { mutableStateOf(null) }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) {
                CategoryListItem(
                    category = it,
                    isChosen = chosenCategory?.id == it.id
                ) { newChosenCategory ->
                    chosenCategory = it
                    onCategoryChosen(newChosenCategory)
                }
            }
        }
    }

    @Composable
    private fun CategoryListItem(
        category: CategoryEntity,
        isChosen: Boolean,
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
                    color = themeManager.getColor(colorType = ColorType.PRIMARY),
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxHeight()
                .width(120.dp)
                .border(
                    width = if (!isChosen) 0.dp else 3.dp,
                    color = if (!isChosen) Color.Transparent else Color.Green,
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = category.title,
                    color = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    private fun TitleAndInput(
        title: String,
        onTextEntered: (String) -> Unit
    ) {
        val textFieldText = remember { mutableStateOf("") }
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = themeManager.getColor(colorType = ColorType.HEADER)
        )
        TextField(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            value = textFieldText.value,
            colors = TextFieldDefaults.textFieldColors(
                textColor = themeManager.getColor(colorType = ColorType.TEXT_PRIMARY),
                backgroundColor = themeManager.getColor(colorType = ColorType.INPUT),
                cursorColor = themeManager.getColor(colorType = ColorType.PRIMARY),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = { newText ->
                textFieldText.value = newText
                onTextEntered(newText)
            }
        )
    }
}