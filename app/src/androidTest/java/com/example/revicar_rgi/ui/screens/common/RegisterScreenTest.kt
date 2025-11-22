package com.example.revicar_rgi.ui.screens.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTituloYCampos() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val authViewModel = AuthViewModel(authRepository)

        composeTestRule.setContent {
            RegisterScreen(
                authViewModel = authViewModel,
                onSuccessNavigation = {},
                onNavigateToLogin = {}
            )
        }

        // Verificar título
        composeTestRule.onNodeWithText("Crear Cuenta").assertIsDisplayed()

        // Verificar campos
        composeTestRule.onNodeWithText("Nombre *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apellidos *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Run * (ej: 12345678-k)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirmar contraseña *").assertIsDisplayed()
        
        // Verificar Checkbox
        composeTestRule.onNodeWithText("¿Eres mecánico?").assertIsDisplayed()

        // Verificar botón "Registrarse"
        composeTestRule.onNodeWithText("Registrarse").assertIsDisplayed()
        
        // Verificar link a login
        composeTestRule.onNodeWithText("¿Ya tienes cuenta? Inicia sesión").assertIsDisplayed()
    }
}
