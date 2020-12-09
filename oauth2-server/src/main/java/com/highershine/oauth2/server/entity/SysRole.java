package com.highershine.oauth2.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole implements GrantedAuthority {

    private Long id;
    private String authority;

}
