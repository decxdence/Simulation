package dev.sumilation.app;

import dev.sumilation.domain.creature.Creature;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;

import java.util.*;

public class SimulationEngine {
    private final SimulationMap simMap;

    public SimulationEngine(SimulationMap simMap) {
        this.simMap = simMap;
    }

    public void makeTurn() {
        // Создаем копию карты. (Чтобы не менять реальную карту во избежание ошибок).
        List<Entity> snapshot = new ArrayList<>(simMap.getWorldMap().values());

        // Логика планирования хода
        Map<Creature, Position> planned = new HashMap<>();
        for (Entity e : snapshot) {
            if (e instanceof Creature c) {
                Position p = c.planMove(simMap);
                if (p != null && simMap.inBounds(p.x(), p.y())) {
                    planned.put(c, p);
                }
            }
        }

        // Карта изначальной позиции существ
        Map<Position, Entity> startOcc = new HashMap<>();
        for (Entity e : snapshot) {
            startOcc.put(e.getPosition(), e);
        }

        // Карта кто куда хочет
        Map<Position, List<Creature>> wishers = new HashMap<>();
        for (var e : planned.entrySet()) {
            Position target = e.getValue();
            Creature who = e.getKey();
            wishers.computeIfAbsent(target, k -> new ArrayList<>()).add(who);
        }

        // Применение ходов
        Set<Creature> moved = new HashSet<>();

        for (var e : wishers.entrySet()) {
            Position target = e.getKey();
            var list = e.getValue();

            if (list.size() != 1) {
                Collections.shuffle(list);
            }

            Creature c = list.get(0);
            if (moved.contains(c)) continue;

            // Если существо уже НЕ находится в своей текущей (стартовой для тика) клетке,
            // значит его съели/сместили ранее в этом же тике — пропускаем его ход.
            Entity stillThere = simMap.getWorldMap().get(c.getPosition());
            if (stillThere != c) {
                continue;
            }

            Entity occ = simMap.getEntityAt(target);
            if (c instanceof Herbivore && occ instanceof Creature) {
                continue; // травоядное не идёт в занятое
            }

            if (c instanceof Predator) {
                // хищник идёт только если в клетке либо пусто, либо овца
                if (occ != null && !(occ instanceof Herbivore)) {
                    continue;
                }
            }


            c.applyMove(target, simMap);
            moved.add(c);
        }
    }

        void nextTurn () {

        }
        void startSimulation () {
        }
        void stopSimulation () {
        }
    }