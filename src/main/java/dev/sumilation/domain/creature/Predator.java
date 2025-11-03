package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationConfig;
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

    @Override protected boolean isGoalForThis(Position position, SimulationMap sim) { return sim.isGoalForPredator(position); }
    @Override protected boolean isPassableForThis(Position position, SimulationMap sim) { return sim.isPassableForPredator(position); }
    @Override protected void beforeEnter(Position target, SimulationMap sim, SimulationConfig cfg) {
        Entity entity = sim.getEntityAt(target);

        if (entity instanceof Herbivore herb) {
            herb.setHealth(herb.getHealth() - this.getAttackPower());
            if (herb.getHealth() <= 0) {
                sim.removeAt(target);
                this.setHealth(this.getHealth() + cfg.predatorKillBonusHp); // +3
            }
        } else if (entity instanceof Grass) {
            sim.removeAt(target);
        }
    }
    protected Creature createOffspring(Position pos, SimulationConfig cfg) {
        return new Predator(pos, cfg.predatorSpeed, cfg.predatorBabyHp, cfg.predatorAttackPower);
    }
}