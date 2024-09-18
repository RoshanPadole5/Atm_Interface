package Hosp.HSM;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public AppointmentManagementPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient ID", "Date", "Doctor"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JTextField patientIdField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField doctorField = new JTextField();

        inputPanel.add(new JLabel("Patient ID:"));
        inputPanel.add(patientIdField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Doctor:"));
        inputPanel.add(doctorField);

        JButton addButton = new JButton("Add Appointment");
        addButton.addActionListener((ActionEvent e) -> {
            int patientId = Integer.parseInt(patientIdField.getText());
            String date = dateField.getText();
            String doctor = doctorField.getText();
            addAppointment(patientId, date, doctor);
            loadAppointments();
        });

        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);

        loadAppointments();
    }

    private void addAppointment(int patientId, String date, String doctor) {
        try (Connection conn = DatabaseConnection.getConnection1()) {
            String sql = "INSERT INTO Appointments (patient_id, appointment_date, doctor_name) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);
            stmt.setString(2, date);
            stmt.setString(3, doctor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAppointments() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Appointments";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                String date = rs.getString("appointment_date");
                String doctor = rs.getString("doctor_name");
                tableModel.addRow(new Object[]{id, patientId, date, doctor});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
