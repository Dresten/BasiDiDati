import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/Gestore_Ospedaliero";
    private static final String USER = "app_user";
    private static final String PASSWORD = "Tua_password_sicura1";

    // Registrare il driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC driver not found.");
        }
    }

    public static Connection getConnection(String user, String password) throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }

    public static boolean authenticate(String medicoID, String password) {
        String query = "SELECT password FROM Medico WHERE MedicoID = ?";
        try (Connection conn = getConnection(USER, PASSWORD); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, medicoID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return password.equals(storedPassword);
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getMedicoConnection(String medicoID) throws SQLException {
        // Recupera la password per l'utente medico e crea una connessione con credenziali diverse
        String query = "SELECT password FROM Medico WHERE MedicoID = ?";
        try (Connection conn = getConnection(USER, PASSWORD); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, medicoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return getConnection(medicoID, storedPassword);
            } else {
                throw new SQLException("MedicoID non trovato.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
