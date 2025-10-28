package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;


import java.util.List;


public class Predator extends Creature {
    private final int attackPower;

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
    @Override protected void beforeEnter(Position target, SimulationMap sim) {
        Entity e = sim.getEntityAt(target);

        if (e instanceof Herbivore herb) {
            herb.setHealth(herb.getHealth() - this.getAttackPower());
                if (herb.getHealth() <= 0) {
                    sim.getWorldMap().remove(target);
                    this.setHealth(this.getHealth() + 3);
                }
        } else if (e instanceof Grass) {
            sim.getWorldMap().remove(target);
        }
    }
}