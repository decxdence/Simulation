package dev.sumilation;

import dev.sumilation.app.Renderer;
import dev.sumilation.app.SimulationMap;

public class Main {
    public static void main(String[] args) {

        SimulationMap sim1 = new SimulationMap(10, 10);
        Renderer r = new Renderer();
        r.printMap(sim1);
    }
}