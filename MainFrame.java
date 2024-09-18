package Hosp.HSM;

	import javax.swing.*;
	import java.awt.*;

	public class MainFrame extends JFrame {
	    public MainFrame() {
	        setTitle("Hospital Management System");
	        setSize(1024, 768);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null); // Center the window

	        // Create a tabbed pane for different modules
	        JTabbedPane tabbedPane = new JTabbedPane();
	        tabbedPane.addTab("Patients", new PatientManagementPanel());
	        tabbedPane.addTab("Appointments", new AppointmentManagementPanel());
	        tabbedPane.addTab("Billing", new BillingPanel());

	        setContentPane(tabbedPane);
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            MainFrame mainFrame = new MainFrame();
	            mainFrame.setVisible(true);
	        });
	    }
	}

}
