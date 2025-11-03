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
    }



    void nextTurn () {}
    void startSimulation () {}
    void stopSimulation () {}
    }