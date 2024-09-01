import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Campo Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Campo Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Pulsante Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        // Aggiungi ActionListener per gestire il login
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login riuscito");
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtilities.invokeLater(() -> {
                new MainFrame(username,password).setVisible(true);
            });

    
        } else {
            JOptionPane.showMessageDialog(this, "Credenziali non valide");
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        // Chiamata al metodo statico senza creare un'istanza
        return DatabaseConnection.authenticate(username, password);
    }
}
