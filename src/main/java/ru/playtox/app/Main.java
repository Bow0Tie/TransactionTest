package ru.playtox.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.playtox.threads.Transactor;
import ru.playtox.users.ACollection;
import ru.playtox.users.AGenerator;
import ru.playtox.users.Account;

import java.util.concurrent.*;


public class Main {

    public static final Logger logger = LogManager.getLogger();

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static Future<?> future;

    private static final int ACCOUNTS_AMOUNT = 10;
    private static final int TREADS_AMOUNT = 4;
    private static final int START_MONEY = 10000;

    private static int transactionCount = 0;
    private static int sum = 0;

    public static int getTransactionCount() {
        return transactionCount;
    }

    public static void incTransactionCount() {
        transactionCount++;
    }

    public static int getAccountsAmount() {
        return ACCOUNTS_AMOUNT;
    }

    public static int getStartMoney() {
        return START_MONEY;
    }

    public static void main(String[] args) {

        if (ACCOUNTS_AMOUNT < 2 || TREADS_AMOUNT < 1 || START_MONEY < 1) {
            logger.error("bad constants");
            return;
        }
        ACollection<Account> collection = new ACollection<>();
        AGenerator generator = new AGenerator(collection, ACCOUNTS_AMOUNT, START_MONEY);
        generator.generate();
        for (int i = 0; i < ACCOUNTS_AMOUNT; i++) {
            logger.info("Account " + collection.get(i).getID() + " had " + collection.get(i).getMoney());
        }

        for (int i = 0; i < TREADS_AMOUNT; i++)
            future = executor.submit(new Transactor(collection));

        try {
            future.get();
            logger.info("All Threads done");
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("is interrupt");
        }

        for (int i = 0; i < ACCOUNTS_AMOUNT; i++) {
            logger.info("Account " + collection.get(i).getID() + " had " + collection.get(i).getMoney());
            sum += collection.get(i).getMoney();
        }
        logger.info("Final sum after all transaction = " + sum);

        try {
            logger.info("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.warn("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                logger.error("cancel non-finished tasks");
            }
            executor.shutdownNow();
            logger.info("shutdown finished");
        }
    }
}
