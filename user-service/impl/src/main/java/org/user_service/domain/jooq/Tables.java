/*
 * This file is generated by jOOQ.
 */
package org.user_service.domain.jooq;


import org.user_service.domain.jooq.tables.AuthCode;
import org.user_service.domain.jooq.tables.User;


/**
 * Convenience access to all tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * User's disposable codes for auth
     */
    public static final AuthCode AUTH_CODE = AuthCode.AUTH_CODE;

    /**
     * User's info
     */
    public static final User USER = User.USER;
}
