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
        List<Entity> snapshot = sim.snapshotEntities();
        for (Entity entity : snapshot) {
            if (entity instanceof Creature creature) {
                creature.setHealth(creature.getHealth() - cfg.hungerPerTick);
                if (creature.getHealth() <= 0) {
                    sim.removeAt(creature.getPosition());
                }
            }
        }
    }
}