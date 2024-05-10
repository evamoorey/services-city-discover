package org.city_discover.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerUrls {
    public static final String LOGIN_URL = "/auth/login";
    public static final String LOGIN_CODE_URL = "/auth/code";
    public static final String REFRESH_URL = "/auth/refresh";

    public static final String USER_URL = "/user";
    public static final String USER_ID_URL = "/user/{id}";
    public static final String USERS_URL = "/users";

    public static final String SUBSCRIBE_URL = "/subscribe/{id}";
    public static final String UNSUBSCRIBE_URL = "/unsubscribe/{id}";
    public static final String SUBSCRIPTIONS_URL = "/subscriptions/{id}";
    public static final String SUBSCRIBERS_URL = "/subscribers/{id}";

    public static final String ADMIN_USER_ID_URL = "/admin/user/{id}";
}
