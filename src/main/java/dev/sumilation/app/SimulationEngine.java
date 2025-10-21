package dev.sumilation.app;

import dev.sumilation.domain.creature.Creature;
import dev.sumilation.domain.entity.Entity;

import java.util.ArrayList;

public class SimulationEngine {
    private final SimulationMap simMap;

    public SimulationEngine(SimulationMap simMap) {
        this.simMap = simMap;
    }

    public void makeTurn() {
        for (Entity e : new ArrayList<>(simMap.getWorldMap().values())) {
            if  (e instanceof Creature creature) {
                creature.makeMove(simMap);
            }
        }
    }

    void nextTurn() {

    }

    void startSimulation() {
    }

    void stopSimulation() {
    }
}