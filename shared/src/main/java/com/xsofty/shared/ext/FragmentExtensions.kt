package com.xsofty.shared.ext

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.displayToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}