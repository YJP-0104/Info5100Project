package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {
    private static final String AIRLINE_NAME = "CANADA AIR";
    private static final String APP_TITLE = "Airline Management System";
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JMenuBar navigationBar;
    
    public Home() {
        initializeFrame();
        setupMainPanel();
        createHeader();
        createImageCenter(); // Add this line
        createNavigationMenu();
        createFooter();
        finalizeFrame();
    }
    
    private void initializeFrame() {
        setTitle(APP_TITLE + " - Home");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void setupMainPanel() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);
    }
    
    private void createHeader() {
        welcomeLabel = new JLabel(AIRLINE_NAME + " WELCOMES YOU");
        welcomeLabel.setBounds(450, 50, 800, 50);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setFont(new Font("Montserrat", Font.BOLD, 36));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel);
    }
    
    private void createNavigationMenu() {
        navigationBar = new JMenuBar();
        navigationBar.setBackground(SECONDARY_COLOR);
        navigationBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Details Menu
        JMenu detailsMenu = createMenu("AIRLINE SERVICES", "Arial", 16);
        JMenuItem flightDetailsItem = addMenuItem(detailsMenu, "Flight Details", "View all flight information");
        addMenuItem(detailsMenu, "Add Customer Details", "Register new customer");
        addMenuItem(detailsMenu, "Book Flight", "Book a new flight ticket");
        addMenuItem(detailsMenu, "Journey Details", "View journey information");
        addMenuItem(detailsMenu, "Cancel Ticket", "Cancel existing ticket");
        
        // Ticket Menu
        JMenu ticketMenu = createMenu("TICKET SERVICES", "Arial", 16);
        addMenuItem(ticketMenu, "Boarding Pass", "Generate boarding pass");
        
        navigationBar.add(detailsMenu);
        navigationBar.add(ticketMenu);
        setJMenuBar(navigationBar);
    }
    
    private JMenu createMenu(String title, String fontName, int fontSize) {
        JMenu menu = new JMenu(title);
        menu.setFont(new Font(fontName, Font.BOLD, fontSize));
        menu.setForeground(Color.BLACK);
        return menu;
    }
    
    private JMenuItem addMenuItem(JMenu menu, String title, String tooltip) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setFont(new Font("Arial", Font.PLAIN, 14));
        menuItem.setToolTipText(tooltip);
        menuItem.addActionListener(this);
        menuItem.setBackground(Color.BLACK);
        menu.add(menuItem);
        return menuItem;
    }
    
    private void createFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 245));
        footerPanel.setBounds(0, 700, 1600, 40);
        
        JLabel copyrightLabel = new JLabel("© " + 
            java.time.Year.now().getValue() + " " + AIRLINE_NAME + 
            " Management System | All Rights Reserved");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        copyrightLabel.setForeground(SECONDARY_COLOR);
        
        footerPanel.add(copyrightLabel);
        mainPanel.add(footerPanel);
    }
    
    private void finalizeFrame() {
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        
        try {
            switch (command) {
                case "Flight Details":
                    SwingUtilities.invokeLater(() -> {
                        new FlightInfo().setVisible(true);
                    });
                    break;
                case "Add Customer Details":
                    SwingUtilities.invokeLater(() -> {
                        new AddCustomer().setVisible(true);
                    });
                    break;
                case "Book Flight":
                    SwingUtilities.invokeLater(() -> {
                        new BookFlight().setVisible(true);
                    });
                    break;
                case "Journey Details":
                    SwingUtilities.invokeLater(() -> {
                        new JourneyDetails().setVisible(true);
                    });
                    break;
                case "Cancel Ticket":
                    SwingUtilities.invokeLater(() -> {
                        new Cancel().setVisible(true);
                    });
                    break;
                case "Boarding Pass":
                    SwingUtilities.invokeLater(() -> {
                        new BoardingPass().setVisible(true);
                    });
                    break;
                default:
                    showError("Invalid Option", "The selected option is not valid.");
                    break;
            }
        } catch (Exception e) {
            showError("System Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Use EventQueue to ensure thread safety
            EventQueue.invokeLater(() -> {
                new Home();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createImageCenter() {
    // Load the image
    ImageIcon icon = new ImageIcon("E:\\\\NEU LABS\\\\Info5100\\\\Project\\\\info5100Project\\\\AirlineManagementSystem\\\\src\\\\airlinemanagementsystem\\\\logo.jpg"); // Replace with the actual path to your image
    
    int newWidth = 400;  // Desired width
    int newHeight = 300; // Desired height
    Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    ImageIcon resizedIcon = new ImageIcon(scaledImage);

    // Add the resized image to a JLabel
    JLabel imageLabel = new JLabel(resizedIcon);

    // Center the image in the panel
    imageLabel.setBounds(
        (mainPanel.getWidth() - newWidth) / 2,
        (mainPanel.getHeight() - newHeight) / 2,
        newWidth,
        newHeight
    );
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Add to the main panel
    mainPanel.add(imageLabel);

    // Repaint the panel to reflect changes
    mainPanel.repaint();
}

}