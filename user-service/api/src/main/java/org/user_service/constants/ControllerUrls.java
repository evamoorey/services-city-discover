package org.user_service.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerUrls {
    public static final String USER_URL = "/auth/user";
    public static final String USER_ID_URL = "/auth/user/{id}";

    public static final String CODE_URL = "/auth/code";
}
