package com.highershine.portal.common.utils;

import java.util.UUID;

public class KeyUtils {
	public static String get32UUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }

    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();
    }

    public String[] get32UUIDs(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = get32UUID();
        }
        return ss;
    }
}
