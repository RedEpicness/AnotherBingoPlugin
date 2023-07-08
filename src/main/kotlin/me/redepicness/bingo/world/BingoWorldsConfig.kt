package me.redepicness.bingo.world

data class BingoWorldsConfig(
        val preloading: BingoWorldPreloadConfig = BingoWorldPreloadConfig(),
)

data class BingoWorldPreloadConfig(
        val preload: Boolean = true,
        val overworldRadius: Int = 5000,
        val netherRadius: Int = 1000,
        val endRadius: Int = 200,
)
