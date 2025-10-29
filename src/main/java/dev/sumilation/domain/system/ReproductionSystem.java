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

        List<Entity> adults = new ArrayList<>(sim.getWorldMap().values());
        for (Entity e : adults) {
            if (!(e instanceof Creature c)) continue;

            c.decrementReproCooldown();

            if (c instanceof Herbivore h) {
                if (h.getHealth() >= cfg.herbivoreReproThreshold && h.getReproCooldown() == 0) {
                    h.tryMakeOffspring(sim, cfg).ifPresent(child -> {
                        spawns.add(child);
                        h.setHealth(h.getHealth() - cfg.herbivoreReproCost);
                        h.setReproCooldown(cfg.herbivoreReproCooldown);
                    });
                }
            } else if (c instanceof Predator p) {
                if (p.getHealth() >= cfg.predatorReproThreshold && p.getReproCooldown() == 0) {
                    p.tryMakeOffspring(sim, cfg).ifPresent(child -> {
                        spawns.add(child);
                        p.setHealth(p.getHealth() - cfg.predatorReproCost);
                        p.setReproCooldown(cfg.predatorReproCooldown);
                    });
                }
            }
        }

        for (Entity child : spawns) {
            Position pos = child.getPosition();
            if (sim.getEntityAt(pos) == null) {
                sim.getWorldMap().put(pos, child);
            }
        }
    }
}