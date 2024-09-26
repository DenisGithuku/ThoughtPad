package com.gitsoft.thoughtpad.model

enum class ThemeConfig {
    LIGHT,
    DARK,
    SYSTEM
}

class ThemeConfigConverter {
    companion object {
        fun fromThemeConfig(value: ThemeConfig): String {
            return value.name
        }

        fun toThemeConfig(value: String): ThemeConfig {
            return ThemeConfig.valueOf(value)
        }
    }
}