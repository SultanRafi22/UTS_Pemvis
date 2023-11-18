import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class MainFrame extends JFrame{
    public static void main(String[] args) {

        // Membuat frame
        JFrame frame = new JFrame("Form Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);

        // Membuat panel utama dengan BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Membuat panel dengan GridBagLayout untuk gambar
        JPanel imagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Membuat label untuk gambar UNSRI
        JLabel imageLabel = new JLabel(new ImageIcon("icon/unsri.jpeg"));

        // Mengatur grid constraints untuk gambar
        gbc.gridx = 0;
        gbc.gridy = 0;
        imagePanel.add(imageLabel, gbc);

        // Membuat panel dengan GridBagLayout untuk form login
        JPanel formPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        
        // Membuat label dan textfield untuk username
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField(20);

        // Membuat label dan textfield untuk password
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);

        // Mengatur grid constraints untuk form login
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(userTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        // Membuat panel untuk tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Membuat tombol "Submit", "Clear", dan "Edit"
        JButton submitButton = new JButton("Submit");
        JButton clearButton = new JButton("Clear");
        JButton exitButton = new JButton("Exit");

        // Menambahkan tombol ke panel tombol
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText();
                char[] passwordChars = passField.getPassword();
                String password = new String(passwordChars);

                // Buat koneksi ke database
                String jdbcURL = "jdbc:mysql://localhost:3306/uts_pemvis"; 
                String dbUsername = "root"; 
                String dbPassword = ""; 

                try {
                    Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);

                    // Buat statement SQL
                    String query = "SELECT * FROM login WHERE username=? AND password=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    // Eksekusi query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Periksa hasil query
                    if (resultSet.next()) {
                        resultSet.close();
                        preparedStatement.close();
                        connection.close();
                        Arrays.fill(passwordChars, '0');
                        passField.setText("");
                        frame.dispose();
                        // MainForm welcomeFrame = new MainForm(username);
                        MainForm.main(null);

                    } else {
                        JOptionPane.showMessageDialog(formPanel, "Login Gagal. Silakan coba lagi.");
                    }

                    // Tutup koneksi ke database
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(formPanel, "Kesalahan Koneksi Database.");
                }

                // Hapus kata sandi dari memori dan hapus isi dari password field
                Arrays.fill(passwordChars, '0');
                passField.setText(""); 
            }
        });

        // Menambahkan action pada tombol Clear
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userTextField.setText("");
                passField.setText("");
            }
        });

        // Menambahkan action pada tombol Exit
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Menambahkan komponen ke panel utama 
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
        mainPanel.add(imagePanel);
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        mainPanel.setBorder(new EmptyBorder(3, 3, 3, 3)); 

        // Menambahkan panel utama ke frame
        frame.add(mainPanel);

        // Memposisikan frame ke tengah layar
        frame.setLocationRelativeTo(null);

        // Menampilkan frame
        frame.setVisible(true);
    }
}