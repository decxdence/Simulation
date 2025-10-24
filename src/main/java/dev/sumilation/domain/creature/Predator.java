package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

public class Predator extends Creature {
    private final int attackPower;

    public Predator(Position position, int speed, int health, int attackPower) {
        super(position, speed, health);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }


    @Override protected boolean isGoalForThis(Position p, SimulationMap sim) { return sim.isGoalForPredator(p); }
    @Override protected boolean isPassableForThis(Position p, SimulationMap sim) { return sim.isPassableForPredator(p); }
    @Override protected void beforeEnter(Position target, SimulationMap sim) {
        Entity e = sim.getEntityAt(target);
        if (e instanceof Herbivore) {
            sim.getWorldMap().remove(target);
        } else if (e instanceof Grass) {
            sim.getWorldMap().remove(target);
        }
    }
}