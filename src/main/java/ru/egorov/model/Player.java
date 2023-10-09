package ru.egorov.model;

public class Player extends Creature {
    private int numberOfHeal;
    public Player() {
        super();
        numberOfHeal = 4;
    }

    public int getNumberOfHeal() {
        return numberOfHeal;
    }

    public boolean heal() {
        if (numberOfHeal == 0) return false;
        currentHealth += (int) Math.round(maxHealth * 0.3);
        if (currentHealth > maxHealth) currentHealth = maxHealth;
        numberOfHeal--;
        return true;
    }
}
