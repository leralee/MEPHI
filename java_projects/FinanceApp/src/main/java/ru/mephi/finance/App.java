package ru.mephi.finance;

import ru.mephi.finance.model.TransactionType;
import ru.mephi.finance.model.User;
import ru.mephi.finance.model.Wallet;
import ru.mephi.finance.repository.DataStorage;
import ru.mephi.finance.service.AuthService;
import ru.mephi.finance.service.WalletService;

import java.util.Map;
import java.util.Scanner;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class App {
    private static final DataStorage dataStorage = new DataStorage();
    private static Map<String, Wallet> wallets;
    private static Map<String, User> userMap;
    private static WalletService walletService;
    private static AuthService authService;

    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeApp();

        while (true) {
            if (currentUser == null) {
                displayUnauthenticatedMenu();
            } else {
                displayAuthenticatedMenu();
            }

            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                exitApplication();
                break;
            }

            handleCommand(command);
        }
    }

    private static void displayUnauthenticatedMenu() {
        System.out.println("Введите команду (login/register/exit):");
    }

    private static void displayAuthenticatedMenu() {
        System.out.println("Введите команду (addIncome, addExpense, setBudget, showStats, logout, exit):");
    }

    private static void handleCommand(String command) {
        if (command.equalsIgnoreCase("register")) {
            handleRegistration();
        } else if (command.equalsIgnoreCase("login")) {
            handleLogin();
        } else if (command.equalsIgnoreCase("addIncome")) {
            handleAddIncome();
        } else if (command.equalsIgnoreCase("addExpense")) {
            handleAddExpense();
        } else if (command.equalsIgnoreCase("setBudget")) {
            handleSetBudget();
        } else if (command.equalsIgnoreCase("showStats")) {
            handleShowStats();
        } else if (command.equalsIgnoreCase("logout")) {
            handleLogout();
        } else {
            System.out.println("Неизвестная команда. Повторите ввод.");
        }
    }

    private static void handleRegistration() {
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        if (authService.register(login, password)) {
            System.out.println("Пользователь успешно зарегистрирован.");
            walletService.createWalletForUser(login);
        } else {
            System.out.println("Пользователь с таким логином уже существует.");
        }
    }

    private static void handleLogin() {
        System.out.println("Введите логин:");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        authService.login(login, password)
                .ifPresentOrElse(
                        user -> {
                            currentUser = user; // Сохраняем текущего пользователя
                            System.out.println("Добро пожаловать, " + user.getLogin());
                        },
                        () -> System.out.println("Неверные логин или пароль.")
                );
    }

    private static void handleAddIncome() {
        if (!isUserLoggedIn()) return;

        System.out.println("Введите категорию дохода:");
        String category = scanner.nextLine();
        System.out.println("Введите сумму:");
        Double amount = readDoubleFromUser();
        if (amount == null) return;

        walletService.addTransaction(currentUser.getLogin(), category, amount, TransactionType.INCOME);
        System.out.println("Доход добавлен.");
    }

    private static void handleAddExpense() {
        if (!isUserLoggedIn()) return;

        System.out.println("Введите категорию расхода:");
        String category = scanner.nextLine();
        System.out.println("Введите сумму:");
        Double amount = readDoubleFromUser();
        if (amount == null) return;

        walletService.addTransaction(currentUser.getLogin(), category, amount, TransactionType.EXPENSE);

        if (walletService.isBudgetExceeded(currentUser.getLogin(), category)) {
            System.out.println("Внимание! Превышен бюджет по категории: " + category);
        } else {
            System.out.println("Расход добавлен.");
        }
    }

    private static void handleSetBudget() {
        if (!isUserLoggedIn()) return;

        System.out.println("Введите категорию:");
        String category = scanner.nextLine();
        System.out.println("Введите сумму бюджета:");
        Double budget = readDoubleFromUser();
        if (budget == null) return;

        walletService.setBudget(currentUser.getLogin(), category, budget);
        System.out.println("Бюджет установлен.");
    }

    private static void handleShowStats() {
        if (!isUserLoggedIn()) return;

        String login = currentUser.getLogin();
        double totalIncome = walletService.getTotalIncome(login);
        double totalExpense = walletService.getTotalExpense(login);

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + totalExpense);

        Wallet wallet = walletService.getWalletByLogin(login);
        for (String category : wallet.getBudgetByCategory().keySet()) {
            double budget = wallet.getBudgetForCategory(category);
            double expense = walletService.getExpenseByCategory(login, category);
            double remaining = budget - expense;
            System.out.println("Категория: " + category + ", Бюджет: " + budget + ", Остаток: " + remaining);
        }
    }

    private static void handleLogout() {
        currentUser = null;
        System.out.println("Вы вышли из аккаунта.");
    }

    private static void exitApplication() {
        System.out.println("Приложение завершено.");
        scanner.close();
    }

    private static boolean isUserLoggedIn() {
        if (currentUser == null) {
            System.out.println("Сначала авторизуйтесь!");
            return false;
        }
        return true;
    }

    private static Double readDoubleFromUser() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введено некорректное число. Попробуйте снова.");
            return null;
        }
    }

    private static void initializeApp() {
        // Загрузка данных
        wallets = dataStorage.loadWallets();
        userMap = dataStorage.loadUsers();
        System.out.println("Данные успешно загружены.");

        walletService = new WalletService(wallets);
        authService = new AuthService(userMap);

        // Установка ShutdownHook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сохраняем данные перед завершением...");
            dataStorage.saveWallets(wallets);
            dataStorage.saveUsers(userMap);
            System.out.println("Данные сохранены перед завершением работы.");
        }));
    }
}
