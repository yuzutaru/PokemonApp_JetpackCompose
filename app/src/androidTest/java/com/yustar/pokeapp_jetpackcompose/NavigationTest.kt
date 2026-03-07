package com.yustar.pokeapp_jetpackcompose

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.yustar.auth.R
import com.yustar.auth.domain.LoginUserUseCase
import com.yustar.auth.presentation.LoginViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest

class FakeLoginUserUseCase : LoginUserUseCase {
    override suspend fun invoke(username: String, password: String): Boolean = true
}

class NavigationTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            modules(module {
                factory<LoginUserUseCase> { FakeLoginUserUseCase() }
                viewModel { LoginViewModel(get()) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun navHost_startDestinationIsLogin() {
        lateinit var navController: TestNavHostController
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PokeAppNavHost(navController = navController, startDestination = "login")
        }

        // Check that the login screen is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.login_to_your_account)).assertIsDisplayed()
        
        // Verify current destination is "login"
        assertEquals("login", navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun navHost_loginSuccess_navigatesToHome() {
        lateinit var navController: TestNavHostController
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PokeAppNavHost(navController = navController, startDestination = "login")
        }

        // Input some text
        composeTestRule.onNodeWithText(context.getString(R.string.input_email)).performTextInput("test@example.com")
        composeTestRule.onNodeWithText(context.getString(R.string.input_password)).performTextInput("password")

        // Click login
        composeTestRule.onNodeWithText(context.getString(R.string.login)).performClick()

        // Verify current destination is "home"
        composeTestRule.waitForIdle()
        assertEquals("home", navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun navHost_startDestinationIsHome() {
        lateinit var navController: TestNavHostController
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PokeAppNavHost(navController = navController, startDestination = "home")
        }

        // Verify current destination is "home"
        assertEquals("home", navController.currentBackStackEntry?.destination?.route)
    }
}
