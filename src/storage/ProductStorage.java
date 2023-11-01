package storage;

import model.Product;

public class ProductStorage {
    private Product[] products = new Product[10];
    private int size;

    public void add(Product product) {
        if (size == products.length) {
            extend();
        }
        products[size++] = product;
    }

    private void extend() {
        Product[] tmp = new Product[size * 2];
        System.arraycopy(tmp, 0, tmp, 0, products.length);
        products = tmp;
    }

    public void printAllProduct() {
        for (int i = 0; i < size; i++) {
            System.out.println("ID: " + products[i].getId() + " name : " + products[i].getName() + " Type : " + products[i].getProductType()
                    + " price : " + products[i].getPrice() + " quantity : " + products[i].getStockQty());
        }
    }

    public Product getById(String productId) {
        for (int i = 0; i < size; i++) {
            if (products[i].getId().equals(productId)) {
                return products[i];
            }
        }
        return null;
    }

    public void deleteById(String productId) {
        int indexById = getIndexById(productId);
        if (indexById == -1) {
            System.err.println("Wrong id");
        }
        for (int i = indexById + 1; i < size; i++) {
            products[i - 1] = products[i];
        }
        size--;
    }

    private int getIndexById(String productId) {
        for (int i = 0; i < size; i++) {
            if (products[i].getId().equals(productId)) {
                return i;
            }
        }
        return -1;
    }

    public Double countTotalPrice(String productId, int productQty) {
        int indexById = getIndexById(productId);
        if (indexById == -1) {
            System.err.println("Wrong id");
        }
        return products[indexById].getPrice() * productQty;
    }

    public Product getProductById(String productId) {
        for (int i = 0; i < size; i++) {
            if (products[i].getId().equals(productId)) {
                return products[i];
            }
        }
        return null;
    }
}
