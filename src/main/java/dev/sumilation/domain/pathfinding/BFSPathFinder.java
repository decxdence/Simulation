package dev.sumilation.domain.pathfinding;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.geometry.Direction;
import dev.sumilation.domain.entity.geometry.Position;

import java.util.*;
import java.util.function.Predicate;

public final class BFSPathFinder implements PathFinder{

    @Override
    public PathResult findPath(Position start,
                      SimulationMap sim,
                      Predicate<Position> isGoal,
                      Predicate<Position> isPassable) {

            Deque<Position> initQueue = new ArrayDeque<>();
            Set<Position> visited = new HashSet<>();
            Map<Position, Position> parent = new HashMap<>();


            initQueue.addLast(start);
            visited.add(start);
            parent.put(start, null);

            Position goal = null;
            Direction[] dirs = Direction.values();

            while (!initQueue.isEmpty()) {
                Position p = initQueue.pollFirst();

                if (isGoal.test(p)) {
                    goal = p;
                    break;
                }

                int x = p.x(), y = p.y();


                for (Direction dir : dirs) {

                    if (!sim.inBounds(x + dir.dx, y + dir.dy)) continue;

                    Position pos = new Position(x + dir.dx, y + dir.dy);
                    if (!isPassable.test(pos)) {
                        if (!isGoal.test(pos)) continue;
                    }
                    if (!visited.add(pos)) continue;

                    parent.put(pos, p);
                    initQueue.addLast(pos);
                }
            }

            if (goal == null) return null;

            List<Position> path = new ArrayList<>();
            for (Position current = goal; current != null; current = parent.get(current)) {
                path.add(current);
            }

            Collections.reverse(path);
            return new PathResult(path, goal);
    }
}