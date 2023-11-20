package com.example.login.requests;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {
    private String jwtToken;
}
