import javax.swing.*;


public class App {  

    public static void main(String[] args) {

        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // centra lo schermo

        LoginPanel loginPanel = new LoginPanel();
        frame.add(loginPanel);
        frame.setVisible(true);

    }
}
