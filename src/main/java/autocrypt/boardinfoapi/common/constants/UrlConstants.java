package autocrypt.boardinfoapi.common.constants;

public abstract class UrlConstants {
    public static final String API_PREFIX = "/api";
    public static final String ID = "/{id}";

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";

    /**
     * user uri
     */
    public static final String USER_BASE = API_PREFIX+"/users";
    public static final String LOGIN = "/login";

    /**
     * post uri
     */
    public static final String POST_BASE = API_PREFIX + "/posts";
    public static final String LOCK = "/lock";
}
