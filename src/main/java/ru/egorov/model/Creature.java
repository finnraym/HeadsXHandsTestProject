package ru.egorov.model;

public class Creature {
    protected int attack;
    protected int defense;
    protected int maxHealth;
    protected int currentHealth;
    protected int minDamage;
    protected int maxDamage;
    public Creature() {
    }
    public Creature(int attack, int defense, int health, int minDamage, int maxDamage) {
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = health;
        this.currentHealth = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        int max = maxDamage - minDamage;
        return (int) (Math.random() * ++max) + minDamage;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public boolean setAttack(int attack) {
        if(attack < 1 || attack > 30) return false;
        this.attack = attack;
        return true;
    }

    public boolean setDefense(int defense) {
        if (defense < 1 || defense > 30) return false;
        this.defense = defense;
        return true;
    }

    public boolean setHealth(int health, int max) {
        if (health < 0 || health > max) return false;
        this.maxHealth = health;
        this.currentHealth = health;
        return true;
    }

    public boolean setDamage(int minDamage, int maxDamage, int max) {
        if (minDamage < 0 || minDamage > maxDamage || maxDamage > max) return false;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        return true;
    }

    public void updateHealth(int value) {
        currentHealth -= value;
        if (currentHealth < 0) currentHealth = 0;
    }

}
