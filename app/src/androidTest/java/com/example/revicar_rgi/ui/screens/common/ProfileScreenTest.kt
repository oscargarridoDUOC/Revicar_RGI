package com.example.revicar_rgi.ui.screens.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.ui.viewmodel.ProfileViewModel
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTitulo() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val viewModel = ProfileViewModel(authRepository)
        
        composeTestRule.setContent {
            val navController = rememberNavController()
            ProfileScreen(
                viewModel = viewModel,
                navController = navController,
                onLogout = {}
            )
        }

        // Verificar título "Mi Perfil"
        composeTestRule.onNodeWithText("Mi Perfil").assertIsDisplayed()
    }
}
