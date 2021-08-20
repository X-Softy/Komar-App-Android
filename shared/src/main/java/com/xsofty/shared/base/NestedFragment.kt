package com.xsofty.shared.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.xsofty.shared.nav.BottomNavigationHandler

abstract class NestedFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as BottomNavigationHandler).hideNavigation()
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as BottomNavigationHandler).showNavigation()
    }
}