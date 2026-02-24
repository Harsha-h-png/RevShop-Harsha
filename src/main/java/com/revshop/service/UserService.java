package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public void register(User user) {
        userDAO.registerUser(user);
    }

    public User login(String email, String password) {
        return userDAO.login(email, password);
    }
}
