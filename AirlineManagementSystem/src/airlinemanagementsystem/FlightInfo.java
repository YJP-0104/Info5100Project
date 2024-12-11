package airlinemanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class FlightInfo extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airlinemanagementsystem?useSSL=false";
    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "harsh11";
//    private static final String DB_PASSWORD = "yash";
    private static final String DB_PASSWORD = "my-secret-pw";

    
    private JTable flightTable;
    private JTable reservationTable;
    private DefaultTableModel flightTableModel;
    private DefaultTableModel reservationTableModel;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    
    public FlightInfo() {
        setTitle("Flight Information System");
        setupUI();
        loadFlightData();
        loadReservationData();
    }
    
    private void setupUI() {
        // Set font size globally
        Font tabFont = new Font("Arial", Font.BOLD, 18);
        UIManager.put("TabbedPane.font", tabFont);
        
        // Main panel setup
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title setup
        titleLabel = new JLabel("Flight Information Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Tabbed Pane setup
        tabbedPane = new JTabbedPane();
        
        // Flight Table setup
        flightTable = createStyledTable();
        flightTableModel = (DefaultTableModel) flightTable.getModel();
        JScrollPane flightScrollPane = new JScrollPane(flightTable);
        
        // Reservation Table setup
        reservationTable = createStyledTable();
        reservationTableModel = (DefaultTableModel) reservationTable.getModel();
        JScrollPane reservationScrollPane = new JScrollPane(reservationTable);
        
        // Add tabs
        tabbedPane.addTab("Flight Details", flightScrollPane);
        tabbedPane.addTab("Reservation Details", reservationScrollPane);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Frame setup
        setContentPane(mainPanel);
        setSize(1200, 700);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private JTable createStyledTable() {
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table.setModel(model);
        
        // Table styling
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Table header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(51, 51, 51));
        header.setForeground(Color.BLACK);
        
        return table;
    }
    
    private void loadFlightData() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM flight")) {
            
            // Get metadata and set up columns
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Clear existing columns and data
            flightTableModel.setColumnCount(0);
            flightTableModel.setRowCount(0);
            
            // Add columns
            for (int i = 1; i <= columnCount; i++) {
                flightTableModel.addColumn(formatColumnName(metaData.getColumnName(i)));
            }
            
            // Add rows
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                flightTableModel.addRow(rowData);
            }
            
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Failed to load flight data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadReservationData() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation")) {
            
            // Get metadata and set up columns
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Clear existing columns and data
            reservationTableModel.setColumnCount(0);
            reservationTableModel.setRowCount(0);
            
            // Add columns
            for (int i = 1; i <= columnCount; i++) {
                reservationTableModel.addColumn(formatColumnName(metaData.getColumnName(i)));
            }
            
            // Add rows
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                reservationTableModel.addRow(rowData);
            }
            
        } catch (SQLException e) {
            showErrorDialog("Database Error", "Failed to load reservation data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String formatColumnName(String columnName) {
        // Convert database column names to more readable format
        return columnName.substring(0, 1).toUpperCase() + 
               columnName.substring(1).toLowerCase().replace("_", " ");
    }
    
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            FlightInfo flightInfo = new FlightInfo();
            flightInfo.setVisible(true);
        });
    }
}