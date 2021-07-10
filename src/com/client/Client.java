package com.client;

import java.io.IOException;
import java.net.Socket;
import com.chatapp.Connection;
import com.chatapp.ConsoleHelper;
import com.chatapp.Message;
import com.chatapp.MessageType;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    /*
    run() - The run method of the Client class creates a new SocketThread object. The client waits until the user enters
    a server address, port, and username. While the client is connected, it reads a String from the console. If the user
    inputs 'exit', the client is disconnected, otherwise the client sends the String to the server.
     */
    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                wait();
            } catch (Exception e) {
                notify();
                System.exit(0);
            }
        }

        if (clientConnected == true) {
            ConsoleHelper.writeMessage("com.chatapp.Connection established. To exit, enter 'exit'.");
            while (clientConnected == true) {
                String s = ConsoleHelper.readString();
                if (s.equals("exit")) {
                    break;
                }

                if (shouldSendTextFromConsole() == true) {
                    sendTextMessage(s);
                }
            }
        } else {
            ConsoleHelper.writeMessage("An error occurred while working with the com.chatapp.client.");
        }
    }

    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Enter server address.");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage("Enter the server port");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage("Enter the username");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    /*
    sendTextMessage() - This method sends a new message of type text to the server.
     */
    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage(text);
            clientConnected = false;
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public class SocketThread extends Thread {
        /*
        run() - The run method of the SocketThread class creates a new socket that binds the specified server address
        and port. A new connection is created for the socket. A handshake between the client and server takes place and the
        main loop is entered.
         */
        public void run() {
            try {
                Socket socket = new Socket(getServerAddress(), getServerPort());
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }


        }

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " has joined the chat.");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " has left the chat.");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        /*
        clientHandshake() - This method receives a message from the socket. If the received message is of type Name
        Request, the method writes a message prompting the user to enter a username to the socket.
        If the received message is of type Name Accepted, the method notifies the client that it is connected to the server.
        Otherwise, an IOException is thrown.
         */
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST) {
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    return;
                } else {
                    throw new IOException("Unexpected com.chatapp.MessageType");
                }
            }
        }

        /*
        clientMainLoop() - This method receives a message from the socket. If the message is of type Text, then the
        ConsoleHelper writes the message to the console. If the message is of type User Added the console helper outputs
        to console that the user has joined the chat. If the message is of type User Removed, the console helper outputs
        that the user has left the chat. Otherwise, an IO exception is thrown.
         */
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while(true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                } else if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                } else {
                    throw new IOException("Unexpected com.chatapp.MessageType");
                }
            }
        }
    }

}
