package dev.sumilation;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationEngine;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.view.RendererConsole;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SimulationConfig cfg = new SimulationConfig();
        SimulationMap sim = new SimulationMap(cfg.mapWidth, cfg.mapHeight, cfg);
        SimulationEngine engine = new SimulationEngine(sim);
        RendererConsole renderer = new RendererConsole();

        while (true) {
            engine.makeTurn();
            renderer.printMap(sim);
            Thread.sleep(1500); // подбери по вкусу
        }
    }
}