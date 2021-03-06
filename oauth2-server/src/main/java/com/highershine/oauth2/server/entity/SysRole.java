package com.highershine.oauth2.server.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class SysRole implements GrantedAuthority {
    private String id;

    private String authority;
}
