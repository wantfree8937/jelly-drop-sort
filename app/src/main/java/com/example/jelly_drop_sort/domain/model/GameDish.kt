package com.example.jelly_drop_sort.domain.model

// 각 접시의 정보를 나타내는 도메인 모델입니다.
// 한 접시는 최대 4개의 젤리를 가질 수 있습니다.
data class GameDish(
    val id: Int,
    // 접시에 쌓여있는 젤리들의 리스트 (리스트의 마지막 원소가 맨 위에 노출되는 젤리입니다)
    val jellies: List<JellyColor> = emptyList()
) {
    companion object {
        const val MAX_JELLY_COUNT = 4
    }

    // 접시가 꽉 찼는지 확인합니다.
    fun isFull(): Boolean = jellies.size >= MAX_JELLY_COUNT

    // 접시가 비어있는지 확인합니다.
    fun isEmpty(): Boolean = jellies.isEmpty()

    // 맨 위에 위치한 젤리를 확인합니다. (비어있으면 null)
    fun peekJelly(): JellyColor? = jellies.lastOrNull()

    // 이 접시의 젤리들이 완전히 정렬되었는지 확인합니다.
    // 완전히 비어있거나, 젤리가 최대치(4개)로 꽉 차 있고 모두 동일한 색상일 경우에 참을 반환합니다.
    fun isSorted(): Boolean {
        if (isEmpty()) return true
        if (jellies.size < MAX_JELLY_COUNT) return false
        val firstColor = jellies.first()
        return jellies.all { it == firstColor }
    }
}
