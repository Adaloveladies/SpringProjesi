package com.adaloveladies.SpringProjesi.payload;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}