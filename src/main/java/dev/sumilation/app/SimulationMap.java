package dev.sumilation.app;

import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SimulationMap {
    private final int width;
    private final int height;
    private final Map<Position, Entity> worldMap = new HashMap<>();
    private final SimulationConfig cfg;     // ← конфиг
    private final Random rand = new Random();

    public SimulationMap(int width, int height, SimulationConfig cfg) {
        this.width = width;
        this.height = height;
        this.cfg = cfg;
        initMap();
    }
    public SimulationMap(int width, int height) {
        this(width, height, new SimulationConfig());
    }

    public Map<Position, Entity> getWorldMap() {
        return worldMap;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Entity getEntityAt(Position p) {
        return worldMap.get(p);
    }

    public void initMap() {
        worldMap.clear();

        // Пороговые интервалы по процентам из cfg
        // Пустые клетки = остаток до 100
        int grassEnd     = 30 + cfg.grassChance;                       // пусто ~30%
        int herbEnd      = grassEnd + cfg.herbivoreChance;
        int predEnd      = herbEnd  + cfg.predatorChance;
        int treeEnd      = predEnd  + cfg.treeChance;
        int rockEnd      = treeEnd  + cfg.rockChance;

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int r = rand.nextInt(100) + 1; // 1..100
                Position p = new Position(x, y);

                if (r <= 30) {
                    // пусто ~30%
                } else if (r <= grassEnd) {
                    worldMap.put(p, new Grass(p));
                } else if (r <= herbEnd) {
                    worldMap.put(p, new Herbivore(p, cfg.herbivoreSpeed, cfg.herbivoreStartHP));
                } else if (r <= predEnd) {
                    worldMap.put(p, new Predator(p, cfg.predatorSpeed, cfg.predatorStartHP, cfg.predatorAttackPower));
                } else if (r <= treeEnd) {
                    worldMap.put(p, new Tree(p));
                } else if (r <= rockEnd) {
                    worldMap.put(p, new Rock(p));
                } else {
                    // если суммы процентов < 70 (вдруг менял cfg) — остаток тоже пусто
                }
            }
        }
    }

    public boolean inBounds(int x, int y) {
        return (0 <= x) && (x < width) && (0 <= y) && (y < height);
    }
    public boolean isPassableForHerbivore(Position p) {
        Entity e = getEntityAt(p);
        return e == null || (e instanceof Grass);
    }
    public boolean isPassableForPredator(Position p) {
        Entity e = getEntityAt(p);
        // было: return e == null || (e instanceof Grass) || (e instanceof Herbivore);
        return e == null || (e instanceof Grass);
    }
    public boolean isGoalForHerbivore(Position p) {
        Entity e = getEntityAt(p);
        return e instanceof Grass;
    }
    public boolean isGoalForPredator(Position p) {
        Entity e = getEntityAt(p);
        return e instanceof Herbivore;
    }

}