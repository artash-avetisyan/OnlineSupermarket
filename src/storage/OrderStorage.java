package storage;

import model.Order;
import model.OrderStatus;
import model.Product;

public class OrderStorage {
    private Order[] orders = new Order[10];
    private int size;

    public void add(Order order) {
        if (size == orders.length) {
            extend();
        }
        orders[size++] = order;
    }

    private void extend() {
        Order[] tmp = new Order[size * 2];
        System.arraycopy(tmp, 0, tmp, 0, orders.length);
        orders = tmp;
    }

    private void printAllOrder() {
        for (int i = 0; i < size; i++) {
            System.out.println(orders[i].getId());
        }
    }

    public Product findUser(String id) {
        for (int i = 0; i < size; i++) {
            if (orders[i].getId().equals(id)) {
                return orders[i].getProduct();
            }

        }
        return null;
    }

    public void printOrderUserByEmail(String email) {
        for (int i = 0; i < size; i++) {
            if (orders[i].getUser().getEmail().equals(email)) {
                System.out.println("Order: " + orders[i].getId() + " product " + orders[i].getProduct().getName() + " quantity " + orders[i].getQty() + " price" + orders[i].getPrice() + "Order status:" + orders[i].getOrderStatus());
            }
        }
    }

    public void printAllOrders() {
        for (int i = 0; i < size; i++) {
            System.out.println("ID:" + orders[i].getId() + "who buy - " + orders[i].getUser().getName() + " product name - " + orders[i].getProduct().getName() + " price " + orders[i].getPrice() + " quantity " + orders[i].getQty() + "Order status:" + orders[i].getOrderStatus());
        }
    }

    public void changeOrderById(String orderId, OrderStatus orderStatus) {
        for (int i = 0; i < size; i++) {
            if (orders[i].getId().equals(orderId)) {
                orders[i].setOrderStatus(orderStatus);
                if (orderStatus == OrderStatus.DELIVERED) {
                    Product product = orders[i].getProduct();
                    int currentStockQty = product.getStockQty();
                    int orderedQty = orders[i].getQty();
                    int newStockQty = currentStockQty - orderedQty;
                    product.setStockQty(newStockQty);
                }
            }
        }
    }

    public void cancelOrderByID(String orderIdByCanceled) {
        for (int i = 0; i < size; i++) {
            if (orders[i].getId().equals(orderIdByCanceled)) {
                orders[i].setOrderStatus(OrderStatus.CANCELED);
            }
        }
    }

    public void printOrderById(String orderId) {
        for (int i = 0; i < size; i++) {
            if (orders[i].getId().equals(orderId)) {
                System.out.println("Order: " + orders[i].getId() + " product " + orders[i].getProduct().getName() + " quantity " + orders[i].getQty() + " price" + orders[i].getPrice() + "Order status:" + orders[i].getOrderStatus());
            }
        }

    }
}
