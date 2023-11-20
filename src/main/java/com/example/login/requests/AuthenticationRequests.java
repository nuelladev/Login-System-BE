package com.example.login.requests;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequests {
    private String username;
    private String password;
}
