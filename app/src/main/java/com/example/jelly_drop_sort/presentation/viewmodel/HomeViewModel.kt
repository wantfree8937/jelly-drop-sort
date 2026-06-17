package com.example.jelly_drop_sort.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// 홈 화면의 UI 로직과 이벤트를 처리하는 뷰모델입니다.
class HomeViewModel : ViewModel() {

    // 화면 네비게이션 또는 일회성 액션을 알리기 위한 SharedFlow
    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // 게임 시작 버튼 클릭 이벤트 처리
    suspend fun onStartGameClicked() {
        _eventFlow.emit(HomeEvent.NavigateToGame)
    }
}

// 홈 화면에서 발생하는 일회성 이벤트를 정의하는 봉인 인터페이스입니다.
sealed interface HomeEvent {
    // 게임 화면으로 이동하는 이벤트
    object NavigateToGame : HomeEvent
}
