package dev.sumilation.app;

public final class SimulationConfig {
    // Голод
    public final int hungerPerTick = 1;

    // Трава (рост)
    public final double grassSpreadProb = 0.20;
    public final int grassMaxNeighborPlants = 2;
    public final int grassRandomSeeds = 10;
    public final double grassRandomProb = 0.25;

    // Овцы (Herbivore)
    public final int herbivoreEatGrassHp = 2;
    public final int herbivoreReproThreshold = 20;
    public final int herbivoreReproCost = 10;
    public final int herbivoreReproCooldown = 2;
    public final int herbivoreBabyHp = 4;

    // Волки (Predator)
    public final int predatorKillBonusHp = 3;
    public final int predatorReproThreshold = 30;
    public final int predatorReproCost = 12;
    public final int predatorReproCooldown = 3;
    public final int predatorBabyHp = 4;
}