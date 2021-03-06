package co.com.dk.juanvaldez.jvsignupmc.constants;

public class WebURIConstants {

    //authentication
    public static final String UNAUTHORIZED_REQUEST = "Unauthorized request.";

    public static final String BEARER = "Bearer ";

    public static final String AUDIENCE_SERVICE = "service";
    public static final String AUDIENCE_PUBLIC = "public";
    public static final String AUDIENCE_INTERNAL = "internal";

    public static final Long USER_SERVICE = 0L;

    //SPOONITY APIs
    public static final String SPOONITY_USER_EMAIL_EXISTS = "/user/email/exists";
    public static final String SPOONITY_USER_CEDULA_EXISTS = "/user/cedula/exists";
    public static final String SPOONITY_USER_MOBILE_EXISTS = "/user/mobile/exists";
    public static final String SPOONITY_USER_REGISTER = "/user/register";

    public static final String SPOONITY_USER_ACTIVATE_BY_SMS = "/user/activate/sms";
    public static final String SPOONITY_USER_ACTIVATE_BY_EMAIL = "/user/activate/email";
    public static final String SPOONITY_USER_ACTIVATE = "/user/activate";
    public static final String SPOONITY_IS_VALIDATED = "/user/isValidated";

    //PERMISSIONS
    public static final String SERVICE = "SERVICE";
    public static final String PUBLIC = "PUBLIC";

}
