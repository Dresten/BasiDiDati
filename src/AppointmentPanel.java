import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentPanel extends JPanel {

    private JTextField pazienteIDField;
    private JTextField orarioField;
    private JTextField tipoVisitaField;
    private JButton fissareButton;
    private String medicoID;

    public AppointmentPanel(String medicoID) {
        this.medicoID = medicoID;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campo per ID Paziente
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID Paziente:"), gbc);

        gbc.gridx = 1;
        pazienteIDField = new JTextField(15);
        add(pazienteIDField, gbc);

        // Campo per Orario
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Orario (YYYY-MM-DD HH:MM):"), gbc);

        gbc.gridx = 1;
        orarioField = new JTextField(15);
        add(orarioField, gbc);

        // Campo per Tipo Visita
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Tipo Visita:"), gbc);

        gbc.gridx = 1;
        tipoVisitaField = new JTextField(15);
        add(tipoVisitaField, gbc);

        // Pulsante per Fissare Appuntamento
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        fissareButton = new JButton("Fissare Appuntamento");
        add(fissareButton, gbc);

        // Aggiungi ActionListener per fissare l'appuntamento
        fissareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fissareAppuntamento();
                    JOptionPane.showMessageDialog(AppointmentPanel.this, "Appuntamento fissato con successo!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AppointmentPanel.this, "Errore nella fissazione dell'appuntamento.");
                }
            }
        });
    }

    private void fissareAppuntamento() throws SQLException {
        String pazienteID = pazienteIDField.getText();
        String orario = orarioField.getText();
        String tipoVisita = tipoVisitaField.getText();

        String query = "INSERT INTO Appuntamento (OrarioAppuntamento, TipoVisita, PazienteID, MedicoID) " +
                       "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getMedicoConnection(medicoID);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orario);
            stmt.setString(2, tipoVisita);
            stmt.setString(3, pazienteID);
            stmt.setString(4, medicoID); // Utilizzo dell'ID del medico

            stmt.executeUpdate();
        }
    }
}


