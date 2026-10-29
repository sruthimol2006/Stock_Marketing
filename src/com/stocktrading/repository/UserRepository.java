package com.stocktrading.repository;

import com.stocktrading.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public boolean save(User user) {
        if (user == null || user.getUserId() == null || findById(user.getUserId()) != null) {
            return false;
        }
        return users.add(user);
    }

    public User findById(String userId) {
        if (userId == null) {
            return null;
        }
        for (User user : users) {
            if (userId.equals(user.getUserId())) {
                return user;
            }
        }
        return null;
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public boolean update(User updatedUser) {
        if (updatedUser == null || updatedUser.getUserId() == null) {
            return false;
        }
        for (int i = 0; i < users.size(); i++) {
            if (updatedUser.getUserId().equals(users.get(i).getUserId())) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String userId) {
        return userId != null && users.removeIf(user -> userId.equals(user.getUserId()));
    }
}
