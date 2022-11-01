package com.example.mlkitapp.ui.profile.screens

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.authentication.FirebaseActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val optionsList: ArrayList<OptionsData> = ArrayList()

@Composable
fun ProfileScreen() {
    var listPrepared by remember {
        mutableStateOf(false)
    }
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()

            prepareOptionsData(viewModel, context)

            listPrepared = true
        }
    }

    if (listPrepared) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                UserDetails(context = context)
            }

            items(optionsList) { item ->
                OptionsItemStyle(item = item, context = context)
            }

        }
    }
}

@Composable
private fun UserDetails(context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier
                .size(64.dp)
                .clip(shape = CircleShape),
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Mock Profile Image"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                Text(
                    text = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                    style = TextStyle(
                        fontSize = 22.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
private fun OptionsItemStyle(item: OptionsData, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                item.onClick()
            }
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier = Modifier
            .size(32.dp),
            painter = painterResource(id = item.iconRes),
            contentDescription = item.title,
            tint = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.subTitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        color = Color.Gray
                    )
                )

            }

            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.ArrowForward,
                contentDescription = item.title
            )
        }

    }
}

private fun prepareOptionsData(authViewModel: AuthViewModel, context: Context) {

    optionsList.add(
        OptionsData(
            iconRes = R.drawable.ic_person,
            title = "Account",
            subTitle = "Manage your account",
            onClick = { /*TODO*/ }
        )
    )


    optionsList.add(
        OptionsData(
            iconRes = R.drawable.twotone_bookmark_black_24dp,
            title = "Saved",
            subTitle = "Your saved recognized texts",
            onClick = { /*TODO*/ }
        )
    )

    optionsList.add(
        OptionsData(
            iconRes = R.drawable.ic_settings,
            title = "Settings",
            subTitle = "General app settings",
            onClick = { /*TODO*/ }
        )
    )

    optionsList.add(
        OptionsData(
            iconRes = R.drawable.ic_logout,
            title = "Logout",
            subTitle = "Logout from you account",
            onClick = {
                authViewModel.logout()
                context.startActivity(Intent(context, FirebaseActivity::class.java))
            }
        )
    )

}
data class OptionsData(@DrawableRes val iconRes: Int, val title: String, val subTitle: String, val onClick:() -> Unit)

@Preview
@Composable
fun Preview(){
    //ProfileScreen()
}