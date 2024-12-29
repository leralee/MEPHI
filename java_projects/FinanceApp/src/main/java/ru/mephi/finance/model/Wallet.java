package ru.mephi.finance.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class Wallet {
    private String ownerLogin;                   // Логин пользователя-владельца
    private List<Transaction> transactions;      // Список транзакций
    private Map<String, Double> budgetByCategory;// Бюджеты по категориям

    // Конструктор без параметров (для Jackson)
    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgetByCategory = new HashMap<>();
    }

    // Конструктор с параметрами
    public Wallet(String ownerLogin) {
        this();
        this.ownerLogin = ownerLogin;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void setBudget(String category, double budget) {
        budgetByCategory.put(category, budget);
    }

    public double getBudgetForCategory(String category) {
        return budgetByCategory.getOrDefault(category, 0.0);
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Double> getBudgetByCategory() {
        return budgetByCategory;
    }
}
