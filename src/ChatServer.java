import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static List<String> chatHistory = new ArrayList<>();
    private static final String HISTORY_FILE = "chat_history.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        loadChatHistory(); // Load chat history from file on server startup
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void broadcast(String message, ClientHandler excludeUser) {
        String messageWithTime = addTimeToMessage(message);
        chatHistory.add(messageWithTime);
        saveChatHistory(); // Save chat history after each broadcast
        for (ClientHandler client : clientHandlers) {
            if (client != excludeUser) {
                client.sendMessage(messageWithTime);
            }
        }
    }

    public static synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    public static synchronized List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    public static synchronized Set<String> getUserNames() {
        Set<String> userNames = new HashSet<>();
        for (ClientHandler client : clientHandlers) {
            userNames.add(client.getUserName());
        }
        return userNames;
    }

    // Load chat history from file
    private static void loadChatHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                chatHistory.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading chat history: " + e.getMessage());
        }
    }

    // Save chat history to file
    private static void saveChatHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (String message : chatHistory) {
                writer.write(message);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving chat history: " + e.getMessage());
        }
    }

    // Add current time to message
    public static String addTimeToMessage(String message) {
        String time = dateFormat.format(new Date());
        return "[" + time + "] " + message;
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private String userName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your name: ");
            userName = in.readLine();
            System.out.println(userName + " connected");
            ChatServer.broadcast(userName + " joined the chat", this);
            sendUserList();

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("/history")) {
                    sendChatHistory();
                } else if (message.startsWith("/add ")) {
                    // Add user logic - not specified in original request
                } else if (message.startsWith("/remove ")) {
                    // Remove user logic - not specified in original request
                } else {
                    System.out.println(userName + ": " + message);
                    ChatServer.broadcast(userName + ": " + message, this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatServer.removeClient(this);
            ChatServer.broadcast(userName + " left the chat", this);
            System.out.println(userName + " disconnected");
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendChatHistory() {
        List<String> history = ChatServer.getChatHistory();
        for (String message : history) {
            out.println(message);
        }
    }

    public void sendUserList() {
        Set<String> userNames = ChatServer.getUserNames();
        for (String userName : userNames) {
            String message = "USER: " + userName;
            out.println(ChatServer.addTimeToMessage(message));
        }
    }

    public String getUserName() {
        return userName;
    }
}
