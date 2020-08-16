package ru.playtox.users;

public class AGenerator {

    private final ACollection<Account> collection;

    private final int amount;
    private final int money;

    public AGenerator(ACollection<Account> collection, int amount, int money) {
        this.collection = collection;
        this.amount = amount;
        this.money = money;
    }

    public void generate() {
        for (int x = 0; x < amount; x++) {
            collection.add(new Account(money));
        }
    }
}
