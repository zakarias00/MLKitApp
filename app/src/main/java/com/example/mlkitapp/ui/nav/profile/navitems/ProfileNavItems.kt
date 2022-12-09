package com.example.mlkitapp.ui.nav.profile.navitems

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.nav.routes.NAV_LOGOUT
import com.example.mlkitapp.ui.nav.routes.NAV_PROFILE_ACCOUNT_MANAGEMENT
import com.example.mlkitapp.ui.nav.routes.NAV_PROFILE_SETTINGS
import com.example.mlkitapp.ui.nav.routes.NAV_SAVED

sealed class ProfileNavItems(
    @StringRes val title: Int,
    @StringRes val sub_title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String,
    val readableText: String
){
    object Account : ProfileNavItems(R.string.account, R.string.account_sub_title, R.drawable.ic_person, NAV_PROFILE_ACCOUNT_MANAGEMENT, "You clicked on the first option, now you are on account management screen, where you can change your password.")
    object Saved : ProfileNavItems(R.string.saved, R.string.saved_sub_title, R.drawable.twotone_bookmark_black_24dp, NAV_SAVED, "You clicked on the second option, now you are on saved screen")
    object Settings : ProfileNavItems(R.string.settings, R.string.settings_sub_title, R.drawable.ic_settings, NAV_PROFILE_SETTINGS, "You clicked on the third option, now you are on the settings screen, where you can open several app related settings. By clicking on the first option, you open general app settings. By clicking on the second option you can open accessibility settings. By clicking on the third option you can open location settings. By clicking on the last option you can disable talkback.")
    object Logout : ProfileNavItems(R.string.logout, R.string.logout_sub_title, R.drawable.ic_logout, NAV_LOGOUT, "You clicked on logout option")
}