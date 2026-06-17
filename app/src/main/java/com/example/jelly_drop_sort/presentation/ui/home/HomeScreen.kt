package com.example.jelly_drop_sort.presentation.ui.home

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jelly_drop_sort.presentation.viewmodel.HomeEvent
import com.example.jelly_drop_sort.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 젤리 느낌의 화사한 메인 홈 화면 컴포저블입니다.
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 뷰모델 이벤트 구독
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToGame -> {
                    onNavigateToGame()
                }
            }
        }
    }

    // 젤리 정렬 게임 컨셉에 어울리는 소프트 그라데이션 배경
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF0F5), // 라벤더 블러시 (Lavender Blush)
            Color(0xFFE8D3FF), // 소프트 퍼플 (Soft Purple)
            Color(0xFFFFD1DC)  // 파스텔 핑크 (Pastel Pink)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 80.dp, horizontal = 24.dp)
        ) {
            // 타이틀 영역
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Jelly Drop\nSort",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF6A1B9A), // 깊은 보라색
                    lineHeight = 56.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = false
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "말랑말랑 젤리를 정렬해보세요!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8E24AA),
                    textAlign = TextAlign.Center
                )
            }

            // 젤리 느낌을 형상화한 그래픽 플레이스홀더 (둥글고 귀여운 원형 젤리 디자인들)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                JellyBubble(color = Color(0xFFFF6B6B), size = 60)
                JellyBubble(color = Color(0xFF4D96FF), size = 80)
                JellyBubble(color = Color(0xFF6BCB77), size = 50)
                JellyBubble(color = Color(0xFFFFD93D), size = 70)
            }

            // "게임 시작" 버튼
            JellyButton(
                text = "게임 시작",
                onClick = {
                    coroutineScope.launch {
                        viewModel.onStartGameClicked()
                    }
                }
            )
        }
    }
}

// 젤리를 상징하는 둥글둥글하고 반짝이는 느낌의 그래픽 요소 컴포저블입니다.
@Composable
fun JellyBubble(color: Color, size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(30.dp),
                ambientColor = color.copy(alpha = 0.5f),
                spotColor = color
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.8f),
                        color,
                        color.copy(alpha = 0.9f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
    )
}

// 클릭 시 스케일 축소 애니메이션이 작동하는 젤리 질감의 커스텀 버튼입니다.
@Composable
fun JellyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ButtonScale"
    )

    val buttonBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFF4081), // 네온 핑크
            Color(0xFF9C27B0)  // 비비드 퍼플
        )
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .width(240.dp)
            .height(64.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0xFFFF4081)
            )
            .background(
                brush = buttonBrush,
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 기본 클릭 리플 효과 제거 (커스텀 젤리 튕김 애니메이션 적용 목적)
                onClick = onClick
            )
    ) {
        // 내부 텍스트
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // 사용자가 터치할 때 버튼 상태 변경 핸들러
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                        isPressed = true
                    }
                    is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                        isPressed = false
                    }
                    is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                        isPressed = false
                    }
                }
            }
        }
    }
}
