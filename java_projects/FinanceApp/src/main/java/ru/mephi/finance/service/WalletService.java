package ru.mephi.finance.service;

import ru.mephi.finance.model.Transaction;
import ru.mephi.finance.model.TransactionType;
import ru.mephi.finance.model.Wallet;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class WalletService {
    private Map<String, Wallet> wallets;

    public WalletService(Map<String, Wallet> wallets) {
        this.wallets = wallets;
    }

    // Создаём кошелёк для нового пользователя
    public Wallet createWalletForUser(String login) {
        // Если кошелёк уже есть, не пересоздаём
        if (wallets.containsKey(login)) {
            return wallets.get(login);
        }
        // Создаём новый и добавляем в общую Map
        Wallet wallet = new Wallet(login);
        wallets.put(login, wallet);
        return wallet;
    }

    // Получить кошелёк пользователя
    public Wallet getWalletByLogin(String login) {
        return wallets.get(login);
    }

    // Добавить доход/расход
    public void addTransaction(String login, String category, double amount, TransactionType type) {
        Wallet wallet = wallets.get(login);
        if (wallet != null) {
            Transaction transaction = new Transaction(category, amount, type, LocalDate.now());
            wallet.addTransaction(transaction);
        }
    }

    // Установить бюджет для категории
    public void setBudget(String login, String category, double budget) {
        Wallet wallet = wallets.get(login);
        if (wallet != null) {
            wallet.setBudget(category, budget);
        }
    }

    // Подсчитать общий доход / расход
    public double getTotalIncome(String login) {
        Wallet wallet = wallets.get(login);
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense(String login) {
        Wallet wallet = wallets.get(login);
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // Подсчитать доход / расход по категории
    public double getIncomeByCategory(String login, String category) {
        Wallet wallet = wallets.get(login);
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME && t.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getExpenseByCategory(String login, String category) {
        Wallet wallet = wallets.get(login);
        return wallet.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE && t.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // Проверка остатка бюджета и уведомление о превышении
    public boolean isBudgetExceeded(String login, String category) {
        Wallet wallet = wallets.get(login);
        double budget = wallet.getBudgetForCategory(category);
        double expense = getExpenseByCategory(login, category);
        // Если расход больше, чем бюджет, значит превышен
        return expense > budget;
    }
}
