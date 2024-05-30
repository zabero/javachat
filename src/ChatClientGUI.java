import javax.swing.*;
import java.awt.*;

public class ChatClientGUI {
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
        JTextArea chatWindow = new JTextArea();
        chatWindow.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        chatWindow.setBounds(10, 10, 550, 400); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(chatWindow);

        // Lista podłączonych użytkowników
        JList<String> userList = new JList<>();
        userList.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        userList.setBounds(570, 10, 200, 200); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(userList);

        // Okno wpisywania wiadomości
        JTextArea messageInput = new JTextArea();
        messageInput.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        messageInput.setBounds(10, 420, 550, 100); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(messageInput);

        // Przycisk wyślij wiadomość
        JButton sendMessageButton = new JButton("Przycisk wyślij wiadomość");
        sendMessageButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
        sendMessageButton.setBounds(10, 530, 150, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(sendMessageButton);

        // Przycisk dodaj osobę do listy kontaktów
        JButton addUserButton = new JButton("Przycisk doda osobę do listy kontaktów");
        addUserButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        addUserButton.setBounds(570, 220, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(addUserButton);

        // Przycisk usuń z kontaktów daną osobę
        JButton removeUserButton = new JButton("Przycisk usuń z kontaktów daną osobę");
        removeUserButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        removeUserButton.setBounds(570, 260, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(removeUserButton);

        // Pokaż historię rozmowy
        JButton showHistoryButton = new JButton("Pokaż historię rozmowy");
        showHistoryButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
        showHistoryButton.setBounds(570, 300, 200, 30); // Ustal pozycję i rozmiar zgodnie ze schematem
        mainPanel.add(showHistoryButton);

        // Display the frame
        frame.setVisible(true);
    }
}
