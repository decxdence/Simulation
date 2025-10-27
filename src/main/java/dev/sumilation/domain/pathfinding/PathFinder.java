package dev.sumilation.domain.pathfinding;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.geometry.Position;

import java.util.function.Predicate;

public interface PathFinder {

    /**
     * Находит путь до ближайшей цели.
     * @param start      стартовая позиция
     * @param sim        карта
     * @param isGoal     p -> true, если p является целевой клеткой
     * @param isPassable p -> true, если p проходима для агента
     * @return PathResult (путь [start..goal] и goal) или null, если цели нет
     */

    PathResult findPath(Position start,
                        SimulationMap sim,
                        Predicate<Position> isGoal,
                        Predicate<Position> isPassable);
}
