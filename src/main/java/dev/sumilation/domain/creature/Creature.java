package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;

import java.util.*;

public abstract class Creature extends Entity {
    private final int speed;
    private int health;

    public Creature(Position position, int speed, int health) {
        super(position);
        this.speed = speed;
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void moveTo(Position next, SimulationMap sim) {
        Entity current = sim.getEntityAt(next);

        if (current != null && current != this && current instanceof Creature) {
            return;
        }

        sim.getWorldMap().remove(this.getPosition());
        this.setPosition(next);
        sim.getWorldMap().put(next, this);
    }

    public Position planMove(SimulationMap sim) {
        Position res = bfsNextStep(sim);
        if (res == null) {
            // нет цели: можно вернуть null (стоим) или сделать 1 случайный шаг в доступного соседа
        } else if (res.equals(getPosition())) {
            System.out.println("[DBG] planMove returned START for " + this.getClass().getSimpleName() + " at " + getPosition());
        }
        return res;

    }

    public Position bfsNextStep(SimulationMap sim) {
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

            if (isGoalForThis(p, sim)) {
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
                    if (isPassableForThis(pos, sim)) {
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

    protected void beforeEnter(Position target, SimulationMap sim) {}

    public final void applyMove(Position next, SimulationMap sim) {
        if (next.equals(this.getPosition())) return;

        beforeEnter(next, sim);

        sim.getWorldMap().remove(this.getPosition());
        this.setPosition(next);
        sim.getWorldMap().put(next, this);

    }


    protected abstract boolean isGoalForThis(Position p, SimulationMap sim);
    protected abstract boolean isPassableForThis(Position p, SimulationMap sim);
}