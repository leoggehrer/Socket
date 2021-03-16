package org.htl.leonding.logic;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import org.htl.leonding.models.Message;
import org.htl.leonding.pattern.Observable;
import org.htl.leonding.pattern.Observer;

public class SocketThread extends Thread implements Observer {
    private final static String QuitCommand = "quit";
    private final static String Disconnected = "disconnected";
    private UUID id = null;
    private final Socket socket;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    public SocketThread(Socket socket) {
        if (socket == null)
            throw new IllegalArgumentException("socket");

        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDaemon(true);
    }

    @Override
    public void run() {
        String command = "";

        while (command.equals(QuitCommand) == false) {
            try {
                Message message = (Message)ois.readObject();

                id = message.getId();
                command = message.getCommand();
                SocketContainer.getInstance().addMessage(message);

                System.out.printf("%s\n", message.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notify(Observable sender, Object args) {
        if (args instanceof Message) {
            Message message = (Message) args;
            UUID mId = message.getId();

            if (mId != null && id != null && id.equals(mId) == false) {
                try {
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
