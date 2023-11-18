import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.awt.*;

public class MainForm {

    private static DefaultTableModel tableModel;
    private static JTable dataTable;

    // Function untuk menyimpan data ke database
    private static void saveDataToDatabase(String nama, String jenisKelamin, String jurusan, String alamat) {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uts_pemvis", "root", "");
        String query = "INSERT INTO mahasiswa (nama, jenis_kelamin, jurusan, alamat) VALUES (?, ?, ?, ?);";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, nama);
        preparedStatement.setString(2, jenisKelamin);
        preparedStatement.setString(3, jurusan);
        preparedStatement.setString(4, alamat);
        preparedStatement.executeUpdate();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // Fungsi untuk mengambil data dari database dan disimpan di Panel Tab Data Tabel
    private static JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        // JPanel panel = new JPanel(new GridLayout(4, 1, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();

        // Nama
        JLabel nameLabel = new JLabel("Nama :");
        JTextField nameField = new JTextField(20);

        // Jenis Kelamin
        JLabel genderLabel = new JLabel("Jenis Kelamin :");
        JPanel genderPanel = new JPanel(new GridLayout(1, 3, 5, 5)); 
        JRadioButton maleRadioButton = new JRadioButton("Laki-Laki");
        JRadioButton femaleRadioButton = new JRadioButton("Perempuan");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderPanel.add(genderLabel);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);

        // Jurusan
        JLabel majorLabel = new JLabel("Jurusan :");
        String[] majors = {"", "Teknik Informatika", "Sistem Informasi", "Sistem Komputer"};
        JComboBox<String> majorComboBox = new JComboBox<>(majors);

        // Alamat
        JLabel addressLabel = new JLabel("Alamat :");
        JTextArea addressTextArea = new JTextArea(4, 20);
        JScrollPane addressScrollPane = new JScrollPane(addressTextArea);

        // Tombol "Submit"
        JButton submitButton = new JButton("Submit");

        // Tombol "Clear"
        JButton clearButton = new JButton("Clear");

        // ActionListener untuk tombol Submit dan Clear
        Dimension buttonSize = new Dimension(30, 30);
        submitButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);

        // Menambahkan action pada tombol submit
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nama = nameField.getText();
                String jenisKelamin = maleRadioButton.isSelected() ? "Laki-Laki" : "Perempuan";
                String jurusan = (String) majorComboBox.getSelectedItem();
                String alamat = addressTextArea.getText();
    
                // Simpan data ke database
                saveDataToDatabase(nama, jenisKelamin, jurusan, alamat);
    
                // Tambahkan data ke tabel
                tableModel.addRow(new Object[]{nama, jenisKelamin, jurusan, alamat});
    
                // Bersihkan input
                nameField.setText("");
                genderGroup.clearSelection();
                majorComboBox.setSelectedIndex(0);
                addressTextArea.setText("");
            }
        });

        // Menambahkan action pada tombol clear
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                genderGroup.clearSelection();
                majorComboBox.setSelectedIndex(0);
                addressTextArea.setText("");
            }
        });

        // Membuat dan mengatur panel utama di form mahasiswa
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(genderLabel, gbc);

        gbc.gridx = 1;
        panel.add(maleRadioButton, gbc);

        gbc.gridx = 2;
        panel.add(femaleRadioButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(majorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(majorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 0;
        panel.add(addressScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(clearButton, gbc);

        return panel;
    }
    
    // Function untuk menampilkan form dan panel tab
    public static void createAndShowGUI() {

        // Inisialisasi tabel untuk menampilkan data
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nama");
        tableModel.addColumn("Jenis Kelamin");
        tableModel.addColumn("Jurusan");
        tableModel.addColumn("Alamat");
        dataTable = new JTable(tableModel);

        JFrame frame = new JFrame("Master Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setMinimumSize(new Dimension(600, 450)); 
        frame.setMaximumSize(new Dimension(600, 450)); 
        frame.setPreferredSize(new Dimension(600, 450)); 

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab "Register (Mengisi data mahasiswa)"
        JPanel registerPanel = createRegisterPanel();
        tabbedPane.addTab("Form Mahasiswa", registerPanel);

        // Tab "Data Tabel (Menampilkan data mahasiswa)" 
        JPanel dataTabelPanel = new JPanel(new BorderLayout());
        dataTabelPanel.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        tabbedPane.addTab("Data Tabel", dataTabelPanel);

        frame.add(tabbedPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
    }
}