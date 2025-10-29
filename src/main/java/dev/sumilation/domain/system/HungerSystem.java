package dev.sumilation.domain.system;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.creature.Creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class HungerSystem implements SystemPhase {
    @Override
    public void apply(SimulationMap sim, SimulationConfig cfg, Random rnd) {
        List<Entity> units = new ArrayList<>(sim.getWorldMap().values());
        for (Entity e : units) {
            if (e instanceof Creature c) {
                c.setHealth(c.getHealth() - cfg.hungerPerTick);
                if (c.getHealth() <= 0) {
                    sim.getWorldMap().remove(c.getPosition());
                }
            }
        }
    }
}