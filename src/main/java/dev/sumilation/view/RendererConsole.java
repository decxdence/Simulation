package dev.sumilation.view;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.object.Rock;
import dev.sumilation.domain.object.Tree;

public final class RendererConsole {

    public void printMap(SimulationMap sim) {
        clearConsole();

        for (int y = 0; y < sim.getHeight(); y++) {
            for (int x = 0; x < sim.getWidth(); x++) {
                Entity e = sim.getEntityAt(new Position(x, y));
                System.out.print(symbol(e));
            }
            System.out.println();
        }
    }

    private String symbol(Entity e) {
        if (e == null) return "..";
        if (e instanceof Predator)  return "🐺";
        if (e instanceof Herbivore) return "🐑";
        if (e instanceof Grass)     return "🌿";
        if (e instanceof Tree)      return "🌳";
        if (e instanceof Rock)      return "⛰";
        return "??";
    }

    private void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}