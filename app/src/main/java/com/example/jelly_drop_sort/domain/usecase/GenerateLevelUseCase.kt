package com.example.jelly_drop_sort.domain.usecase

import com.example.jelly_drop_sort.domain.model.GameDish
import com.example.jelly_drop_sort.domain.model.JellyColor

// 게임 시작 시 초기 젤리 배치 상태를 무작위로 생성하는 유스케이스입니다.
class GenerateLevelUseCase {

    operator fun invoke(): List<GameDish> {
        // 색상 3가지를 각각 4개씩 총 12개의 젤리 풀(Pool)을 만듭니다.
        // 모든 정렬이 완료되면 3개의 접시에 각각 동일한 색상의 젤리가 4개씩 모이게 됩니다.
        val targetColors = listOf(JellyColor.RED, JellyColor.BLUE, JellyColor.GREEN)
        val jellyPool = mutableListOf<JellyColor>()
        
        for (color in targetColors) {
            repeat(GameDish.MAX_JELLY_COUNT) {
                jellyPool.add(color)
            }
        }

        // 젤리들을 무작위로 섞습니다.
        jellyPool.shuffle()

        val dishes = mutableListOf<GameDish>()

        // 3개의 접시에 젤리를 4개씩 가득 채웁니다.
        for (i in 0 until 3) {
            val startIndex = i * GameDish.MAX_JELLY_COUNT
            val endIndex = startIndex + GameDish.MAX_JELLY_COUNT
            val dishJellies = jellyPool.subList(startIndex, endIndex).toList()
            dishes.add(GameDish(id = i, jellies = dishJellies))
        }

        // 2개의 빈 접시를 생성합니다 (이동 공간 용도).
        for (i in 3 until 5) {
            dishes.add(GameDish(id = i, jellies = emptyList()))
        }

        return dishes
    }
}
