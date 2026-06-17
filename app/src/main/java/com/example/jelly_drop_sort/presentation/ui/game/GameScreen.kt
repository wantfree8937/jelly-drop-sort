package com.example.jelly_drop_sort.presentation.ui.game

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jelly_drop_sort.R
import com.example.jelly_drop_sort.presentation.viewmodel.GameViewModel

// 컬러 소트 본 게임의 플레이 화면 컴포저블입니다.
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dishes by viewModel.dishes
    val selectedIndex by viewModel.selectedDishIndex
    val moveCount by viewModel.moveCount
    val isClear by viewModel.isGameClear

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1. 배경 이미지 렌더링
        Image(
            painter = painterResource(id = R.drawable.bg_game_main),
            contentDescription = "게임 배경",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. 메인 게임 콘텐츠 레이아웃
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // 상단 헤더 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 뒤로가기 (홈으로) 버튼
                IconButton(onClick = onBackToHome) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "◀", 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF6A1B9A)
                        )
                    }
                }

                // 현재까지의 이동 횟수 보드
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "이동 횟수: $moveCount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                }

                // 다시 시작(새 게임) 버튼
                IconButton(onClick = { viewModel.startGame() }) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↻", 
                            fontSize = 22.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF6A1B9A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 접시들 가로 배열 배치 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                dishes.forEachIndexed { index, dish ->
                    val isSelected = selectedIndex == index

                    // 개별 접시 및 젤리가 담기는 수직 영역 터치 범위
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .width(68.dp)
                            .height(280.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, // 기본 리플 제거하여 수동 애니메이션 극대화
                                onClick = { viewModel.onDishClicked(index) }
                            )
                    ) {
                        // 접시 그래픽 렌더링
                        Image(
                            painter = painterResource(id = R.drawable.img_jelly_dish1),
                            contentDescription = "접시",
                            modifier = Modifier
                                .width(64.dp)
                                .height(56.dp)
                                .align(Alignment.BottomCenter)
                        )

                        // 해당 접시에 속한 젤리 리스트를 수직으로 적재
                        dish.jellies.forEachIndexed { jellyIndex, jellyColor ->
                            val isTop = jellyIndex == dish.jellies.lastIndex
                            
                            // 선택된 접시의 맨 위 젤리에 대해 위로 뜨는 오프셋 애니메이션 처리
                            val bounceOffset by animateDpAsState(
                                targetValue = if (isSelected && isTop) -32.dp else 0.dp,
                                animationSpec = tween(150),
                                label = "JellyJump"
                            )

                            // 젤리 높이와 겹침 오프셋 계산 (기본 접시 위 기준)
                            val baseOffsetY = -16.dp - (jellyIndex * 34).dp
                            val finalOffsetY = baseOffsetY + bounceOffset

                            Box(
                                modifier = Modifier
                                    .offset(y = finalOffsetY)
                                    .size(48.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_jelly_base),
                                    contentDescription = "젤리",
                                    colorFilter = ColorFilter.tint(Color(jellyColor.rgb)),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // 3. 게임 클리어 성공 팝업 오버레이
        if (isClear) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(32.dp)
                ) {
                    Text(
                        text = "CLEAR 🎉",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4D96FF)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "축하합니다!\n모든 젤리가 알맞게 정렬되었습니다.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "총 이동 횟수: $moveCount 회",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.startGame() },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4081)
                        ),
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text(
                            text = "다시 하기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
