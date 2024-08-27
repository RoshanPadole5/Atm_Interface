package Atmmachine.ro;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ATMGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField amountField;
    private JLabel messageLabel;
    private Account account;

    public ATMGUI(int accountId) {
        account = new Account(accountId);

        frame = new JFrame("Indian ATM Interface");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        messageLabel = new JLabel("Welcome to the ATM");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));

        amountField = new JTextField(10);

        JButton checkBalanceButton = createButton("Check Balance", e -> showBalance());
        JButton depositButton = createButton("Deposit", e -> depositAmount());
        JButton withdrawButton = createButton("Withdraw", e -> withdrawAmount());
        JButton changePinButton = createButton("Change PIN", e -> changePin());
        JButton exitButton = createButton("Exit", e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Enter amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(checkBalanceButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(depositButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(withdrawButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(changePinButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(exitButton, gbc);

        frame.add(panel);
        frame.setVisible(true);

        authenticateUser();
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    private void authenticateUser() {
        int attempts = 0;
        while (attempts < 3) {
            String inputPin = JOptionPane.showInputDialog(frame, "Enter your PIN:");
            if (inputPin != null && !inputPin.isEmpty() && account.validatePin(Integer.parseInt(inputPin))) {
                messageLabel.setText("Authenticated. Choose your operation.");
                return;
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid PIN. Try again.");
                attempts++;
            }
        }
        JOptionPane.showMessageDialog(frame, "Too many failed attempts. Exiting.");
        System.exit(0);
    }

    private void showBalance() {
        JOptionPane.showMessageDialog(frame, "Current Balance: ₹" + account.getBalance());
    }

    private void depositAmount() {
        String input = amountField.getText();
        if (isValidAmount(input)) {
            double amount = Double.parseDouble(input);
            account.deposit(amount);
            JOptionPane.showMessageDialog(frame, "Deposited: ₹" + amount);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid deposit amount.");
        }
    }

    private void withdrawAmount() {
        String input = amountField.getText();
        if (isValidAmount(input)) {
            double amount = Double.parseDouble(input);
            if (account.withdraw(amount)) {
                JOptionPane.showMessageDialog(frame, "Withdrawn: ₹" + amount);
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient funds.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid withdrawal amount.");
        }
    }

    private void changePin() {
        String newPin = JOptionPane.showInputDialog(frame, "Enter new PIN:");
        if (newPin != null && !newPin.isEmpty() && newPin.matches("\\d{4}")) {
            account.changePin(Integer.parseInt(newPin));
            JOptionPane.showMessageDialog(frame, "PIN changed successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid PIN format. Please enter a 4-digit PIN.");
        }
    }

    private boolean isValidAmount(String input) {
        try {
            double amount = Double.parseDouble(input);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        // For demonstration, use a static account ID.
        // In a real application, this would be dynamic.
        new ATMGUI(2);
    }
}
