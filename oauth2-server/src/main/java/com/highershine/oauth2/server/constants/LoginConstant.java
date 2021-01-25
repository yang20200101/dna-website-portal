package com.highershine.oauth2.server.constants;

public final class LoginConstant {
    private LoginConstant() {

    }

    public static final String TYPE_PASSWORD = "password";

    public static final String TYPE_PKI = "pki";

    public static final String PASSWORD_EXCEPTION_PREFIX = "login:password:exception:";
    public static final String PKI_NOT_FOUND_EXCEPTION_PREFIX = "login:pki:exception:notFound:";
}
