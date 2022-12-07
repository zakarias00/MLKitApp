package com.example.mlkitapp.ui.nav.navdrawer.navitems

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.nav.routes.NAV_BARCODE
import com.example.mlkitapp.ui.nav.routes.NAV_TEXT_FIELD

sealed class NavDrawerItems(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
){
    object TextField : NavDrawerItems(R.string.text_field, R.drawable.ic_text_fields, NAV_TEXT_FIELD)
    object Barcode : NavDrawerItems(R.string.barcode, R.drawable.ic_qr_code, NAV_BARCODE)
}