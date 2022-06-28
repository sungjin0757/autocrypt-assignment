package autocrypt.boardinfoapi.common.constants;

import java.util.Arrays;
import java.util.List;

public abstract class SecurityConstants {
    public static final String JWT_FILTER_PROCESSING_URL_PREFIX = "/api/**";
    public static final List<String> JWT_FILTER_SKIP_URL = Arrays.asList(
            "/api/users/login", "/api/users/save", "/api/locale-check"
    );

    public static final String SWAGGER_KEY = "jwt-auth";
    public static final String BEARER = "Bearer ";
}
