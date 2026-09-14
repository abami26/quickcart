package com.niit.quickcart.dto;

import java.util.Map;

public class Dtos {

    public static class SignupRequest {
        public String name;
        public String email;
        public String password;
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class OrderRequest {
        // e.g. { "1": 2, "3": 1 }  -> product id (as string) -> quantity
        public Map<String, Integer> items;
        public String address;
    }

    public static class UserSummary {
        public Long id;
        public String name;
        public String email;

        public UserSummary(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    public static class AuthResponse {
        public String token;
        public UserSummary user;

        public AuthResponse(String token, UserSummary user) {
            this.token = token;
            this.user = user;
        }
    }
}
