package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.Entity;
import dev.sumilation.domain.entity.geometry.Position;

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
        sim.getWorldMap().remove(this.getPosition());
        this.setPosition(next);
        sim.getWorldMap().put(next, this);
    }

    public void makeMove(SimulationMap sim) {
    }
}