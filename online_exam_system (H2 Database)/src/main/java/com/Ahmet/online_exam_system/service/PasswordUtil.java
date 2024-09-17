package com.Ahmet.online_exam_system.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }


    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String password = "mySecretPassword";
        String hashedPassword = hashPassword(password);

        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("Password matches: " + verifyPassword(password, hashedPassword));
    }
}
