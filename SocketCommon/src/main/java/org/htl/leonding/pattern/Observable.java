package org.htl.leonding.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    public synchronized void addObserver(Observer observer) {
        if (observer == null)
            throw new IllegalArgumentException("observer");

        if (observers.contains(observer) == false) {
            observers.add(observer);
        }
    }
}
