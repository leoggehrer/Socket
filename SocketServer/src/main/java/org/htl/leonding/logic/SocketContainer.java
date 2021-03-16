package org.htl.leonding.logic;

import org.htl.leonding.models.Message;
import org.htl.leonding.pattern.Observable;

public class SocketContainer extends Observable {
    private static SocketContainer instance = null;

    public static SocketContainer getInstance() {
        if (instance == null) {
            instance = new SocketContainer();
        }
        return instance;
    }

    private SocketContainer() {
    }

    public synchronized void addMessage(Message message) {
        if (message == null)
            throw new IllegalArgumentException("message");

        notifyAll(message);
    }
}

