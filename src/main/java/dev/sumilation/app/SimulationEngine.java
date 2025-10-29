package dev.sumilation.app;

import dev.sumilation.domain.system.SystemPhase;
import dev.sumilation.domain.system.HungerSystem;
import dev.sumilation.domain.system.MovementSystem;
import dev.sumilation.domain.system.ReproductionSystem;
import dev.sumilation.domain.system.VegetationSystem;
import dev.sumilation.app.SimulationConfig;

import java.util.*;

public class SimulationEngine {
    private final SimulationMap simMap;
    private final SimulationConfig cfg = new SimulationConfig();
    private final Random rnd = new Random();
    private final List<SystemPhase> pipeline = List.of(
            new HungerSystem(),
            new MovementSystem(),
            new ReproductionSystem(),
            new VegetationSystem()
    );

    public SimulationEngine(SimulationMap simMap) {
        this.simMap = simMap;
    }

    public void makeTurn() {
        for (SystemPhase s : pipeline) {
            s.apply(simMap, cfg, rnd);
        }


        /* Тут просто дебаг
        System.out.println("== HP овец на данный момент ==");
        simMap.getWorldMap().values().stream()
                .filter(eboy -> eboy instanceof Herbivore)
                .map(eboy -> (Herbivore) eboy)
                .forEach(h -> System.out.printf("🐑 at %s → HP: %d%n", h.getPosition(), h.getHealth()));
        System.out.println();

        System.out.println("== HP волков на данный момент ==");
        simMap.getWorldMap().values().stream()
                .filter(eboy -> eboy instanceof Predator)
                .map(eboy -> (Predator) eboy)
                .forEach(p -> System.out.printf("\uD83D\uDC3A at %s → HP: %d%n", p.getPosition(), p.getHealth()));
        System.out.println();

         */
    }



    void nextTurn () {}
    void startSimulation () {}
    void stopSimulation () {}
    }