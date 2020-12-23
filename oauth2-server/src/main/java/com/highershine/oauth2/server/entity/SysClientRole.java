package com.highershine.oauth2.server.entity;

import lombok.Data;

import java.util.List;

@Data
public class SysClientRole {
    private List<String> roles;

    private String clientId;
}
