import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatClientGUI {
    private static JTextArea chatWindow;
    private static JTextArea messageInput;
    private static JList<String> userList;
    private static DefaultListModel<String> userListModel;
    private static PrintWriter out;

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Okno programu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        frame.add(mainPanel);

        // Okno rozmowy
        chatWindow = new JTextArea();
        chatWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        chatWindow.setBounds(10, 10, 550, 400); // Ustal pozycję i rozmiar zgodnie ze schematem
        chatWindow.setEditable(false); // make chat window read-only
        mainPanel.add(chatWindow);

        // Lista podłączonych użytkowników
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        userList.setBounds(570, 10, 200, 200); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(userList);

        // Okno wpisywania wiadomości
        messageInput = new JTextArea();
        messageInput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        messageInput.setBounds(10, 420, 550, 100); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(messageInput);

        // Przycisk wyślij wiadomość
        JButton sendMessageButton = new JButton("Przycisk wyślij wiadomość");
        //sendMessageButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        sendMessageButton.setBounds(10, 530, 150, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(sendMessageButton);

        // Przycisk dodaj osobę do listy kontaktów
        JButton addUserButton = new JButton("Przycisk doda osobę do listy kontaktów");
        //addUserButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        addUserButton.setBounds(570, 220, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(addUserButton);

        // Przycisk usuń z kontaktów daną osobę
        JButton removeUserButton = new JButton("Przycisk usuń z kontaktów daną osobę");
        //removeUserButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        removeUserButton.setBounds(570, 260, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(removeUserButton);

        // Pokaż historię rozmowy
        JButton showHistoryButton = new JButton("Pokaż historię rozmowy");
        //showHistoryButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        showHistoryButton.setBounds(570, 300, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(showHistoryButton);

        // Add action listeners
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        showHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestChatHistory();
            }
        });

        // Connect to the server
        connectToServer();

        // Display the frame
        frame.setVisible(true);
    }

    private static void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Request user name
            String userName = JOptionPane.showInputDialog("Enter your name:");
            out.println(userName);

            // Start a new thread to listen for messages from the server
            new Thread(new Runnable() {
                public void run() {
                    String message;
                    try {
                        while ((message = in.readLine()) != null) {
                            if (message.startsWith("USER: ")) {
                                updateUserList(message.substring(6));
                            } else {
                                chatWindow.append(message + "\n");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageInput.setText("");
        }
    }

    private static void requestChatHistory() {
        out.println("/history");
    }

    private static void updateUserList(String userName) {
        if (!userListModel.contains(userName)) {
            userListModel.addElement(userName);
        }
    }
}
