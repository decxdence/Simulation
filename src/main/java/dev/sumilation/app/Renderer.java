package dev.sumilation.app;

import dev.sumilation.domain.creature.Carnivore;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

public final class Renderer {
    public void printMap(SimulationMap sim) {
        for (int x = 0; x < sim.getWidth(); x++) {
            for (int y = 0; y < sim.getHeight(); y++) {
                Position p = new Position(x, y);
                Entity e = sim.getEntityAt(p);
                System.out.print(symbol(e));
            }
            System.out.println();
        }
    }

    private char symbol(Entity e) {
        if (e == null) return '.';
        if (e instanceof Carnivore) return 'C';
        if (e instanceof Herbivore) return 'H';
        if (e instanceof Grass)  return 'G';
        if (e instanceof Tree)  return 'T';
        if (e instanceof Rock) return 'R';
        return '?';
    }
}