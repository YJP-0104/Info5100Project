package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddCustomer extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SUCCESS_COLOR = new Color(34, 139, 34);
    private static final Color DANGER_COLOR = new Color(220, 20, 60);
    
    private JTextField nameField;
    private JTextField nationalityField;
    private JTextField idField;
    private JTextField addressField;
    private JTextField phoneField;
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private JButton saveButton;
    private JButton clearButton;
    private JPanel mainPanel;
    private JPanel formPanel;
    
    public AddCustomer() {
        initializeFrame();
        createComponents();
        setupLayout();
        setupListeners();
        finalizeFrame();
    }
    
    private void initializeFrame() {
        setTitle("Add New Customer - Air India");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void createComponents() {
        // Initialize text fields
        nameField = createStyledTextField();
        nationalityField = createStyledTextField();
        idField = createStyledTextField();
        addressField = createStyledTextField();
        phoneField = createStyledTextField();
        
        // Initialize radio buttons
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        styleRadioButton(maleRadio);
        styleRadioButton(femaleRadio);
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        
        // Initialize buttons
        saveButton = createStyledButton("Save", SUCCESS_COLOR);
        clearButton = createStyledButton("Clear", DANGER_COLOR);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private void styleRadioButton(JRadioButton radio) {
        radio.setFont(new Font("Arial", Font.PLAIN, 14));
        radio.setBackground(Color.WHITE);
        radio.setFocusPainted(false);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void setupLayout() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Add Customer Details", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form Panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form components
        addFormRow("Name:", nameField, gbc, 0);
        addFormRow("Nationality:", nationalityField, gbc, 1);
        addFormRow("ID Number:", idField, gbc, 2);
        addFormRow("Address:", addressField, gbc, 3);
        addFormRow("Phone:", phoneField, gbc, 4);
        
        // Gender panel
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        addFormRow("Gender:", genderPanel, gbc, 5);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.CYAN);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private void addFormRow(String labelText, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        formPanel.add(component, gbc);
    }
    
    private void setupListeners() {
        saveButton.addActionListener(e -> handleSave());
        clearButton.addActionListener(e -> handleClear());
    }
    
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }
        
        try {
            saveCustomerToDatabase();
            showSuccess("Customer details saved successfully!");
            dispose();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private boolean validateInputs() {
        // Validate required fields
        if (nameField.getText().trim().isEmpty() ||
            nationalityField.getText().trim().isEmpty() ||
            idField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty() ||
            (!maleRadio.isSelected() && !femaleRadio.isSelected())) {
            
            showError("Please fill in all fields!");
            return false;
        }
        
        // Validate phone number
        if (!phoneField.getText().matches("\\d{10}")) {
            showError("Please enter a valid 10-digit phone number!");
            return false;
        }
        
        return true;
    }
    
    private void saveCustomerToDatabase() throws SQLException {
        String query = "INSERT INTO passenger (name, nationality, phone, address, id, gender) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = new Conn().c;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, nationalityField.getText().trim());
            pstmt.setString(3, phoneField.getText().trim());
            pstmt.setString(4, addressField.getText().trim());
            pstmt.setString(5, idField.getText().trim());
            pstmt.setString(6, maleRadio.isSelected() ? "Male" : "Female");
            
            pstmt.executeUpdate();
        }
    }
    
    private void handleClear() {
        nameField.setText("");
        nationalityField.setText("");
        idField.setText("");
        addressField.setText("");
        phoneField.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        nameField.requestFocus();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void finalizeFrame() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new AddCustomer());
    }
}
