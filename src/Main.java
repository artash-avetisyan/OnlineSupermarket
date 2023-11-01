import command.Commands;
import exception.OutOfStockException;
import model.*;
import storage.OrderStorage;
import storage.ProductStorage;
import storage.UserStorage;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main implements Commands {
    static Scanner scanner = new Scanner(System.in);
    static UserStorage userStorage = new UserStorage();
    static ProductStorage productStorage = new ProductStorage();
    static OrderStorage orderStorage = new OrderStorage();
    static User currentUser;

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printLoginRegisterCommands();
            String command = scanner.nextLine();
            switch (command) {
                case LOGIN:
                    login();
                    break;
                case REGISTER:
                    register();
                    break;
                default:
                    System.out.println("Wrong command");
            }
        }
    }

    private static void register() {
        System.out.println("Please input id");
        String id = scanner.nextLine().toUpperCase();
        System.out.println("Please input name");
        String name = scanner.nextLine();
        System.out.println("Please input email");
        String email = scanner.nextLine().toLowerCase();
        System.out.println("Please input password");
        String password = scanner.nextLine();
        System.out.println("Please select userType");
        String userTypeInput = scanner.nextLine().toUpperCase();
        try {
            UserType userType = UserType.valueOf(userTypeInput);
            User user = new User(id, name, email, password, userType);
            userStorage.add(user);
            System.out.println(userType + " added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user type. Please select from the following options " + Arrays.toString(UserType.values()));
        }
    }

    private static void login() {
        System.out.println("Please input email");
        String email = scanner.nextLine().toLowerCase();
        System.out.println("Please input password");
        String password = scanner.nextLine();
        currentUser = userStorage.checkLoginAndPassword(email, password);
        if (currentUser == null) {
            System.out.println("Wrong email or password");
            return;
        }
        if (currentUser.getUserType() == UserType.USER) userCommands();
        else adminCommands();
    }

    private static void adminCommands() {
        boolean isRun = true;
        while (isRun) {
            Commands.printAdminCommands();
            String command = scanner.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case ADD_PRODUCT:
                    addProduct();
                    break;
                case REMOVE_PRODUCT_BY_ID:
                    removeProductById();
                    break;
                case PRINT_PRODUCTS:
                    productStorage.printAllProduct();
                    break;
                case PRINT_USERS:
                    userStorage.printAllUsers();
                    break;
                case PRINT_ORDERS:
                    orderStorage.printAllOrders();
                    break;
                case CHANGE_ORDER_STATUS:
                    changeOrderStatus();
                    break;
                default:
                    System.out.println("Wrong command");

            }
        }
    }

    private static void addProduct() {
        System.out.println("Please input ID");
        String id = scanner.nextLine().toUpperCase();
        System.out.println("Please input Name");
        String name = scanner.nextLine();
        System.out.println("Please input Description");
        String description = scanner.nextLine();
        System.out.println("Please input Price");
        Double price = null;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please enter a valid number.");
        }
        if (price == null) {
            return;
        }

        System.out.println("Please input stock Quantity");
        int stockQty = 0;
        try {
            stockQty = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock quantity. Please enter a valid integer.");
        }
        if (stockQty == 0) {
            return;
        }
        System.out.println("Please input product Type (ELECTRONICS, CLOTHING,BOOKS): ");
        String productTypeStr = scanner.nextLine().toUpperCase();
        try {
            ProductType productType = ProductType.valueOf(productTypeStr);
            Product product = new Product(id, name, description, price, stockQty, productType);
            productStorage.add(product);
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid product type. Please select from the following type " + Arrays.toString(ProductType.values()));
        }
    }

    private static void removeProductById() {
        System.out.println("Please choose product ID");
        productStorage.printAllProduct();
        String productId = scanner.nextLine().toUpperCase();
        Product byId = productStorage.getById(productId);
        if (byId == null) {
            System.err.println("Product with " + productId + " does not exists");
            return;
        }
        productStorage.deleteById(productId);
        System.err.println("Product deleted");
    }

    private static void changeOrderStatus() {
        System.out.println("Please input order id");
        orderStorage.printAllOrders();
        String orderId = scanner.nextLine().toUpperCase();
        orderStorage.printOrderById(orderId);
        System.out.println("Please input change order status (NEW,DELIVERED,CANCELED): ");
        String productTypeStr = scanner.nextLine().toUpperCase();
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(productTypeStr);
            orderStorage.changeOrderById(orderId, orderStatus);
            System.out.println("Order changed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid order status. Please select from the following status " + Arrays.toString(OrderStatus.values()));
        }

    }

    private static void userCommands() {
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            String command = scanner.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    currentUser = null;
                    break;
                case PRINT_ALL_PRODUCTS:
                    productStorage.printAllProduct();
                    break;
                case BUY_PRODUCT:
                    buyProduct();
                    break;
                case PRINT_MY_ORDERS:
                    printCurrentUserOrders();
                    break;
                case CANCEL_ORDER_BY_ID:
                    cancelOrderById();
                    break;

            }
        }
    }

    private static void buyProduct() {
        productStorage.printAllProduct();

        System.out.println("Please input product ID ");
        String productId = scanner.nextLine().toUpperCase();
        Product product = productStorage.getProductById(productId);

        System.out.println("Please input stock Quantity ");
        int productQty = Integer.parseInt(scanner.nextLine());
        if (productQty > product.getStockQty()) {
            throw new OutOfStockException("Don't have enough product available");
        }
        System.out.println("Please input payment method " + Arrays.toString(PaymentMethod.values()));
        String paymentMethodInput = scanner.nextLine().toUpperCase();
        try {
            PaymentMethod.valueOf(paymentMethodInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid payment type. Please select from the following type " + Arrays.toString(PaymentMethod.values()));
        }

        Double totalPrice = productStorage.countTotalPrice(productId, productQty);

        System.out.println("Do you want to buy this product in this quantity " + productQty + " and price " + product.getPrice() + " (Total Price " + totalPrice + "), write yes if you want to confirm");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("YES")) {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodInput);
            Order order = new Order(productId, currentUser, product, new Date(), totalPrice, OrderStatus.NEW, productQty, paymentMethod);
            orderStorage.add(order);
            System.out.println("Order added successfully");
        }
    }

    private static void printCurrentUserOrders() {
        orderStorage.printOrderUserByEmail(currentUser.getEmail());
    }

    private static void cancelOrderById() {
        System.out.println("Please write order id");
        orderStorage.printOrderUserByEmail(currentUser.getEmail());
        String orderIdByCanceled = scanner.nextLine().toUpperCase();
        orderStorage.cancelOrderByID(orderIdByCanceled);

    }

}