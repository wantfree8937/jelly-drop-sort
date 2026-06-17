package com.example.jelly_drop_sort.domain.model

// 젤리 색상을 정의하고 각 색상에 매칭되는 RGB 컬러 값을 가지는 열거형 클래스입니다.
enum class JellyColor(val rgb: Long) {
    RED(0xFFFF4D4D),
    BLUE(0xFF4D96FF),
    GREEN(0xFF6BCB77),
    YELLOW(0xFFFFD93D);

    companion object {
        // 인덱스로부터 안전하게 색상을 가져오는 헬퍼 메서드입니다.
        fun fromOrdinal(ordinal: Int): JellyColor {
            val vals = values()
            return vals[ordinal % vals.size]
        }
    }
}
