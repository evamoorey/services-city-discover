package org.city_discover.service;

public interface TokenService {
    void verifyToken(String token);

    void verifyAdmin(String authorization);
}
