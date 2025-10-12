package dev.sumilation.app;

import dev.sumilation.domain.creature.Carnivore;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Tree;

import java.util.HashMap;
import java.util.Map;

public class SimulationMap {
    private final int height;
    private final int width;
    private final Map<Position, Entity> WorldMap = new HashMap<>();

    public SimulationMap(int height, int width) {
        this.height = height;
        this.width = width;

        WorldMap.put(new Position(0, 0), new Grass(new Position(1, 1)));
        WorldMap.put(new Position(0, 1), new Tree(new Position(1, 2)));
        WorldMap.put(new Position(1, 0), new Carnivore(new Position(1, 3), 1, 10, 3));
        WorldMap.put(new Position(1, 1), new Herbivore(new Position(1, 4), 1, 10));
    }

    public Map<Position, Entity> getWorldMap() {
        return WorldMap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Entity getEntityAt(Position p) {
        return WorldMap.get(p);
    }

}