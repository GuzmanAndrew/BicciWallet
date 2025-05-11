package com.bankw.ms_users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileDto {

    private String name;
    private String username;
    private String email;
    private String role;

}
