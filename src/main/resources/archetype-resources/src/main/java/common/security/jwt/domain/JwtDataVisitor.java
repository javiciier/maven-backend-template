package ${package}.common.security.jwt.domain;

import lombok.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtDataVisitor {
    public static Map<String, Object> toMap(JwtData jwtData) {
        Map<String, Object> map = new HashMap<>();
        map.put(JwtDataFields.USER_ID.getValue(), jwtData.getUserID());
        map.put(JwtDataFields.NICKNAME.getValue(), jwtData.getNickname());
        map.put(JwtDataFields.ROLES.getValue(), jwtData.getRoles());

        return map;
    }

    @SuppressWarnings("unchecked")
    public static JwtData fromMap(Map<String, Object> map) {
        return JwtData.builder()
                .userID(UUID.fromString((String) map.get(JwtDataFields.USER_ID.getValue())))
                .nickname((String) map.get(JwtDataFields.NICKNAME.getValue()))
                .roles((List<String>) map.get(JwtDataFields.ROLES.getValue()))
                .build();
    }
}
