package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class Predator extends Creature {
    private final int attackPower;
    public int reproThreshold() { return 25; }

    public Predator(Position position, int speed, int health, int attackPower) {
        super(position, speed, health);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    protected Position choosePlannedStep(List<Position> path, int k, SimulationMap sim) {
        int dist = path.size() - 1;         // путь [start..goal], dist = кол-во рёбер
        if (k == dist && dist > 1) {
            // не позволяем «прыжок» сквозь поле с атакой на дистанции
            return path.get(dist - 1);      // подходим вплотную
        }
        return path.get(k);                  // обычный шаг
    }

    @Override protected boolean isGoalForThis(Position p, SimulationMap sim) { return sim.isGoalForPredator(p); }
    @Override protected boolean isPassableForThis(Position p, SimulationMap sim) { return sim.isPassableForPredator(p); }
    @Override protected void beforeEnter(Position target, dev.sumilation.app.SimulationMap sim) {
        Entity e = sim.getEntityAt(target);

        if (e instanceof Herbivore herb) {
            herb.setHealth(herb.getHealth() - this.getAttackPower());
            if (herb.getHealth() <= 0) {
                sim.getWorldMap().remove(target);
                this.setHealth(this.getHealth() + new SimulationConfig().predatorKillBonusHp); // +3
            }
        } else if (e instanceof Grass) {
            sim.getWorldMap().remove(target);
        }
    }
    @Override public Optional<Entity> tryMakeOffspring(SimulationMap sim, SimulationConfig cfg) {
        List<Direction> dirs = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(dirs);

        Position p = this.getPosition();
        for (Direction d : dirs) {
            int nx = p.x() + d.dx, ny = p.y() + d.dy;
            if (!sim.inBounds(nx, ny)) continue;

            Position pos = new Position(nx, ny);
            if (sim.getEntityAt(pos) == null) {
                return Optional.of(new Predator(pos, 3, cfg.predatorBabyHp, 8));
            }
        }
        return Optional.empty();
    }
}