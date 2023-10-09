package ru.egorov;
public class GameStarter {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Добро пожаловать в игру!");
        new GameProcess().process();
        System.out.println("До свидания!");
    }
}