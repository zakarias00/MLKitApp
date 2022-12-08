package com.example.mlkitapp.authtests

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
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
import java.util.Date

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RegistrationTest {

    private val randomTimeStamp = Date().time

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
                    startDestination = NAV_SIGNUP,
                ) {
                    composable(NAV_LOGIN) {
                        LoginScreen(authViewModel, navController)
                    }
                    composable(NAV_SIGNUP) {
                        RegistrationScreen(authViewModel, navController)
                    }
                    composable(NAV_MAIN_SCREEN){
                        MainScreen(Modifier.testTag("TAG"))
                    }
                }
            }
        }
    }

    @Test
    fun registrationScreenDisplays(){
        composeTestRule.onNodeWithTag("REGISTRATION_SCREEN").assertIsDisplayed()
    }

    @Test
    fun registrationDisplaysCorrectly(){
        composeTestRule.onNodeWithTag("APP_ICON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").assertIsDisplayed()
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").assertIsDisplayed()
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").assertIsDisplayed()
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").assertIsDisplayed()
        composeTestRule.onNodeWithTag("CANCEL_BUTTON").assertIsDisplayed()
    }

    @Test
    fun registrationButtonDisabledByDefault(){
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").assertIsNotEnabled()
    }

    @Test
    fun registrationButtonEnablesWithCorrectInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("cim@email.com")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").assertIsEnabled()
    }

    @Test
    fun registrationButtonDisabledOnDifferentPasswordInputs(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("cim@email.com")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("1234567")
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").assertIsNotEnabled()
    }

    @Test
    fun emailErrorDisplaysOnEmptyInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("cim")
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextClearance()
        composeTestRule.onNodeWithText("The email field cannot be empty!").assertIsDisplayed()
    }

    @Test
    fun emailErrorDisplaysOnInputWithoutAtCharacter(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("cim")
        composeTestRule.onNodeWithText("The email field must contains a @ character!").assertIsDisplayed()
    }

    @Test
    fun emailErrorDisplaysOnInputWithoutDomainName(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("cim@email")
        composeTestRule.onNodeWithText("The email field must end with a domain name!").assertIsDisplayed()
    }

    @Test
    fun passwordErrorDisplaysOnEmptyInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pw")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextClearance()
        composeTestRule.onNodeWithText("The password field cannot be empty!").assertIsDisplayed()
    }

    @Test
    fun passwordErrorDisplaysOnWrongInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pwd1234")
        composeTestRule.onNodeWithText("The password must contain at least 8 characters!").assertIsDisplayed()
    }

    @Test
    fun confirmedPasswordErrorDisplaysAfterDifferentInputs(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithText("The two passwords must be equals!").assertIsDisplayed()
    }

    @Test
    fun passwordIsHiddenByDefault(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").assertExists().assert(hasText("••••••••"))
    }

    @Test
    fun passwordDisplaysOnIconClick(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("VISIBILITY_ON_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").assertExists().assert(hasText("pwd12345"))
    }

    @Test
    fun passwordDisplaysThanHidesOnIconClick(){
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("VISIBILITY_ON_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("VISIBILITY_OFF_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").assertExists().assert(hasText("••••••••"))
    }

    @Test
    fun confirmedPasswordIsHiddenByDefault(){
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").assertExists().assert(hasText("••••••••"))
    }

    @Test
    fun confirmedPasswordDisplaysOnIconClick(){
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("CONFIRMED_PASSWORD_VISIBILITY_ON_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").assertExists().assert(hasText("pwd12345"))
    }

    @Test
    fun confirmedPasswordDisplaysThanHidesOnIconClick(){
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("pwd12345")
        composeTestRule.onNodeWithTag("CONFIRMED_PASSWORD_VISIBILITY_ON_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("CONFIRMED_PASSWORD_VISIBILITY_OFF_ICON_TAG").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").assertExists().assert(hasText("••••••••"))
    }

    @Test
    fun onButtonClickNavigatesToRegistrationScreen(){
        composeTestRule.onNodeWithTag("CANCEL_BUTTON").performClick()
        composeTestRule.onNodeWithTag("LOGIN_SCREEN").assertIsDisplayed()
    }

    @Test
    fun circularIndicatorDisplaysOnValidRegistrationInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("$randomTimeStamp@email.com")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_PROGRESS_INDICATOR").assertIsDisplayed()
    }

    @Test
    fun loginScreenDoNotHideOnWrongRegistrationData(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("tesztelek2@tesztelek.hu")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_BUTTON").performClick()
        composeTestRule.onNodeWithTag("REGISTRATION_SCREEN").assertIsDisplayed()
    }

    @Test
    fun mainScreenDisplaysOnValidInput(){
        composeTestRule.onNodeWithTag("REGISTRATION_EMAIL_FIELD").performTextInput("$randomTimeStamp@email.com")
        composeTestRule.onNodeWithTag("REGISTRATION_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithTag("REGISTRATION_CONFIRMED_PASSWORD_FIELD").performTextInput("12345678")
        composeTestRule.onNodeWithText("REGISTRATION").performClick()
        composeTestRule.waitUntil (
            5000
        ) {
            composeTestRule.onAllNodesWithTag("MAIN_SCREEN")
                .fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithTag("MAIN_SCREEN").assertIsDisplayed()
    }
}