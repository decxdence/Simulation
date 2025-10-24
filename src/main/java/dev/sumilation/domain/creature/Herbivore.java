package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

import java.util.*;

public class Herbivore extends Creature {

    public Herbivore(Position position, int speed, int health) {
        super(position, speed, health);
    }


    @Override protected boolean isGoalForThis(Position p, SimulationMap sim) { return sim.isGoalForHerbivore(p); }
    @Override protected boolean isPassableForThis(Position p, SimulationMap sim) { return sim.isPassableForHerbivore(p); }
    @Override protected void beforeEnter(Position target, SimulationMap sim) {
        Entity e = sim.getEntityAt(target);
        if (e instanceof Grass) {
            sim.getWorldMap().remove(target);
        }
    }
}

