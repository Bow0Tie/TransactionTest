package ru.playtox.users;

import ru.playtox.app.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ACollection<T extends Account> {

    private final HashMap<UUID, T> accounts;
    private final ArrayList<UUID> keys;

    public ACollection() {
        accounts = new HashMap<>();
        keys = new ArrayList<>();
    }

    public void add(T account) {
        accounts.put(account.getID(), account);
        keys.add(account.getID());
    }

    public T get(UUID ID) {
        return accounts.get(ID);
    }

    public T get(int num) {
        try {
            return accounts.get(keys.get(num));
        } catch (IndexOutOfBoundsException e) {
            Main.logger.warn("unknown account number");
            return null;
        }
    }

    public void del(UUID ID) {
        accounts.remove(ID);
    }

    public void del(int num) {
        try {
            accounts.remove(keys.get(num));
        } catch (IndexOutOfBoundsException e) {
            Main.logger.error("try to remove index out of bounds");
        }
    }
}
