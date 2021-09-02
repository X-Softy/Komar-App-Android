package com.xsofty.komarista.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.xsofty.komarista.R
import com.xsofty.komarista.helper.AuthResultListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SignInView {
                    (requireActivity() as AuthResultListener).fireSignInIntent()
                }
            }
        }
    }
}

@Composable
private fun SignInView(
    onSignInButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.smoke_pink),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            SignInButton {
                onSignInButtonClicked()
            }
            Spacer(modifier = Modifier.height(128.dp))
        }
    }
}

@Composable
private fun SignInButton(
    onButtonClick: () -> Unit = {}
) {
    val buttonTextIdle = stringResource(R.string.button_sign_in_idle)
    val buttonTextPressed = stringResource(R.string.button_sign_in_pressed)

    val buttonText = remember { mutableStateOf(buttonTextIdle) }

    Row(
        modifier = Modifier
            .background(color = Color.White, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(64.dp)
            .clickable {
                buttonText.value = buttonTextPressed
                onButtonClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.icon_google),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = buttonText.value,
                color = Color.DarkGray,
                fontSize = 20.sp
            )
        }
    }
}