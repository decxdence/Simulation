package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

import java.util.*;

public class Herbivore extends Creature {

    public Herbivore(Position position, int speed, int health) {
        super(position, speed, health);
    }


    @Override protected boolean isGoalForThis(Position position, SimulationMap sim) { return sim.isGoalForHerbivore(position); }
    @Override protected boolean isPassableForThis(Position position, SimulationMap sim) { return sim.isPassableForHerbivore(position); }
    @Override protected void beforeEnter(Position target, SimulationMap sim, SimulationConfig cfg) {
        Entity entity = sim.getEntityAt(target);
        if (entity instanceof Grass) {
            sim.removeAt(target);
            this.setHealth(this.getHealth() + cfg.herbivoreEatGrassHp); // +2
        }
    }
    @Override protected Creature createOffspring(Position pos, SimulationConfig cfg) {
        return new Herbivore(pos, cfg.herbivoreSpeed, cfg.herbivoreBabyHp);
    }
}

