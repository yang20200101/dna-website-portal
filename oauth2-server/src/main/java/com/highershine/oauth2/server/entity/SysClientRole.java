package com.highershine.oauth2.server.entity;

import lombok.Data;

@Data
public class SysClientRole {
    private String roleId;

    private String clientId;

    private String clientName;
}
