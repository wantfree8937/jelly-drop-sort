package com.example.jelly_drop_sort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jelly_drop_sort.presentation.ui.game.GameScreen
import com.example.jelly_drop_sort.presentation.ui.home.HomeScreen
import com.example.jelly_drop_sort.presentation.ui.theme.JellydropsortTheme
import com.example.jelly_drop_sort.presentation.viewmodel.GameViewModel
import com.example.jelly_drop_sort.presentation.viewmodel.HomeViewModel

// 앱의 진입점(MainActivity)입니다.
// 간단한 상태(Screen) 관리를 통해 홈 화면과 본 게임 화면 간의 화면 전환을 처리합니다.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val homeViewModel = HomeViewModel()
        val gameViewModel = GameViewModel()

        setContent {
            JellydropsortTheme {
                // 현재 노출할 화면 상태 (HOME 또는 GAME)
                var currentScreen by remember { mutableStateOf(Screen.HOME) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        Screen.HOME -> {
                            HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToGame = {
                                    gameViewModel.startGame() // 게임 화면 진입 시 상태 초기화
                                    currentScreen = Screen.GAME
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        Screen.GAME -> {
                            GameScreen(
                                viewModel = gameViewModel,
                                onBackToHome = {
                                    currentScreen = Screen.HOME
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 화면 구분을 위한 Enum 클래스입니다.
enum class Screen {
    HOME, GAME
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    JellydropsortTheme {
        HomeScreen(viewModel = HomeViewModel(), onNavigateToGame = {})
    }
}