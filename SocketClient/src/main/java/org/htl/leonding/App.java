package org.htl.leonding;

import org.htl.leonding.logic.SocketListener;
import org.htl.leonding.models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "SocketClient-App is running..." );

        Socket socket = new Socket("localhost", 4444);
        SocketListener socketListener = new SocketListener(socket);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "", body = "", from = "";

        System.out.printf("Name: ");
        from = br.readLine();
        System.out.println("Now you can write:");

        socketListener.start();

        while (command.equals("quit") == false) {
            command = br.readLine();
            if (command.equals("quit") == false) {
                body = command;
                command = "text";
            }
            Message message = new Message();

            message.setCommand(command);
            message.setFrom(from);
            message.setBody(body);

            socketListener.writeMessage(message);
        }
    }
}
