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
        List<Entity> snapshot = new ArrayList<>(sim.getWorldMap().values());

        // 2) планирование
        Map<Creature, Position> planned = new HashMap<>();
        for (Entity e : snapshot) {
            if (e instanceof Creature c) {
                Position p = c.planMove(sim);
                if (p != null && sim.inBounds(p.x(), p.y())) {
                    planned.put(c, p);
                }
            }
        }

        // 3) кто куда хочет
        Map<Position, List<Creature>> wishers = new HashMap<>();
        for (var en : planned.entrySet()) {
            wishers.computeIfAbsent(en.getValue(), k -> new ArrayList<>()).add(en.getKey());
        }

        // 4) применение
        Set<Creature> moved = new HashSet<>();

        for (var en : wishers.entrySet()) {
            Position target = en.getKey();
            List<Creature> list = en.getValue();

            if (list.size() != 1) Collections.shuffle(list, rnd);

            Creature c = list.get(0);
            if (moved.contains(c)) continue;

            Entity stillThere = sim.getWorldMap().get(c.getPosition());
            if (stillThere != c) continue;

            Entity occ = sim.getEntityAt(target);
            if (c instanceof Herbivore && occ instanceof Creature) continue;

            if (c instanceof Predator) {
                if (occ != null && !(occ instanceof Herbivore || occ instanceof dev.sumilation.domain.object.Grass)) {
                    continue;
                }
            }

            c.applyMove(target, sim);
            moved.add(c);

            // уборка павших овец
            sim.getWorldMap().entrySet().removeIf(e ->
                    e.getValue() instanceof dev.sumilation.domain.creature.Herbivore h && h.getHealth() <= 0);
        }
    }
}