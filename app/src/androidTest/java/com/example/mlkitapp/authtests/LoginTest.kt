package com.example.mlkitapp.authtests

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.FirebaseActivity
import com.example.mlkitapp.ui.authentication.screens.LoginScreen
import com.example.mlkitapp.ui.authentication.screens.RegistrationScreen
import com.example.mlkitapp.ui.authentication.viewmodel.AuthViewModel
import com.example.mlkitapp.ui.main.MainScreen
import com.example.mlkitapp.ui.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.nav.routes.NAV_SIGNUP
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters


@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<FirebaseActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()

        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = hiltViewModel()
            authViewModel.logout()

            MLKitAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = NAV_LOGIN,
                ) {
                    composable(NAV_LOGIN) {
                        LoginScreen(authViewModel, navController)
                    }
                    composable(NAV_SIGNUP) {
                        RegistrationScreen(authViewModel, navController)
                    }
                    composable(NAV_MAIN_SCREEN){
                        MainScreen()
                    }
                }
            }
        }
    }

    @Test
    fun loginScreenDisplays(){
        composeTestRule.onNodeWithTag("LOGIN_SCREEN").assertIsDisplayed()
    }

    @Test
    fun loginScreenDisplaysCorrectly(){
        composeTestRule.onNodeWithTag("APP_ICON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LOGIN_FORGOT_PASSWORD_BUTTON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LOGIN_WITH_LABEL").assertIsDisplayed()
        composeTestRule.onNodeWithTag("GOOGLE_ICON_BUTTON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("WITHOUT_ACCOUNT_SIGN_UP_LABEL").assertIsDisplayed()
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").assertIsDisplayed()
    }

    @Test
    fun loginButtonDisabledByDefault(){
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").assertIsNotEnabled()
    }

    @Test
    fun loginButtonEnablesWithCorrectInput(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("cim@email.com")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").assertIsEnabled()
    }

    @Test
    fun emailErrorDisplaysAfterIncorrectInput(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("cim")
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextClearance()
        composeTestRule.onNodeWithText("The email field cannot be empty!").assertIsDisplayed()
    }

    @Test
    fun passwordErrorDisplaysAfterIncorrectInput(){
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("pw")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextClearance()
        composeTestRule.onNodeWithText("The password field cannot be empty!").assertIsDisplayed()
    }

    @Test
    fun passwordIsHiddenByDefault(){
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("pwd1234")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").assertExists().assert(hasText("•••••••"))
    }

    @Test
    fun passwordDisplaysOnIconClick(){
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("pwd1234")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_VISIBILITY_ON_ICON").performClick()
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").assertExists().assert(hasText("pwd1234"))
    }

    @Test
    fun passwordDisplaysThanHidesOnIconClick(){
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("pwd1234")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_VISIBILITY_ON_ICON").performClick()
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_VISIBILITY_OFF_ICON").performClick()
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").assertExists().assert(hasText("•••••••"))
    }

    @Test
    fun changePasswordDialogDisplays(){
        composeTestRule.onNodeWithTag("LOGIN_FORGOT_PASSWORD_BUTTON").performClick()
        composeTestRule.onNodeWithTag("PASSWORD_CHANGE_DIALOG_TAG").assertExists()
    }

    @Test
    fun onButtonClickNavigatesToRegistrationScreen(){
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_SCREEN").assertIsDisplayed()
    }

    @Test
    fun circularIndicatorDisplaysOnLoginInput(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("cim@email.com")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").performClick()
        composeTestRule.onNodeWithTag("LOGIN_PROGRESS_INDICATOR").assertIsDisplayed()
    }

    @Test
    fun circularIndicatorDisplaysOnGoogleLogin(){
        composeTestRule.onNodeWithTag("GOOGLE_ICON_BUTTON").performClick()
        composeTestRule.waitForIdle()
        //composeTestRule.onNodeWithTag("GOOGLE_LOGIN_PROGRESS_INDICATOR").assertIsDisplayed()
    }

    @Test
    fun toastDisplaysOnWrongEmailInput(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("cim")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").performClick()
        composeTestRule.onNodeWithText("The email address is badly formatted.").assertIsDisplayed()
        composeTestRule.waitForIdle()
    }

    @Test
    fun toastDisplaysOnWrongLoginData(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("cim@emailcim.com")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").performClick()
       // composeTestRule.onNode(hasText("There is no user record corresponding to this identifier. The user may have been deleted.")).assertIsDisplayed()
    }

    @Test
    fun mainScreenDisplaysOnValidInput(){
        composeTestRule.onNodeWithTag("LOGIN_EMAIL_FIELD").performTextInput("tesztelek2@tesztelek.hu")
        composeTestRule.onNodeWithTag("LOGIN_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("LOGIN_BUTTON").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        //assert(composeTestRule.activity.actionBar!!.isShowing)
    }
}