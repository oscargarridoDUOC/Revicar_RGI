package com.example.revicar_rgi.ui.screens.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.revicar_rgi.ui.viewmodel.NotificationsViewModel
import org.junit.Rule
import org.junit.Test

class NotificationsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTitulo() {
        val viewModel = NotificationsViewModel()

        composeTestRule.setContent {
            NotificationsScreen(viewModel = viewModel)
        }

        // Verificar título
        composeTestRule.onNodeWithText("Notificaciones").assertIsDisplayed()
    }
}
