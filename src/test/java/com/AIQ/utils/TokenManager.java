package com.AIQ.utils;

public class TokenManager {

    private static String token;

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getToken() {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is not set. Please authenticate first.");
        }
        return token;
    }

}
