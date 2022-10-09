package com.example.mlkitapp.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.navigation.NAV_BARCODE
import com.example.mlkitapp.ui.navigation.NAV_DOCUMENT
import com.example.mlkitapp.ui.navigation.NAV_FACE
import com.example.mlkitapp.ui.navigation.NAV_IMAGE
import com.example.mlkitapp.ui.navigation.NAV_TEXT_FIELD

sealed class NavDrawerItems(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
){
    object Text_field : NavDrawerItems(R.string.text_field, R.drawable.ic_text_fields, NAV_TEXT_FIELD)
    object Document : NavDrawerItems(R.string.document, R.drawable.ic_document, NAV_DOCUMENT)
    object Barcode : NavDrawerItems(R.string.barcode, R.drawable.ic_camera, NAV_BARCODE)
    object Face : NavDrawerItems(R.string.face, R.drawable.ic_face, NAV_FACE)
    object Image : NavDrawerItems(R.string.image, R.drawable.ic_image, NAV_IMAGE)
}