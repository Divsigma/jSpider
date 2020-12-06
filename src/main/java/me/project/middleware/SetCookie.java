package me.project.middleware;

import me.project.Request;
import me.project.Response;

public class SetCookie implements Middleware {

    @Override
    public Object handleRequest(Request request) {

        String cookieString = request.getCookieString();
        if(cookieString != null) {
            System.err.println("Setting cookies in request ...");
            request.setHeaderField("Cookie", cookieString);
        }
        return null;
    }

    @Override
    public Object handleResponse(Response response) {

        String cookieString = response.getHeaderField("Set-Cookie");
        if(cookieString != null) {
            System.err.println("Setting cookies in request (from response) ...");
            // System.out.println(cookieString);
            Request request = response.getRequest();

            String[] fieldString = cookieString.split("; ");
            for(String field : fieldString) {
                String[] kv = field.split("=", 2);
                if(kv.length == 1) {
                    request.setCookieField(kv[0], " ");
                } else {
                    request.setCookieField(kv[0], kv[1]);
                }

            }

            // System.out.println(request.getCookieString());
        }



        return null;
    }
}
