package com.example.mlkitapp.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun ProfileNavItemsUi(
    modifier: Modifier = Modifier,
    navController: NavController,
    onDestinationClicked: (route: String) -> Unit
) {

    val screens = listOf(
        ProfileNavItems.Account,
        ProfileNavItems.Saved,
        ProfileNavItems.Settings,
        ProfileNavItems.Logout,
    )

    val currentUserEmail = FirebaseAuth.getInstance().currentUser!!.email
    val backgroundColor = MaterialTheme.colors.primaryVariant.copy(0.8f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier
                    .padding(32.dp)
                    .drawBehind {
                        drawCircle(
                            color = backgroundColor,
                            radius = this.size.maxDimension
                        )
                    },
                text = currentUserEmail!![0].toString().uppercase(Locale.ROOT),
                style = TextStyle(
                    fontSize = 32.sp,
                ),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = currentUserEmail.toString(),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
        }

        screens.forEach {  item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true) {
                        item.title
                        onDestinationClicked(item.navRoute)
                    }
                    .padding(all = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(modifier = Modifier
                    .size(32.dp),
                    painter = painterResource(id = item.icon),
                    contentDescription = stringResource(id = item.title),
                    tint = MaterialTheme.colors.primaryVariant
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
                            text = stringResource(id = item.title),
                            style = TextStyle(
                                fontSize = 18.sp,
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = stringResource(id = item.sub_title),
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
                        contentDescription = stringResource(id = item.title)
                    )
                }

            }
        }
    }
}