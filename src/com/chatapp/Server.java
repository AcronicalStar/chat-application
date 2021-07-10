package com.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /*
    sendBroadcastMessage() - This method sends the passed in message to all client connections in the connectionMap.
     */
    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> pair : connectionMap.entrySet()) {
            try {
                pair.getValue().send(message);
            } catch (IOException e) {
                System.out.println("The message could not be sent.");
            }
        }
    }

    /*
    The user will be prompted to enter a port number after which a new server socket bound to the specified
    port will be created. The server socket listens for client connections in a loop and accepts them.
    The method creates a new Handler thread with a socket passed in and runs the Handler. The Handler thread is
    responsible for messages exchanged with the client.
     */
    public static void main(String[] args) throws IOException {
        ConsoleHelper.writeMessage("Enter port number");
        int port = ConsoleHelper.readInt();
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(port);
        System.out.println("The server is running...");
        while(true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                serverSocket.close();
                System.out.println("Error");
                break;
            }
            Handler handler = new Handler(socket);
            handler.start();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        /*
        run() - The run method of the Handler class.
         */
        public void run() {
            ConsoleHelper.writeMessage("A new connection was established with" + socket.getRemoteSocketAddress());
            Connection connection = null;
            String userName = "";
            try {
                connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);
                if (userName != null) {
                    connectionMap.remove(userName);
                }
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            ConsoleHelper.writeMessage("The connection with " + socket.getRemoteSocketAddress() + " is closed.");
        }

        /*
        serverHandshake() - This is where the server meets the client. The method takes a connection as an argument and
        generates a username request. It gets the client's response. If the response has a username, the method extracts
        the name from the response and verifies that it's neither empty nor already being used. The new user is added
        to the connection map and the client is informed that the name has been accepted. Finally, the method returns
        the name.
         */
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message response = connection.receive();
                if (response.getType().equals(MessageType.USER_NAME)) {
                    String name = response.getData();
                    if (name != null && !name.equals("")) {
                        if (!connectionMap.containsKey(name)) {
                            connectionMap.put(name, connection);
                            connection.send(new Message(MessageType.NAME_ACCEPTED));
                            return name;
                        }
                    }
                }
            }
        }

        /*
        notifyUsers() - This method sends information to the new client about the other existing clients. It iterates
        over the connectionMap and gets the usernames of each client. The method generates and sends a user added
        message to every client but the user whose name is equal to userName.
         */
        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection> pair : connectionMap.entrySet()) {
                String name = pair.getKey();
                if (name != userName) {
                    connection.send(new Message(MessageType.USER_ADDED, name));
                }
            }
        }

        /*
        serverMainLoop() - This method receives client messages in an infinite loop. If the message is a text message,
        it generates a new text message and sends it to all clients. If the message is not a text message, an error
        message is displayed.
         */
        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType().equals(MessageType.TEXT)) {
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + message.getData()));
                } else {
                    ConsoleHelper.writeMessage("Error!");
                }

            }
        }
    }

}