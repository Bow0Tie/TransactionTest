package ru.playtox.users;

import ru.playtox.app.Main;

import java.util.UUID;

public class Account {

    private final UUID ID;
    private int Money;

    public Account() {
        ID = UUID.randomUUID();
        Money = 0;
    }

    public Account(int money) {
        ID = UUID.randomUUID();
        Money = money;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
        Main.logger.info("Account " + ID + " CONFIRMS");
    }

    public UUID getID() {
        return ID;
    }
}
