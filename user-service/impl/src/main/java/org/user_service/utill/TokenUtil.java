package org.user_service.utill;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.user_service.exception.UnauthorizedException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@UtilityClass
@Slf4j
public class TokenUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static UUID getUserFromToken(String token) {
        Map<String, Object> tokenInfo = decodeToken(token);
        String userId = (String) tokenInfo.get("sub");
        return UUID.fromString(userId);
    }

    private Map<String, Object> decodeToken(String authorization) {
        Map<String, Object> tokenInfo = null;

        if (authorization != null && !authorization.isBlank()) {
            try {
                String jwtUserPart = authorization.split("\\.")[1];
                String info = new String(Base64.getDecoder().decode(jwtUserPart), StandardCharsets.UTF_8);
                tokenInfo = objectMapper.readValue(info, new TypeReference<>() {
                });
            } catch (RuntimeException | JsonProcessingException e) {
                log.error("Token can't parse", e);
                throw new UnauthorizedException("Token can't parse: [%s]".formatted(e.getMessage()));
            }
        }

        return tokenInfo;
    }
}
