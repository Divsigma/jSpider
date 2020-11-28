package me.project.middleware;

import me.project.passable.Passable;
import me.project.passable.Request;

public class SetCookie implements Middleware {

    @Override
    public Passable handle(Passable passable) {

        System.out.println("Passing through Middleware: SetCookie");

        Request request = (Request) passable;
        request.setHeaderDomain("cookie", "_ga=GA1.2.156721159.1596030976; sc_is_visitor_unique=rx11857110.1596502132.");

        return request;
    }
}
