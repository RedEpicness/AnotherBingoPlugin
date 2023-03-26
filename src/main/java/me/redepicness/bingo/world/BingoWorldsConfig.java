package me.redepicness.bingo.world;

import org.jetbrains.annotations.NotNull;

public record BingoWorldsConfig(@NotNull BingoWorldPreloadConfig preloading) {

    public BingoWorldsConfig(){
        this(new BingoWorldPreloadConfig());
    }

    public record BingoWorldPreloadConfig(boolean preload, int overworldRadius, int netherRadius, int endRadius){

        public BingoWorldPreloadConfig(){
            this(true, 5000, 1000, 200);
        }

    }

}
