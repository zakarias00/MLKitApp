package com.example.mlkitapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.navigation.AppBottomNavigation
import com.example.mlkitapp.ui.navigation.NAV_GALLERY
import com.example.mlkitapp.ui.navigation.NAV_HOME
import com.example.mlkitapp.ui.navigation.NAV_MAP
import com.example.mlkitapp.ui.navigation.NAV_SAVED
import com.example.mlkitapp.ui.textrecognition.BuildCameraUi
import com.example.mlkitapp.ui.textrecognition.OpenSettingsInterface
import com.example.mlkitapp.viewmodel.MLKitViewModel

class MainActivity : ComponentActivity(), OpenSettingsInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mlKitViewModel: MLKitViewModel by viewModels()
            val navigationController = rememberNavController()

            MainScreen(mlKitViewModel, navigationController, this)
        }
    }

    override fun onOpenSettingsClick() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: MLKitViewModel,
    navController: NavController,
    openSettingsInterface: OpenSettingsInterface
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    val showTextRecognitionResult = remember{ viewModel.showOutput }

    var topBarTitle = remember {
        "Text field"
    }
    Scaffold(
        topBar = {
                 TopAppBar(
                     title = { Text(topBarTitle) },
                     navigationIcon = {
                             IconButton(onClick = {
                                 /*TODO*/
                             }) {
                                 Icon(Icons.Rounded.Menu, "settingsIcon")
                             }
                     },
                     actions = {
                               IconButton(onClick = {
                               /*TODO*/
                               }) {
                                   Icon(Icons.Rounded.Settings, "settingsIcon")
                               }
                     },
                     backgroundColor = MaterialTheme.colors.secondary,
                     contentColor = Color.Black,
                     elevation = 10.dp
                 )
        },
        bottomBar = { AppBottomNavigation(navController = navController) },
        drawerBackgroundColor = colorResource(id = R.color.teal_700),
        drawerContent = {
            }
    ) {
        NavHost(navController = navController as NavHostController, startDestination = NAV_HOME) {
            composable(NAV_HOME) {
                BuildCameraUi(
                    context = LocalContext.current,
                    mlKitViewModel = viewModel,
                    cameraScanListener = openSettingsInterface
                )
            }
            composable(NAV_GALLERY) { AppScreen(text = "Gallery Screen") }
            composable(NAV_SAVED) { AppScreen(text = "Saved Screen") }
            composable(NAV_MAP) { AppScreen(text = "map Screen") }
        }
    }
}


@Composable
fun AppScreen(text: String) = Surface(modifier = Modifier.fillMaxSize()) {
    Text(text = text, fontSize = 32.sp)
}

/*@Composable
fun HomeScreen(
    context: Context,
    viewModel: MLKitViewModel,
){
    BuildCameraUi(
        context = context
    )
}*/

@Preview
@Composable
fun DefaultPreview() {
   // MainScreen()
}