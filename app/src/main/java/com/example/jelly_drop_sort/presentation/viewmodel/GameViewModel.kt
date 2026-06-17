package com.example.jelly_drop_sort.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jelly_drop_sort.domain.model.GameDish
import com.example.jelly_drop_sort.domain.usecase.GenerateLevelUseCase
import com.example.jelly_drop_sort.domain.usecase.MoveJellyUseCase

// 컬러 소트 본 게임의 흐름과 상태를 총괄하는 뷰모델입니다.
class GameViewModel : ViewModel() {

    private val generateLevelUseCase = GenerateLevelUseCase()
    private val moveJellyUseCase = MoveJellyUseCase()

    // 접시 목록 상태
    private val _dishes = mutableStateOf<List<GameDish>>(emptyList())
    val dishes: State<List<GameDish>> = _dishes

    // 현재 선택된 접시의 인덱스 (선택되지 않은 경우 null)
    private val _selectedDishIndex = mutableStateOf<Int?>(null)
    val selectedDishIndex: State<Int?> = _selectedDishIndex

    // 총 이동 횟수
    private val _moveCount = mutableStateOf(0)
    val moveCount: State<Int> = _moveCount

    // 게임 성공(클리어) 여부
    private val _isGameClear = mutableStateOf(false)
    val isGameClear: State<Boolean> = _isGameClear

    init {
        startGame()
    }

    // 새로운 게임 시작 (상태 초기화 및 레벨 셔플 생성)
    fun startGame() {
        _dishes.value = generateLevelUseCase()
        _selectedDishIndex.value = null
        _moveCount.value = 0
        _isGameClear.value = false
    }

    // 사용자가 접시를 클릭했을 때 처리되는 이벤트 핸들러
    fun onDishClicked(index: Int) {
        if (_isGameClear.value) return // 이미 게임을 클리어했다면 클릭 무시

        val currentSelected = _selectedDishIndex.value

        if (currentSelected == null) {
            // 아무 접시도 선택되지 않은 상태: 젤리가 담겨 있는 접시만 첫 선택이 가능함
            if (index in _dishes.value.indices && !_dishes.value[index].isEmpty()) {
                _selectedDishIndex.value = index
            }
        } else {
            // 이미 하나의 접시가 선택된 상태
            if (currentSelected == index) {
                // 동일한 접시를 다시 탭하면 선택 취소
                _selectedDishIndex.value = null
            } else {
                // 다른 접시를 탭한 경우 젤리 이동을 시도
                val newDishes = moveJellyUseCase(_dishes.value, currentSelected, index)
                if (newDishes != null) {
                    _dishes.value = newDishes
                    _moveCount.value += 1
                    _selectedDishIndex.value = null
                    checkGameClear()
                } else {
                    // 이동이 불가능한 규칙일 경우, 클릭한 다른 접시에 젤리가 존재한다면
                    // 해당 접시로 선택을 변경하여 UX 편의성 제공
                    if (index in _dishes.value.indices && !_dishes.value[index].isEmpty()) {
                        _selectedDishIndex.value = index
                    } else {
                        _selectedDishIndex.value = null
                    }
                }
            }
        }
    }

    // 모든 접시가 올바르게 정렬되었는지 점검
    private fun checkGameClear() {
        _isGameClear.value = _dishes.value.all { it.isSorted() }
    }
}
