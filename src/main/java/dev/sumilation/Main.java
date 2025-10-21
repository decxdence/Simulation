package dev.sumilation;

import dev.sumilation.app.Renderer;
import dev.sumilation.app.SimulationEngine;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.entity.geometry.Position;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SimulationMap sim1 = new SimulationMap(25, 5);
        Renderer r = new Renderer();


        SimulationEngine se = new SimulationEngine(sim1);

        while (true) {
            r.clearConsole();
            r.printMap(sim1);
            se.makeTurn();
            Thread.sleep(1500); // задержка 0.5 сек
        }



    }
}