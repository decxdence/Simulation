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


    @Override
    public void makeMove(SimulationMap sim) {
        Position next = computeNextStep(sim);

        if (next == null) {
            return;
        }

        if (sim.getEntityAt(next) instanceof Grass) {
            sim.getWorldMap().remove(next);

        }

        this.moveTo(next, sim);

    }

    public Position computeNextStep(SimulationMap sim) {
        Deque<Position> initQueue = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parent = new HashMap<>();

        Position start = this.getPosition();

        initQueue.addLast(start);
        visited.add(start);
        parent.put(start, null);

        Direction[] dirs = Direction.values();

        while (!initQueue.isEmpty()) {
            Position p = initQueue.pollFirst();

            if (sim.isGoalForHerbivore(p)) {
                if (p.equals(start)) {
                    return p;
                } else {
                    Position step = p;
                    Position parentStep = parent.get(step);

                    while (parentStep != null && !parentStep.equals(start)) {
                        step = parentStep;
                        parentStep = parent.get(step);
                    }
                    return step;
                }
            }

            int x = p.x();
            int y = p.y();


            for (Direction dir : dirs) {

                if (sim.inBounds(x + dir.dx, y + dir.dy)) {
                    Position pos = new Position(x + dir.dx, y + dir.dy);
                    if (sim.isPassableForHerbivore(pos)) {
                        if (visited.add(pos)) {
                            parent.put(pos, p);
                            initQueue.addLast(pos);
                        }
                    }
                }
            }
        }
        return null;
    }
}

