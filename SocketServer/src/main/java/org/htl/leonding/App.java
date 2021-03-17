package org.htl.leonding;

import org.htl.leonding.logic.SocketContainer;
import org.htl.leonding.logic.SocketThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "SocketServer-App is running..." );

        try (ServerSocket ss = new ServerSocket(4444)) {
            while (true) {
                Socket socket = ss.accept();
                SocketThread socketThread = new SocketThread(socket);

                SocketContainer.getInstance().addObserver(socketThread);
                socketThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
