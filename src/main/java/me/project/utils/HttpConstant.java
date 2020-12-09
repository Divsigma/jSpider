package me.project.utils;

public class HttpConstant {

    public static abstract class ContentType {

        public static final String JSON = "application/json";

        public static final String FORM = "application/x-www-form-urlencoded";

    }

    public static abstract class Method {

        public static final String POST = "POST";

        public static final String GET = "GET";

    }

    public static abstract class StatusCode {

        public static final int CODE_200 = 200;

        public static final int CODE_301 = 301;

        public static final int CODE_302 = 302;

    }

}
