package ru.playtox.threads;

import ru.playtox.app.Main;
import ru.playtox.users.ACollection;
import ru.playtox.users.Account;

import java.util.concurrent.locks.ReentrantLock;

public class Transactor implements Runnable {

    private final ACollection<Account> collection;
    private final ReentrantLock lock = new ReentrantLock();

    public Transactor(ACollection<Account> collection) {
        this.collection = collection;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            do {
                if (Main.getTransactionCount() < 30 && lock.isHeldByCurrentThread()) {
                    Account accountFrom = collection.get((int) (Math.random() * Main.getAccountsAmount()));
                    Account accountTo = collection.get((int) (Math.random() * Main.getAccountsAmount()));
                    int transactionSum = (int) (Math.random() * (Main.getStartMoney() + 1));
                    if (accountFrom != accountTo) {
                        if (transactionSum < accountFrom.getMoney()) {
                            Main.incTransactionCount();
                            int localTransactionCount = Main.getTransactionCount();
                            Main.logger.info("Transaction  № " + localTransactionCount + " START");
                            Main.logger.info("Account " + accountFrom.getID() + " try to lose " + transactionSum);
                            accountFrom.setMoney(accountFrom.getMoney() - transactionSum);
                            Main.logger.info("Account " + accountTo.getID() + " try to get " + transactionSum);
                            accountTo.setMoney(accountTo.getMoney() + transactionSum);
                            Main.logger.info("Transaction  № " + localTransactionCount + " COMPLETE");
                        } else Main.logger.warn("Not enough money");
                    } else Main.logger.warn("Picked same accounts");
                } else return;
                try {
                    Thread.sleep((int) (Math.random() * 1001) + 1000);
                } catch (InterruptedException e) {
                    Main.logger.warn("Thread interrupted");
                }
            }
            while (true);
        } finally {
            lock.unlock();
        }
    }
}
