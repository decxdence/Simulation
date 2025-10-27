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

    public SimulationMap(int width, int height) {
        this.width = width;
        this.height = height;

        initMap();
        // initMap2();

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
        Random rand = new Random();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int number = rand.nextInt(100) + 1;
                if (number > 1 && number < 40) {

                } else if (number >= 40 && number < 70) {
                    worldMap.put(new Position(x, y), new Grass(new Position(x, y)));
                } else if (number >= 70 && number < 84) {
                    worldMap.put(new Position(x, y), new Herbivore(new Position(x, y), 2, 10));
                } else if (number >= 84 && number < 91) {
                    worldMap.put(new Position(x, y), new Tree(new Position(x, y)));
                } else if (number >= 91 && number < 96) {
                    worldMap.put(new Position(x, y), new Predator(new Position(x, y), 3, 10, 3));}
                  else if (number >= 96 && number < 100) {
                    worldMap.put(new Position(x, y), new Rock(new Position(x, y)));
                }

            }
        }
    }

    public void initMap2() {
        worldMap.clear();

        // Карта 10x5 — удобна для отладки
        // Хищники
        worldMap.put(new Position(0, 0), new Predator(new Position(0, 0), 3, 10, 3));
        worldMap.put(new Position(9, 4), new Predator(new Position(9, 4), 3, 10, 3));

        // Травоядные — цели для волков
        worldMap.put(new Position(4, 0), new Herbivore(new Position(4, 0), 2, 10));
        worldMap.put(new Position(5, 4), new Herbivore(new Position(5, 4), 2, 10));
        worldMap.put(new Position(2, 2), new Herbivore(new Position(2, 2), 2, 10));

        // Трава — проверка на совместное нахождение
        worldMap.put(new Position(3, 0), new Grass(new Position(3, 0)));
        worldMap.put(new Position(6, 3), new Grass(new Position(6, 3)));
        worldMap.put(new Position(1, 4), new Grass(new Position(1, 4)));

        // Препятствия
        worldMap.put(new Position(5, 1), new Rock(new Position(5, 1)));
        worldMap.put(new Position(7, 2), new Tree(new Position(7, 2)));
        worldMap.put(new Position(4, 3), new Tree(new Position(4, 3)));
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