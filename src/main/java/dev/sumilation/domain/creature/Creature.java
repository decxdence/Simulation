package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationConfig;
import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;
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

    // Метод для размножения
    public Optional<Entity> tryMakeOffspring(SimulationMap sim, SimulationConfig cfg) {

        List<Direction> dirs = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(dirs);

        Position position = this.getPosition();
        for (Direction d : dirs) {
            int nx = position.x() + d.dx, ny = position.y() + d.dy;
            if (!sim.inBounds(nx, ny)) continue;

            Position pos = new Position(nx, ny);
            if (sim.getEntityAt(pos) == null) {
                return Optional.of(createOffspring(pos, cfg));
            }
        }
        return Optional.empty();
    }
    protected abstract Creature createOffspring(Position pos, SimulationConfig cfg);
    // Планирование хода. Тут поиск пути
    public Position planMove(SimulationMap sim) {
        Predicate<Position> goal = p -> isGoalForThis(p, sim);
        Predicate<Position> pass = p -> isPassableForThis(p, sim);

        PathResult pathResult = pathFinder.findPath(this.getPosition(), sim, goal, pass);

        if (pathResult == null) return null;

        List<Position> path = pathResult.path();
        int dist = path.size() - 1;
        if (dist <= 0) return null;

        int k = Math.min(getSpeed(), dist);
        return choosePlannedStep(path, k, sim);
    }
    protected Position choosePlannedStep(List<Position> path, int k, SimulationMap sim) {
        return path.get(k);
    }
    // Применение хода
    public final void applyMove(Position next, SimulationMap sim, SimulationConfig cfg) {

        if (next == null ) return;

        beforeEnter(next, sim, cfg);

        Entity after = sim.getEntityAt(next);
        if (after != null && after != this && after instanceof Creature) {
            return;
        }

        sim.moveTo(this, next);

    }
    protected void beforeEnter(Position target, SimulationMap sim, SimulationConfig cfg) {
    }
    // Проверка цели и препятствий
    protected abstract boolean isGoalForThis(Position position, SimulationMap sim);
    protected abstract boolean isPassableForThis(Position position, SimulationMap sim);
}