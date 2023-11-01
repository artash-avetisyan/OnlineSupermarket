package storage;

import model.User;
import model.UserType;

public class UserStorage {
    private User[] users = new User[10];
    private int size;

    public void add(User user) {
        if (size == users.length) {
            extend();
        }
        users[size++] = user;
    }

    private void extend() {
        User[] tmp = new User[size * 2];
        System.arraycopy(users, 0, tmp, 0, users.length);
        users = tmp;
    }

    public void printAllUsers() {
        for (int i = 0; i < size; i++) {
            if (users[i].getUserType().equals(UserType.USER)) {
                System.out.println(users[i].getUserType() + "Id: " + users[i].getId() + "Name: " + users[i].getName());
            }
        }
    }

    public User checkLoginAndPassword(String email, String password) {
        for (int i = 0; i < size; i++) {
            if (email.equals(users[i].getEmail()) && password.equals(users[i].getPassword())) {
                return users[i];
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (int i = 0; i < size; i++) {
            if (users[i].getEmail().equals(email)) {
                return users[i];
            }
        }
        return null;
    }
}
