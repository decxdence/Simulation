package dev.sumilation.app;

import dev.sumilation.domain.creature.Creature;
import dev.sumilation.domain.creature.Herbivore;
import dev.sumilation.domain.creature.Predator;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

import java.util.*;

public class SimulationEngine {
    private final SimulationMap simMap;
    private final Random rnd = new Random();

    private static final double GRASS_SPREAD_PROB = 0.15;   // шанс распространения в соседнюю клетку
    private static final int    GRASS_MAX_NEIGHBOR_PLANTS = 2;  // максимум «детей» от одной травинки за тик
    private static final int    GRASS_RANDOM_SEEDS = 8;     // случайные попытки посева по карте за тик
    private static final double GRASS_RANDOM_PROB  = 0.35;  // шанс срабатывания случайного посева

    public SimulationEngine(SimulationMap simMap) {
        this.simMap = simMap;
    }

    public void makeTurn() {
        hungerPhase();
        movementPhase();
        reproductionPhase();
        grassGrowthPhase();


        // Тут просто дебаг
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

    private void hungerPhase() {
        List<Entity> units = new ArrayList<>(simMap.getWorldMap().values());
        for (Entity e : units) {
            if (e instanceof Creature c) {
                c.setHealth(c.getHealth() - 1);
                if (c.getHealth() <= 0) {
                    simMap.getWorldMap().remove(c.getPosition());
                }
            }
        }
    }
    private void movementPhase() {
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
    private void reproductionPhase() {
        // === 1) Буфер новорождённых ===
        List<Entity> spawns = new ArrayList<>();

        // === 2) Фаза размножения (после движения) ===
        // Текущий срез существ после всех перемещений
        List<Entity> adults = new ArrayList<>(simMap.getWorldMap().values());
        for (Entity e : adults) {
            if (!(e instanceof Creature c)) continue;

            // опционально: тикаем кулдаун раз в тик
            c.decrementReproCooldown();

            if (c.canReproduce()) {
                c.tryMakeOffspring(simMap).ifPresent(child -> {
                    spawns.add(child);
                    c.setHealth(c.getHealth() - c.reproCost());
                    c.setReproCooldown(3); // пауза на размножение
                });
            }
        }

        // === 3) Применение буфера ===
        for (Entity child : spawns) {
            Position pos = child.getPosition();
            if (simMap.getEntityAt(pos) == null) { // защита от коллизий
                simMap.getWorldMap().put(pos, child);
            }
        }
    }
    private void grassGrowthPhase() {
            // 1) Снимок и список существующей травы
            List<Entity> snapshot = new ArrayList<>(simMap.getWorldMap().values());
            List<Grass> grasses = new ArrayList<>();
            for (Entity e : snapshot) {
                if (e instanceof Grass g) grasses.add(g);
            }

            // 2) Кандидаты на посадку (без дубликатов)
            Set<Position> toPlant = new HashSet<>();

            // 3) Локальное распространение от каждой травинки (4-соседей)
            for (Grass g : grasses) {
                Position p = g.getPosition();
                int planted = 0;

                for (Direction d : Direction.values()) {
                    if (planted >= GRASS_MAX_NEIGHBOR_PLANTS) break;

                    int nx = p.x() + d.dx, ny = p.y() + d.dy;
                    if (!simMap.inBounds(nx, ny)) continue;

                    Position pos = new Position(nx, ny);
                    if (simMap.getEntityAt(pos) != null) continue;    // сажаем только в пустое

                    if (rnd.nextDouble() < GRASS_SPREAD_PROB) {
                        toPlant.add(pos);
                        planted++;
                    }
                }
            }

            // 4) Случайные посевы по карте (для регенерации при вымирании)
            for (int i = 0; i < GRASS_RANDOM_SEEDS; i++) {
                int x = rnd.nextInt(simMap.getWidth());
                int y = rnd.nextInt(simMap.getHeight());
                Position pos = new Position(x, y);
                if (simMap.getEntityAt(pos) == null && rnd.nextDouble() < GRASS_RANDOM_PROB) {
                    toPlant.add(pos);
                }
            }

            // 5) Применение посадок
            for (Position pos : toPlant) {
                if (simMap.getEntityAt(pos) == null) {
                    simMap.getWorldMap().put(pos, new Grass(pos));
                }
            }
        }



    void nextTurn () {}
    void startSimulation () {}
    void stopSimulation () {}
    }