package com.example.mlkitapp.ui.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGOUT
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_ACCOUNT_MANAGEMENT
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_SETTINGS
import com.example.mlkitapp.ui.main.nav.routes.NAV_SAVED

sealed class ProfileNavItems(
    @StringRes val title: Int,
    @StringRes val sub_title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
){
    object Account : ProfileNavItems(R.string.account, R.string.account_sub_title, R.drawable.ic_person, NAV_PROFILE_ACCOUNT_MANAGEMENT)
    object Saved : ProfileNavItems(R.string.saved, R.string.saved_sub_title, R.drawable.twotone_bookmark_black_24dp, NAV_SAVED)
    object Settings : ProfileNavItems(R.string.settings, R.string.settings_sub_title, R.drawable.ic_settings, NAV_PROFILE_SETTINGS)
    object Logout : ProfileNavItems(R.string.logout, R.string.logout_sub_title, R.drawable.ic_logout, NAV_LOGOUT)
}