package com.example.jelly_drop_sort.domain.usecase

import com.example.jelly_drop_sort.domain.model.GameDish

// 젤리 이동 규칙 유효성을 검증하고, 이동이 가능하면 새로운 접시 리스트를 반환하는 유스케이스입니다.
class MoveJellyUseCase {

    operator fun invoke(dishes: List<GameDish>, fromIndex: Int, toIndex: Int): List<GameDish>? {
        // 동일한 접시로 이동 시도 시 무시
        if (fromIndex == toIndex) return null
        // 인덱스가 유효 범위를 벗어난 경우 무시
        if (fromIndex !in dishes.indices || toIndex !in dishes.indices) return null

        val fromDish = dishes[fromIndex]
        val toDish = dishes[toIndex]

        // 1. 출발지 접시가 비어 있으면 젤리를 옮길 수 없음
        if (fromDish.isEmpty()) return null

        // 2. 목적지 접시가 가득 차 있으면 젤리를 쌓을 수 없음
        if (toDish.isFull()) return null

        val movingJelly = fromDish.peekJelly() ?: return null
        val targetJelly = toDish.peekJelly()

        // 3. 목적지 접시에 이미 젤리가 있는 상황에서 색상이 일치하지 않는 경우 이동 불가
        if (targetJelly != null && targetJelly != movingJelly) return null

        // 유효한 이동인 경우 각 접시의 상태를 불변 모델 카피(Copy)를 통해 갱신하여 반환
        return dishes.map { dish ->
            when (dish.id) {
                fromDish.id -> {
                    // 출발 접시에서는 맨 위의 젤리 하나를 제거
                    val newJellies = dish.jellies.dropLast(1)
                    dish.copy(jellies = newJellies)
                }
                toDish.id -> {
                    // 목적 접시에는 옮겨온 젤리를 마지막(맨 위)에 추가
                    val newJellies = dish.jellies + movingJelly
                    dish.copy(jellies = newJellies)
                }
                else -> dish
            }
        }
    }
}
