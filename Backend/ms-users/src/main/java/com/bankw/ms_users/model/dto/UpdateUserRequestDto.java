package com.bankw.ms_users.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {

    private String username;
    private String name;
    private String password;
    private String role;
    private String email;

}
