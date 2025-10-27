package dev.sumilation.domain.pathfinding;

import dev.sumilation.domain.entity.geometry.Position;

import java.util.List;

public record PathResult(List<Position> path, Position goal) {
    public PathResult {
        if (path == null || goal == null)
            throw new IllegalArgumentException("path and goal must not be null");

        path = List.copyOf(path);
    }
}