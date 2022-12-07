package com.example.mlkitapp.ui.nav.bottomnav.navitems

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.nav.routes.NAV_GALLERY
import com.example.mlkitapp.ui.nav.routes.NAV_HOME
import com.example.mlkitapp.ui.nav.routes.NAV_MAP

sealed class BottomNavItems(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String,
    val textToRead: String
){
    object Home: BottomNavItems(R.string.home, R.drawable.ic_camera, NAV_HOME, "You can open the phone's camera and take a photo by tapping on the button or by clicking twice on the screen")
    object Gallery: BottomNavItems(R.string.gallery, R.drawable.ic_library, NAV_GALLERY, "You can open the phone's gallery and take a photo by tapping on the button or by clicking twice on the screen")
    object Map: BottomNavItems(R.string.map, R.drawable.ic_locations, NAV_MAP, "")
}