package Hosp.HSM;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public PatientManagementPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Contact"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField contactField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String contact = contactField.getText();
            addPatient(name, age, contact);
            loadPatients();
        });

        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        loadPatients();
    }

    private void addPatient(String name, int age, String contact) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Patients (name, age, contact) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, contact);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPatients() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Patients";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String contact = rs.getString("contact");
                tableModel.addRow(new Object[]{id, name, age, contact});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

