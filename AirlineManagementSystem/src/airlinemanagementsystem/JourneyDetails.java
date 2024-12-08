package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class JourneyDetails extends JFrame {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/airlinemanagementsytem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "yash";
    
    private JTextField pnrTextField;
    private JTable journeyTable;
    private JButton searchButton;
    private JButton clearButton;
    private JButton exportButton;
    private JPanel mainPanel;
    private JPanel searchPanel;
    
    public JourneyDetails() {
        setTitle("Journey Details - CANADA AIR");
        initializeComponents();
        setupLayout();
        setupListeners();
        finalizeFrame();
    }
    
    private void initializeComponents() {
        // Initialize main panels
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(new Color(240, 240, 240));
        
        // Create and style components
        JLabel titleLabel = new JLabel("Journey Information Search");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        JLabel pnrLabel = new JLabel("PNR Number:");
        pnrLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        pnrTextField = new JTextField(15);
        pnrTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        pnrTextField.setBorder(BorderFactory.createCompoundBorder(
            pnrTextField.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Initialize buttons
        searchButton = createStyledButton("Search", new Color(0, 102, 204));
        clearButton = createStyledButton("Clear", new Color(128, 128, 128));
        exportButton = createStyledButton("Export", new Color(0, 153, 51));
        
        // Initialize table
        journeyTable = new JTable();
        journeyTable.setFont(new Font("Arial", Font.PLAIN, 12));
        journeyTable.setRowHeight(25);
        journeyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        journeyTable.getTableHeader().setBackground(new Color(51, 51, 51));
        journeyTable.getTableHeader().setForeground(Color.WHITE);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void setupLayout() {
        // Add components to search panel
        searchPanel.add(new JLabel("PNR Number:"));
        searchPanel.add(pnrTextField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(exportButton);
        
        // Create table panel
        JScrollPane tableScrollPane = new JScrollPane(journeyTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Add components to main panel
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel("Journey Information Search", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }
    
    private void setupListeners() {
        searchButton.addActionListener(e -> searchJourneyDetails());
        clearButton.addActionListener(e -> clearForm());
//        exportButton.addActionListener(e -> exportToExcel());
        
        // Add key listener for Enter key
        pnrTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchJourneyDetails();
                }
            }
        });
    }
    
    private void searchJourneyDetails() {
        String pnrNumber = pnrTextField.getText().trim();
        if (pnrNumber.isEmpty()) {
            showError("PNR Required", "Please enter a PNR number to search.");
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reservation WHERE PNR = ?")) {
            
            pstmt.setString(1, pnrNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                showWarning("No Results", "No journey details found for PNR: " + pnrNumber);
                return;
            }
            
            updateTableWithResults(rs);
            
        } catch (SQLException ex) {
            showError("Database Error", "Error accessing database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void updateTableWithResults(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add columns
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(formatColumnName(metaData.getColumnName(i)));
        }
        
        // Add rows
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }
        
        journeyTable.setModel(model);
        adjustColumnWidths();
    }
    
    private void adjustColumnWidths() {
        TableColumnModel columnModel = journeyTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            int width = 100; // minimum width
            column.setPreferredWidth(width);
        }
    }
    
    private String formatColumnName(String columnName) {
        return columnName.substring(0, 1).toUpperCase() + 
               columnName.substring(1).toLowerCase().replace("_", " ");
    }
    
    private void clearForm() {
        pnrTextField.setText("");
        ((DefaultTableModel) journeyTable.getModel()).setRowCount(0);
        pnrTextField.requestFocus();
    }
    
//    private void exportToExcel() {
//        // TODO: Implement export functionality
//        showInfo("Export", "Export functionality will be available in the next update.");
//    }
    
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void finalizeFrame() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new JourneyDetails().setVisible(true);
        });
    }
}
