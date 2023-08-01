package sales;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProductSalesAnalysis extends JFrame {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnConnect;
    private final JTextArea txtResult;

    private Connection connection;

    public ProductSalesAnalysis() {
        setTitle("Product Sales Analysis");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ConnectButtonListener());

        txtResult = new JTextArea(10, 30);
        txtResult.setEditable(false);

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnConnect);
        panel.add(new JScrollPane(txtResult));

        add(panel);
    }

    private class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            try {
               // Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/jdbcdb";
                connection = DriverManager.getConnection(url, username, password);
                displayAnalysis();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ProductSalesAnalysis.this, "Error connecting to the database: " + ex.getMessage());
            }
        }
    }

    private void displayAnalysis() throws SQLException {
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Not connected to the database!");
            return;
        }

        Map<String, Double> monthlySales = new HashMap<>();
        Map<String, Integer> productSalesCount = new HashMap<>();

        // Fetch monthly sales data from the database
        String query = "SELECT DATE_FORMAT(sale_date, '%Y-%m') AS month, SUM(sale_amount) AS total_sales FROM sales GROUP BY month;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String month = resultSet.getString("month");
                double totalSales = resultSet.getDouble("total_sales");
                monthlySales.put(month, totalSales);
            }
        }

        // Fetch top-selling products from the database
        query = "SELECT product_name, COUNT(*) AS sales_count FROM sales GROUP BY product_name ORDER BY sales_count DESC LIMIT 5;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                int salesCount = resultSet.getInt("sales_count");
                productSalesCount.put(productName, salesCount);
            }
        }

        // Display the analysis results
        StringBuilder resultText = new StringBuilder("Monthly Sales:\n");
        for (String month : monthlySales.keySet()) {
            resultText.append(month).append(": $").append(monthlySales.get(month)).append("\n");
        }

        resultText.append("\nTop Selling Products:\n");
        for (String product : productSalesCount.keySet()) {
            resultText.append(product).append(": ").append(productSalesCount.get(product)).append(" sales\n");
        }

        txtResult.setText(resultText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductSalesAnalysis().setVisible(true));
    }
}

