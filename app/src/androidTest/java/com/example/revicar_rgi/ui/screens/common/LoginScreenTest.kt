package com.example.revicar_rgi.ui.screens.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTituloYCampos() {
        // Usamos objetos reales ya que no podemos usar Mockk
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val authViewModel = AuthViewModel(authRepository)

        composeTestRule.setContent {
            LoginScreen(
                authViewModel = authViewModel,
                onSuccessNavigation = {},
                onNavigateToRegister = {}
            )
        }

        // Verificar título
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()

        // Verificar campo "Email"
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()

        // Verificar campo "Contraseña"
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")
        
        // Verificar botón "Ingresar"
        composeTestRule.onNodeWithText("Ingresar").assertIsDisplayed()
        // No hacemos click para evitar llamadas de red reales que podrían fallar o tardar
        // composeTestRule.onNodeWithText("Ingresar").performClick()

        // Verificar link de registro
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate").assertIsDisplayed()
    }
}
