package dev.sumilation.app;

public final class SimulationConfig {
    // Карта
    public final int mapWidth  = 60;
    public final int mapHeight = 30;

    // Стартовые проценты (остальное — пусто ≈ 30%)
    public final int grassChance     = 40;  // трава
    public final int herbivoreChance = 16;  // овцы
    public final int predatorChance  = 5;   // волки
    public final int treeChance      = 4;
    public final int rockChance      = 5;

    // Овцы
    public final int herbivoreSpeed           = 2;
    public final int herbivoreStartHP         = 11;
    public final int herbivoreEatGrassHp      = 3;
    public final int herbivoreReproThreshold  = 21;
    public final int herbivoreReproCost       = 10;
    public final int herbivoreReproCooldown   = 3;
    public final int herbivoreBabyHp          = 4;

    // Волки
    public final int predatorSpeed            = 3;
    public final int predatorStartHP          = 24;  // запас на «пустые» тики
    public final int predatorAttackPower      = 8;   // убивают за 1–2 удара
    public final int predatorKillBonusHp      = 6;  // убийство окупает голод
    public final int predatorReproThreshold   = 41;  // не плодятся слишком рано
    public final int predatorReproCost        = 12;
    public final int predatorReproCooldown    = 4;
    public final int predatorBabyHp           = 4;

    // Голод
    public final int hungerPerTick = 1;

    // Рост травы — меньше «куч» и больше равномерного посева
    public final double grassSpreadProb        = 0.06; // локальное расползание приглушено
    public final int    grassMaxNeighborPlants = 1;
    public final int    grassRandomSeeds       = 40;   // равномерные посевы по карте
    public final double grassRandomProb        = 0.50; // шанс удачной посадки
}