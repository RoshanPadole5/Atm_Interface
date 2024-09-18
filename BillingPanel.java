package Hosp.HSM;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public BillingPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient ID", "Amount", "Date"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JTextField patientIdField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();

        inputPanel.add(new JLabel("Patient ID:"));
        inputPanel.add(patientIdField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);

        JButton addButton = new JButton("Add Billing");
        addButton.addActionListener((ActionEvent e) -> {
            int patientId = Integer.parseInt(patientIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();
            addBilling(patientId, amount, date);
            loadBillings();
        });

        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        loadBillings();
    }

    private void addBilling(int patientId, double amount, String date) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Billing (patient_id, amount, billing_date) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBillings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Billing";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                double amount = rs.getDouble("amount");
                String date = rs.getString("billing_date");
                tableModel.addRow(new Object[]{id, patientId, amount, date});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
