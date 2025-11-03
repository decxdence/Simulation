package dev.sumilation.domain.system;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Creature;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;

import java.util.*;

public final class ReproductionSystem implements SystemPhase {
    @Override
    public void apply(SimulationMap sim, SimulationConfig cfg, Random rnd) {
        List<Entity> spawns = new ArrayList<>();

        List<Entity> adults = sim.snapshotEntities();
        for (Entity entity : adults) {
            if (!(entity instanceof Creature creature)) continue;

            creature.decrementReproCooldown();

            if (creature instanceof Herbivore herbivore) {
                if (herbivore.getHealth() >= cfg.herbivoreReproThreshold && herbivore.getReproCooldown() == 0) {
                    herbivore.tryMakeOffspring(sim, cfg).ifPresent(child -> {
                        spawns.add(child);
                        herbivore.setHealth(herbivore.getHealth() - cfg.herbivoreReproCost);
                        herbivore.setReproCooldown(cfg.herbivoreReproCooldown);
                    });
                }
            } else if (creature instanceof Predator predator) {
                if (predator.getHealth() >= cfg.predatorReproThreshold && predator.getReproCooldown() == 0) {
                    predator.tryMakeOffspring(sim, cfg).ifPresent(child -> {
                        spawns.add(child);
                        predator.setHealth(predator.getHealth() - cfg.predatorReproCost);
                        predator.setReproCooldown(cfg.predatorReproCooldown);
                    });
                }
            }
        }

        for (Entity child : spawns) {
            Position pos = child.getPosition();
            if (sim.getEntityAt(pos) == null) {
                sim.putAt(pos, child);
            }
        }
    }
}