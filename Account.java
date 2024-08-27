package Atmmachine.ro;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private int accountId;
    private double balance;
    private int pin;

    public Account(int accountId) {
        this.accountId = accountId;
        loadAccountDetails();
    }

    private void loadAccountDetails() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM accounts WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.accountId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.balance = resultSet.getDouble("balance");
                this.pin = resultSet.getInt("pin");
            } else {
                throw new SQLException("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public double getBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            updateBalance();
            recordTransaction("Deposit", amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            updateBalance();
            recordTransaction("Withdrawal", amount);
            return true;
        }
        return false;
    }

    public void changePin(int newPin) {
        this.pin = newPin;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE accounts SET pin = ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.pin);
            statement.setInt(2, this.accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBalance() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, this.balance);
            statement.setInt(2, this.accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordTransaction(String type, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.accountId);
            statement.setString(2, type);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
