package $package.common.security.jwt.application;

import $package.common.security.jwt.domain.JwtData;

public interface JwtGenerator {
    String generateJWT(JwtData data);

    JwtData extractData(String token);
}
