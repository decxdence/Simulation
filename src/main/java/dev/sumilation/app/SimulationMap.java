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
    private final Map<Position, Entity> map = new HashMap<>();

    public SimulationMap() {
        map.put(new Position(1, 1), new Grass(new Position(1, 1)));
        map.put(new Position(1, 2), new Tree(new Position(1, 2)));
        map.put(new Position(1, 3), new Carnivore(new Position(1, 3), 1, 10, 3));
        map.put(new Position(1, 4), new Herbivore(new Position(1, 4), 1, 10));
    }

    public Map<Position, Entity> getMap() {
        return map;
    }
}