import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private String medicoID;
    private String password;

    public MainFrame(String medicoID, String password) {
        this.medicoID = medicoID; 
        this.password = password;// Salva l'ID e pwd del medico loggato
        setTitle("Sistema di Gestione Ospedaliero");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crea un layout di base
        setLayout(new BorderLayout());

        // Crea e aggiungi i pannelli
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Fissare Appuntamenti", new AppointmentPanel(medicoID));
        tabbedPane.addTab("Cartella Clinica", new MedicalRecordPanel(medicoID));

        add(tabbedPane, BorderLayout.CENTER);
    }
}

