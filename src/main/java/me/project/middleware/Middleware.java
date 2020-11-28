package me.project.middleware;

import me.project.passable.Passable;

public interface Middleware {

    Passable handle(Passable passable);

}
