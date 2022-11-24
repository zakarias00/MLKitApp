package com.example.mlkitapp.data.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mlkitapp.ui.main.nav.navitems.NavDrawerItems

object TopBarTitleUtils {
    @StringRes var topBarTitle: MutableState<Int> =  mutableStateOf(NavDrawerItems.TextField.title)

    fun changeTitle(titleRes: Int){
        topBarTitle.value = titleRes
    }
}