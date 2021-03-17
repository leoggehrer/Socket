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
    private Socket socket;
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
        boolean error = false;
        String command = "";

        while (error == false && command.equals(QuitCommand) == false) {
            try {
                Message message = (Message)ois.readObject();

                System.out.printf("%s\n", message.toString());

                id = message.getId();
                command = message.getCommand();

                if (command.equals(QuitCommand)) {
                    SocketContainer.getInstance().removeObserver(this);

                    message.setCommand("disconnected");
                    message.setFrom("Server");
                    message.setBody("");
                    synchronized (this) {
                        oos.writeObject(message);
                        oos.flush();
                    }
                }
                else {
                    message.setCommand("dispatch");
                    SocketContainer.getInstance().addMessage(message);
                }
            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                error = true;
                e.printStackTrace();
            }
        }
        try {
            oos.close();
            oos = null;
            ois.close();
            ois = null;
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(Observable sender, Object args) {
        if (args instanceof Message) {
            Message message = (Message) args;
            UUID mId = message.getId();

            if (mId != null && id != null && id.equals(mId) == false) {
                try
                {
                    synchronized (this) {
                        oos.writeObject(message);
                        oos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
