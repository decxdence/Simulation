package dev.sumilation.app;

import dev.sumilation.domain.creature.Carnivore;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

public final class Renderer {
    public void printMap(SimulationMap sim) {
        for (int y = 0; y < sim.getHeight(); y++) {
            for (int x = 0; x < sim.getWidth(); x++) {
                Position p = new Position(x, y);
                Entity e = sim.getEntityAt(p);
                System.out.print(symbol(e));
            }
            System.out.println();
        }
    }

    private String symbol(Entity e) {
        if (e == null) return "..";
        if (e instanceof Carnivore) return "\uD83D\uDC3A";
        if (e instanceof Herbivore) return "\uD83D\uDC11";
        if (e instanceof Grass)  return "\uD83C\uDF3F";
        if (e instanceof Tree)  return "\uD83C\uDF33";
        if (e instanceof Rock) return "\u26F0";
        return "?";
    }
}