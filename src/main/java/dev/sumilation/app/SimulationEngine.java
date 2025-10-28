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
        // 0) Фаза голода — единая для всех
        List<Entity> units = new ArrayList<>(simMap.getWorldMap().values());
        for (Entity e : units) {
            if (e instanceof Creature c) {
                c.setHealth(c.getHealth() - 1);
                if (c.getHealth() <= 0) {
                    simMap.getWorldMap().remove(c.getPosition());
                }
            }
        }

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
    }

        void nextTurn () {

        }
        void startSimulation () {
        }
        void stopSimulation () {
        }
    }