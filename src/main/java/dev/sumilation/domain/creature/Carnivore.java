package dev.sumilation.domain.creature;

public class Carnivore extends Creature {
    private final int attackPower;

    public Carnivore(int speed, int health, int attackPower) {
        super(speed, health);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    void makeMove() {
        // Move to prey
        // If can bite --> bite prey (deal damage (attackPower)
        // if prey has 0 hp --> death
    }
}