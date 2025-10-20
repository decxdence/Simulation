package dev.sumilation;

import dev.sumilation.app.Renderer;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.geometry.Position;

public class Main {
    public static void main(String[] args) {
        SimulationMap sim1 = new SimulationMap(5, 5);
        Renderer r = new Renderer();
        r.printMap(sim1);
        Herbivore herb = (Herbivore) sim1.getWorldMap().get(new Position(0,0));

        long start = System.nanoTime();
        System.out.println(herb.computeNextStep(sim1));
        long end = System.nanoTime();
        long elapsed = end - start;
        System.out.println("Elapsed time: " + (elapsed / 1_000) + " s");
    }
}