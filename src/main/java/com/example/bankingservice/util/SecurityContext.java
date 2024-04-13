package com.example.bankingservice.util;

public class SecurityContext {
    private static final ThreadLocal<Integer> AUTHENTICATED_USER_ID = new ThreadLocal<>();
    public static Integer getAuthenticatedUserId() {
        return AUTHENTICATED_USER_ID.get();
    }
    public static void setAuthenticatedUserId(Integer authenticatedUserId) {
        AUTHENTICATED_USER_ID.set(authenticatedUserId);
    }
}
