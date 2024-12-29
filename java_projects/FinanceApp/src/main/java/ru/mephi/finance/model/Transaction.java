package ru.mephi.finance.model;

import java.time.LocalDate;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class Transaction {
    private String category;
    private double amount;
    private TransactionType type; // Enum: INCOME или EXPENSE
    private LocalDate date;

    public Transaction() {
    }

    public Transaction(String category, double amount, TransactionType type, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
