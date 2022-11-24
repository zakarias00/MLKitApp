package com.example.mlkitapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.utils.SharedObject
import com.example.mlkitapp.ui.main.nav.navitems.NavDrawerItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
){
    val items = listOf(
        NavDrawerItems.TextField,
        NavDrawerItems.Barcode,
        // NavDrawerItems.Document,
        // NavDrawerItems.Face,
        //  NavDrawerItems.Image
    )

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .fillMaxHeight()
    ) {

        Text(
            text = stringResource(id = R.string.menu),
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .wrapContentSize()
                .padding(36.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.navRoute, onItemClick = {
                it.title
                SharedObject.changeInput(item.title)

                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    NavigationDrawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}