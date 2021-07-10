# Chat Application

# General Info 
This is a chat application created in Java. It consists of a server and multiple clients. The chat app is console based as well as gui based. Users may send text messages via the console and the messages are broadcasted to all client connections.

# Screenshots 

![Screenshot from 2021-06-21 13-56-10](https://user-images.githubusercontent.com/49923044/122811671-c5cacc80-d29e-11eb-86dd-069a62d16b75.png)

![Screenshot from 2021-06-21 14-36-36](https://user-images.githubusercontent.com/49923044/122811697-cd8a7100-d29e-11eb-8a7c-50481a605fe4.png)

![Screenshot from 2021-06-21 14-36-54](https://user-images.githubusercontent.com/49923044/122811710-d11df800-d29e-11eb-92f5-efccc0924c6e.png)

![Screenshot from 2021-06-21 14-38-04](https://user-images.githubusercontent.com/49923044/122811719-d3805200-d29e-11eb-9633-aa946552883a.png)

![Screenshot from 2021-06-21 14-38-23](https://user-images.githubusercontent.com/49923044/122811725-d54a1580-d29e-11eb-80cd-86d4128e8e21.png)

![Screenshot from 2021-06-21 14-38-41](https://user-images.githubusercontent.com/49923044/122811732-d713d900-d29e-11eb-8ccb-aa5232974e54.png)

![Screenshot from 2021-06-21 14-39-50](https://user-images.githubusercontent.com/49923044/122811752-dc712380-d29e-11eb-9c02-ff1a0d0aa393.png)

![Screenshot from 2021-06-21 14-40-10](https://user-images.githubusercontent.com/49923044/122811764-e004aa80-d29e-11eb-8954-410d13feb502.png)


# Technologies
1) Java programming language
3) Model View Controller design pattern
4) Client-server architecture
5) Socket programming
6) Java swing

# Setup

# Features
* Server
  * The Server class represents the server
  * Contains a Handler class which handles all the communcation between the client and server
  * Creates a server socket which waits for client connections, accepts the connection, and creates a socket for the connection
  * The socket is passed to a handler thread
  * The handler thread performs a server handshake which generates a username request and gets a response from the client. The new user is added to a map of connections.
  * The handler notifies all the clients that the new user has joined the chat
  * The server receives text messages in a loop 
* Client
  * The Client class respresents clients
  * Contains a SocketThread class which is a helper class that is responsible for creating a socket, performing a client handshake, and receiving messages from the socket
  * The client creates a socket thread, runs it, and waits for the socket thread to complete
  * The socket thread prompts the user to enter an ip address and port
  * It creates a new socket which binds the specified ip address to the specified port as well as a new connection with the newly created socket
  * The socket thread performs a client handshake during which the client receives a message from the server
  * If the message is of type name request, the socket thread sends a username response
  * If the message is of type name accepted, the socket thread notifies the client that it is connected to the server
  * The socket thread receives messages in a loop and processes them according to their message type
  * After the socket thread completes, the client receives user input via the console and as long as the user does not enter exit, it sends the text to the server
* Message
  * Represents a message
  * Can be of several different types such as Name Request, User Name, Name Accepted, Text, User Added, User Removed
* Connection
  * A wrapper around a socket
  * Represents a connection between a client and a server
  * Messages can be sent and received via the connection
* Bot Client
  * A chat bot that understands and responds to the following commands: date, day, month, year, time, hour, minutes, seconds
  * Contains a bot socket thread which processes messages
* GUI
  * The Model View Controller design pattern is used as the architectural framework for the GUI
  * The user will see 3 windows: A window that prompts the user to enter the server address, one that prompts the user to enter the server port, and one that prompts the user to enter a user name
  * The various usernames are displayed in a scroll pane to the right
  * Users may enter text at the top of the window
  * Text messages are displayed in a scroll pane to the left

# Status
Completed

# Inspiration
I wanted to learn how the client-server architecture worked. I also wanted to learn socket programming.
