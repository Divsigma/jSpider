package me.project.middleware;

import me.project.passable.Passable;
import me.project.passable.Request;

public class SetUserAgent implements Middleware {

    @Override
    public Passable handle(Passable passable) {
        System.out.println("Passing through Middleware: SetUserAgent");

        Request request = (Request) passable;
        request.setHeaderDomain("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");

        return request;
    }

}
