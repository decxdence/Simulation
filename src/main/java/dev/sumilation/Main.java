package dev.sumilation;

import dev.sumilation.app.Renderer;
import dev.sumilation.app.SimulationEngine;
import dev.sumilation.app.SimulationMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SimulationMap sim1 = new SimulationMap(25, 5);
        Renderer r = new Renderer();
        SimulationEngine se = new SimulationEngine(sim1);

        while (true) {
            se.makeTurn();
            r.printMap(sim1);
            r.clearConsole();
            Thread.sleep(150 ); // задержка 0.5 сек
        }





    }
}