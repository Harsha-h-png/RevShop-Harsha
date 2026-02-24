package com.revshop.controller;

import com.revshop.model.User;
import com.revshop.service.AuthService;
import com.revshop.util.ValidationUtil;

import java.util.Scanner;

public class AuthController {

    private AuthService authService = new AuthService();
    private Scanner sc = new Scanner(System.in);

    public void register() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (ValidationUtil.isValidEmail(email)) break;
            System.out.println("❌ Invalid email format. Try again.");
        }

        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = sc.nextLine();
            if (ValidationUtil.isValidPassword(password)) break;
            System.out.println("❌ Password must contain:");
            System.out.println("• 8 characters");
            System.out.println("• Uppercase, lowercase, number, special char");
        }

        System.out.print("Enter role (buyer/seller): ");
        String role = sc.nextLine().toLowerCase();

        User user = new User(name, email, password, role);
        authService.register(user);

        System.out.println("✅ Registration successful");
    }

    public User login() {
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("❌ Invalid email format");
            return null;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = authService.login(email, password);

        if (user == null) {
            System.out.println("❌ Invalid email or password");
        } else {
            System.out.println("✅ Login successful");
        }

        return user;
    }
}
