import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicalRecordPanel extends JPanel {

    private JTextField pazienteIDField;
    private JTextArea diagnosiArea;
    private JTextArea trattamentoArea;
    private JButton caricaButton;
    private JButton salvaButton;
    private JButton inserisciButton;
    private String medicoID;

    public MedicalRecordPanel(String medicoID) {
        this.medicoID = medicoID; // Salva l'ID del medico loggato
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

        // Area per Diagnosi
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Diagnosi:"), gbc);

        gbc.gridx = 1;
        diagnosiArea = new JTextArea(5, 20);
        add(new JScrollPane(diagnosiArea), gbc);

        // Area per Trattamento
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Trattamento:"), gbc);

        gbc.gridx = 1;
        trattamentoArea = new JTextArea(5, 20);
        add(new JScrollPane(trattamentoArea), gbc);

        // Pulsante per Caricare la Cartella Clinica
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        caricaButton = new JButton("Carica Cartella Clinica");
        add(caricaButton, gbc);

        // Pulsante per Salvare le Modifiche
        gbc.gridy = 4;
        salvaButton = new JButton("Salva Modifiche");
        add(salvaButton, gbc);

        // Pulsante per Inserire una Nuova Cartella Clinica
        gbc.gridy = 5;
        inserisciButton = new JButton("Inserisci Nuova Cartella");
        add(inserisciButton, gbc);

        // Aggiungi ActionListener per caricare la cartella clinica
        caricaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    caricaCartellaClinica();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MedicalRecordPanel.this, "Errore nel caricamento della cartella clinica.");
                }
            }
        });

        // Aggiungi ActionListener per salvare le modifiche
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salvaModifiche();
                    JOptionPane.showMessageDialog(MedicalRecordPanel.this, "Modifiche salvate con successo!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MedicalRecordPanel.this, "Errore nel salvataggio delle modifiche.");
                }
            }
        });

        // Aggiungi ActionListener per inserire una nuova cartella clinica
        inserisciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inserisciNuovaCartella();
                    JOptionPane.showMessageDialog(MedicalRecordPanel.this, "Nuova cartella clinica inserita con successo!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MedicalRecordPanel.this, "Errore nell'inserimento della nuova cartella clinica.");
                }
            }
        });
    }

    private void caricaCartellaClinica() throws SQLException {
        String pazienteID = pazienteIDField.getText();
        String query = "SELECT Diagnosi, Trattamento FROM CartellaClinica WHERE PazienteID = ? ORDER BY DataApertura DESC";

        try (Connection conn = DatabaseConnection.getMedicoConnection(medicoID);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pazienteID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                diagnosiArea.setText(rs.getString("Diagnosi"));
                trattamentoArea.setText(rs.getString("Trattamento"));
            } else {
                JOptionPane.showMessageDialog(this, "Nessuna cartella clinica trovata per questo paziente.");
            }
        }
    }

    private void salvaModifiche() throws SQLException {
        String pazienteID = pazienteIDField.getText();
        String diagnosi = diagnosiArea.getText();
        String trattamento = trattamentoArea.getText();

        // Aggiorna solo l'ultima cartella clinica per il paziente
        String query = "UPDATE CartellaClinica SET Diagnosi = ?, Trattamento = ? WHERE CartellaID = (SELECT CartellaID FROM CartellaClinica WHERE PazienteID = ? ORDER BY DataApertura DESC LIMIT 1)";

        try (Connection conn = DatabaseConnection.getMedicoConnection(medicoID);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, diagnosi);
            stmt.setString(2, trattamento);
            stmt.setString(3, pazienteID);
            stmt.executeUpdate();
        }
    }

    private void inserisciNuovaCartella() throws SQLException {
        String pazienteID = pazienteIDField.getText();
        String diagnosi = diagnosiArea.getText();
        String trattamento = trattamentoArea.getText();

        // Inserisce una nuova cartella clinica per il paziente
        String query = "INSERT INTO CartellaClinica (PazienteID, Diagnosi, Trattamento, DataApertura) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getMedicoConnection(medicoID);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, pazienteID);
            stmt.setString(2, diagnosi);
            stmt.setString(3, trattamento);
            stmt.executeUpdate();
        }
    }
}
