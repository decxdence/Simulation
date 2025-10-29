package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
import dev.sumilation.domain.object.Grass;
import dev.sumilation.domain.pathfinding.BFSPathFinder;
import dev.sumilation.domain.pathfinding.PathFinder;
import dev.sumilation.domain.pathfinding.PathResult;

import java.util.*;
import java.util.function.Predicate;

public abstract class Creature extends Entity {
    private final int speed;
    private int health;
    private final PathFinder pathFinder = new BFSPathFinder();
    private int reproCooldown = 0;
    public int reproCost() { return 10; }
    public int reproThreshold() { return 20; }
    public boolean canReproduce() {
        return this.getHealth() >= reproThreshold() && reproCooldown == 0;
    }


    public Creature(Position position, int speed, int health) {
        super(position);
        this.speed = speed;
        this.health = health;
    }

    public int getReproCooldown() {
        return reproCooldown;
    }
    public void setReproCooldown(int reproCooldown) {
        this.reproCooldown = reproCooldown;
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

    public void decrementReproCooldown() {
        if (reproCooldown > 0) reproCooldown--;
    }

    public Optional<Entity> tryMakeOffspring(SimulationMap sim) { return Optional.empty(); }

    public Position planMove(SimulationMap sim) {
        Predicate<Position> goal = p -> isGoalForThis(p, sim);
        Predicate<Position> pass = p -> isPassableForThis(p, sim);

        PathResult pr = pathFinder.findPath(this.getPosition(), sim, goal, pass);

        if (pr == null) return null;

        List<Position> path = pr.path();
        int dist = path.size() - 1;
        if (dist <= 0) return null;

        int k = Math.min(getSpeed(), dist);
        return choosePlannedStep(path, k, sim);
    }

    protected Position choosePlannedStep(List<Position> path, int k, SimulationMap sim) {
        return path.get(k);
    }

    protected void beforeEnter(Position target, SimulationMap sim) {
    }

    public final void applyMove(Position next, SimulationMap sim) {

        if (next == null ) return;

        beforeEnter(next, sim);

        Entity after = sim.getEntityAt(next);
            if (after != null && after != this && after instanceof Creature) {
                return;
            }

            sim.getWorldMap().remove(this.getPosition());
            this.setPosition(next);
            sim.getWorldMap().put(next, this);

        }


    protected abstract boolean isGoalForThis(Position p, SimulationMap sim);
    protected abstract boolean isPassableForThis(Position p, SimulationMap sim);
}