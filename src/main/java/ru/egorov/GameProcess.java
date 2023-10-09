package ru.egorov;

import ru.egorov.model.Creature;
import ru.egorov.model.Monster;
import ru.egorov.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameProcess {
    private final Player player;
    private final List<Monster> monsters;

    public GameProcess() {
        player = new Player();
        monsters = new ArrayList<>();
    }
    public void process() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Хотите прочитать правила игры?");
        System.out.println("1 - Да\n0 - Нет");

        int answer = scanner.nextInt();
        if (answer == 1) showGameRules();

        createPlayerProcess(scanner);
        createMonstersProcess(scanner);
        System.out.println("Хотите посмотреть характеристики монстров?");
        System.out.println("1 - Да\n0 - Нет");
        answer = scanner.nextInt();
        if (answer == 1)  {
            showMonsterСharacteristics();
            Thread.sleep(3000);
        }

        System.out.println("Игра начинается!");
        System.out.println("===============================");

        boolean gameOver = false;
        int numberOfMonster = 0;
        Creature attacker = player;
        Creature defender = monsters.get(numberOfMonster);
        while(!gameOver) {

            boolean processResult = progress(attacker, defender);
            Thread.sleep(1000);
            if (processResult) {
                if (defender instanceof Player) {
                    int numberOfHeal = ((Player) defender).getNumberOfHeal();
                    if (numberOfHeal > 0) {
                        System.out.println("Игрок может себя исцелить. Сделать это?");
                        System.out.println("Да - 1");
                        System.out.println("Нет - 2");
                        String choice = new Scanner(System.in).nextLine();
                        if (choice.equals("1")) {
                            ((Player) defender).heal();
                            System.out.println("Игрок исцелил себя! Теперь у него " + defender.getCurrentHealth() + " здоровья и " + (numberOfHeal - 1) + " количество исцелений!");
                        }
                    }

                    if (!defender.isAlive()) {
                        System.out.println("Игрок погибает. К сожалению игра окончена...");
                        gameOver = true;
                    }
                } else {
                    System.out.println("Монстр под номером " + (numberOfMonster + 1) + " погибает!");
                    numberOfMonster++;
                    if (numberOfMonster >= monsters.size()) {
                        System.out.println("Все монстры повержены. Вы победили!!!");
                        gameOver = true;
                    } else {
                        defender = monsters.get(numberOfMonster);
                    }
                }
            }
            Creature tmp = attacker;
            attacker = defender;
            defender = tmp;
            Thread.sleep(2000);
        }


        scanner.close();
    }

    private void showMonsterСharacteristics() {
        for (int i = 0; i < monsters.size(); i++) {
            Monster currentMonster = monsters.get(i);
            System.out.println("Монстр под номером " + (i + 1));
            System.out.println("Здоровье: " + currentMonster.getMaxHealth());
            System.out.println("Атака: " + currentMonster.getAttack());
            System.out.println("Защита: " + currentMonster.getDefense());
            System.out.println("Урон: " + currentMonster.getMinDamage() + "-" + currentMonster.getMaxDamage());
        }
    }

    private boolean diceRoll(int modifier) {
        while (modifier > 0) {
            int thr = (int) (Math.random() * 6) + 1;
            if (thr >= 5) return true;
            modifier--;
        }
        return false;
    }

    private int getAttackModifier(Creature attacker, Creature defender) {
        int modifier = attacker.getAttack() - (defender.getDefense() + 1);
        return modifier > 0 ? modifier : 1;
    }

    private boolean progress(Creature attacker, Creature defender) throws InterruptedException {
        if (attacker.getClass() == Player.class) {
            System.out.println("Ход игрока...");
        } else {
            System.out.println("Ход монстров...");
        }
        Thread.sleep(1000);

        System.out.println("Рассчитываем модификатор атаки...");
        Thread.sleep(1000);
        int modifier = getAttackModifier(attacker, defender);
        System.out.println("Бросаем кубик...");
        Thread.sleep(1000);

        if (diceRoll(modifier)) {
            int damage = attacker.getDamage();
            defender.updateHealth(damage);
            System.out.println("Удача! Атакующий нанес " + damage + " единиц урона.");
            if (defender.isAlive()) {
                System.out.println("У защищающегося осталось " + defender.getCurrentHealth() + " единиц здоровья.");
            } else {
                return true;
            }
        } else {
            System.out.println("Неудача. Ход переходит противнику.");
        }

        return false;
    }

    private void createMonstersProcess(Scanner scanner) {
        System.out.println("Теперь нужно создать монстров которые будут вам противостоять.");
        System.out.println("Сколько монстров вы хотите создать? Введите одно число больше 0.");
        int countMonsters = scanner.nextInt();
        while (countMonsters <= 0) {
            System.out.println("Попробуйте еще раз:");
            countMonsters = scanner.nextInt();
        }

        System.out.println("Создаем монстров...");
        for (int i = 0; i < countMonsters; i++) {
            Monster monster = new Monster();
            while (!monster.setAttack((int) (Math.random() * 30) + 1)) ;
            while (!monster.setDefense((int) (Math.random() * 30) + 1)) ;
            while (!monster.setHealth((int) (Math.random() * 100) + 1, 100)) ;

            int minDamage, maxDamage;
            do {
                minDamage = (int) (Math.random() * 99) + 1;
                maxDamage = (int) (Math.random() * (100 - minDamage)) + minDamage;
            } while (!monster.setDamage(minDamage, maxDamage, 100));

            monsters.add(monster);
        }
        System.out.println("Монстры созданы!");
    }

    private void createPlayerProcess(Scanner scanner) {
        System.out.println("Для начала нужно создать игрока.");

        System.out.println("Введите значение Атаки:");
        int attack = scanner.nextInt();
        while (!player.setAttack(attack)) {
            System.out.println("К сожалению Атака не может быть такой. Введите одно число от 1 до 30:");
            attack = scanner.nextInt();
        }
        System.out.println("Введите значение Защиты:");
        int defense = scanner.nextInt();
        while (!player.setDefense(defense)) {
            System.out.println("К сожалению Защита не может быть такой. Введите одно число от 1 до 30:");
            defense = scanner.nextInt();
        }

        System.out.println("Введите значение Здоровья:");
        int health = scanner.nextInt();
        while (!player.setHealth(health, 100)) {
            System.out.println("К сожалению Здоровье не может быть таким. Введите одно число от 1 до 100:");
            health = scanner.nextInt();
        }

        System.out.println("Теперь введите через пробел 2 значения минимального и максимального урона от 1 до 100:");
        int minDamage = scanner.nextInt();
        int maxDamage = scanner.nextInt();
        while (!player.setDamage(minDamage, maxDamage, 100)) {
            System.out.println("К сожалению значения урона не могут быть такими. Попробуйте еще раз.");
            System.out.println("Помните что минимальный урон должен быть меньше максимального.");
            minDamage = scanner.nextInt();
            maxDamage = scanner.nextInt();
        }

        System.out.println("Игрок создан!");
    }

    private void showGameRules() {
        System.out.println("В игре есть Существа. К ним относятся Игрок и Монстры.");
        System.out.println("Все существа в игре обладают следующими характеристиками:");
        System.out.println("Атака и Защита - это целые числа от 1 до 30");
        System.out.println("Здоровье - это натуральное число от 0 до 100. Если Здоровье становится равным 0, то Существо умирает.");
        System.out.println("Игрок может себя исцелить до 4-х раз на 30% от максимального Здоровья.");
        System.out.println("У Существа есть параметр Урон. Это диапазон натуральных чисел от 1 до 100. Например, 1-6");
        System.out.println("""
                Одно Существо может ударить другое по такому алгоритму:
                  - Рассчитываем Модификатор атаки. Он равен разности Атаки атакующего и Защиты защищающегося плюс 1
                  - Успех определяется броском N кубиков с цифрами от 1 до 6, где N - это Модификатор атаки. Всегда бросается хотя бы один кубик.
                  - Удар считается успешным, если хотя бы на одном из кубиков выпадает 5 или 6
                  - Если удар успешен, то берется произвольное значение из параметра Урон атакующего и вычитается из Здоровья защищающегося.
                """);
    }
}
