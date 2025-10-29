package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
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
            this.setHealth(this.getHealth() + 2);
        }
    }
    @Override public Optional<Entity> tryMakeOffspring(SimulationMap sim) {
        if (this.getHealth() < reproThreshold()) return Optional.empty();

        List<Direction> dirs = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(dirs);

        Position p = this.getPosition();
        for (Direction d : dirs) {
            int nx = p.x() + d.dx, ny = p.y() + d.dy;
            if (!sim.inBounds(nx, ny)) continue;

            Position pos = new Position(nx, ny);
            if (sim.getEntityAt(pos) != null) continue;

            return Optional.of(new Herbivore(pos, 2, 4));
        }

        return Optional.empty();
    }
}

