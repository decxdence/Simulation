package dev.sumilation.domain.creature;

import dev.sumilation.app.SimulationMap;
import dev.sumilation.domain.entity.geometry.Position;

public class Carnivore extends Creature {
    private final int attackPower;

    public Carnivore(Position position, int speed, int health, int attackPower) {
        super(position, speed, health);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    void makeMove(SimulationMap sim) {
        // Move to prey
        // If can bite --> bite prey (deal damage (attackPower)
        // if prey has 0 hp --> death
    }
}