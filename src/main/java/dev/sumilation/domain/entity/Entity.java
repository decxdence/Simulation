package dev.sumilation.domain.entity;

import dev.sumilation.domain.entity.geometry.Position;

public abstract class Entity {
    private Position position;

    public Entity(Position position) {
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }

}