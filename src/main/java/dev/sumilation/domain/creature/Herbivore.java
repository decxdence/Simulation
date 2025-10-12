package dev.sumilation.domain.creature;

import dev.sumilation.domain.entity.Position;

public class Herbivore extends Creature {

    public Herbivore(Position position, int speed, int health) {
        super(position, speed, health);
    }

    @Override
    void makeMove() {
        // Move to the side with grass
        // If grass is close consume it
    }
}