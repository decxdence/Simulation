package dev.sumilation.domain.system;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.creature.Creature;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;

import java.util.*;

public final class MovementSystem implements SystemPhase {
    @Override
    public void apply(SimulationMap sim, SimulationConfig cfg, Random rnd) {
        // 1) снимок
        List<Entity> snapshot = sim.snapshotEntities();

        // 2) планирование
        Map<Creature, Position> planned = new HashMap<>();
        for (Entity entity : snapshot) {
            if (entity instanceof Creature creature) {
                Position position = creature.planMove(sim);
                if (position != null && sim.inBounds(position.x(), position.y())) {
                    planned.put(creature, position);
                }
            }
        }

        // 3) кто куда хочет
        Map<Position, List<Creature>> wishers = new HashMap<>();
        for (var entry : planned.entrySet()) {
            wishers.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        // 4) применение
        Set<Creature> moved = new HashSet<>();

        for (var entry : wishers.entrySet()) {
            Position target = entry.getKey();
            List<Creature> list = entry.getValue();

            if (list.size() != 1) Collections.shuffle(list, rnd);

            Creature creature = list.get(0);
            if (moved.contains(creature)) continue;

            Entity stillThere = sim.getEntityAt(creature.getPosition());
            if (stillThere != creature) continue;

            Entity occupied = sim.getEntityAt(target);
            if (creature instanceof Herbivore && occupied instanceof Creature) continue;

            if (creature instanceof Predator) {
                if (occupied != null && !(occupied instanceof Herbivore || occupied instanceof dev.sumilation.domain.object.Grass)) {
                    continue;
                }
            }

            creature.applyMove(target, sim, cfg);
            moved.add(creature);
        }
    }
}