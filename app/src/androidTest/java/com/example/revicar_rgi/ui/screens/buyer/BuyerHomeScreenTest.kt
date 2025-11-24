package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class BuyerHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificaElementosDeIUSeMuestran() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            BuyerHomeScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Compra tu próximo auto\ncon total seguridad.")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(
            "Agenda una inspección profesional antes de decidir. Evita sorpresas y negocia un precio justo"
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText("SOLICITAR INSPECCIÓN")
            .assertIsDisplayed()
    }

    @Test
    fun clickEnBotonDisparaAccion() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            BuyerHomeScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("SOLICITAR INSPECCIÓN")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

}