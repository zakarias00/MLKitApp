package com.example.mlkitapp.ui.main.nav.navitems

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.main.nav.routes.NAV_GALLERY
import com.example.mlkitapp.ui.main.nav.routes.NAV_HOME
import com.example.mlkitapp.ui.main.nav.routes.NAV_MAP
import com.example.mlkitapp.ui.main.nav.routes.NAV_SAVED

sealed class BottomNavItems(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
){
    object Home: BottomNavItems(R.string.home, R.drawable.ic_camera, NAV_HOME)
    object Gallery: BottomNavItems(R.string.gallery, R.drawable.ic_library, NAV_GALLERY)
    object Saved: BottomNavItems(R.string.saved, R.drawable.twotone_bookmark_black_24dp, NAV_SAVED)
    object Map: BottomNavItems(R.string.map, R.drawable.ic_locations, NAV_MAP)
}