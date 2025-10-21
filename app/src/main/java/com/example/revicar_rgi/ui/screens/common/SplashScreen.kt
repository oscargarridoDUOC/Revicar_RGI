// Archivo: ui/screens/common/SplashScreen.kt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.SplashCheckState

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    val splashState by authViewModel.splashState.collectAsState()

    LaunchedEffect(splashState) {
        when (splashState) {
            SplashCheckState.NavigateToLogin -> {
                onNavigate(AppRoutes.LOGIN_SCREEN)
            }
            SplashCheckState.NavigateToUserHome -> {
                onNavigate(AppRoutes.MAIN_APP_SCREEN)
            }
            SplashCheckState.NavigateToMechanicHome -> {
                onNavigate(AppRoutes.MECHANIC_HOME_SCREEN)
            }
            SplashCheckState.Loading -> {
            }
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.checkCurrentUser()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}