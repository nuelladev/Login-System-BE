package com.example.login.requests;

import com.example.login.model.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequests {
    private String username;
    private String email;
    private String password;

}
