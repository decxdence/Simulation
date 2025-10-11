package dev.sumilation.domain.creature;

public class Herbivore extends Creature {

    public Herbivore(int speed, int health) {
        super(speed, health);
    }

    @Override
    void makeMove() {
        // Move to the side with grass
        // If grass is close consume it
    }
}