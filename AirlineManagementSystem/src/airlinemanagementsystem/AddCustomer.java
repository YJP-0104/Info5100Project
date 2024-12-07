package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AddCustomer - A UI for adding customer details in the Airline Management System.
 * 
 * @author yashpatel
 */
public class AddCustomer extends JFrame implements ActionListener {
    JTextField tfName, tfPhone, tfid, tfNationality, tfAddress;
    JRadioButton rbMale, rbFemale;
    JButton saveButton, clearButton;

    public AddCustomer() {
        // Set layout and background
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Header
        JLabel heading = new JLabel("Add Customer Details");
        heading.setBounds(280, 20, 500, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(new Color(0, 102, 204)); // Blue color
        add(heading);

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(60, 100, 150, 25);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(220, 100, 200, 25);
        add(tfName);

        // Nationality
        JLabel lblNationality = new JLabel("Nationality:");
        lblNationality.setBounds(60, 150, 150, 25);
        lblNationality.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblNationality);

        tfNationality = new JTextField();
        tfNationality.setBounds(220, 150, 200, 25);
        add(tfNationality);

        // Aadhar Number
        JLabel lblAadhar = new JLabel("ID Number:");
        lblAadhar.setBounds(60, 200, 150, 25);
        lblAadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblAadhar);

        tfid = new JTextField();
        tfid.setBounds(220, 200, 200, 25);
        add(tfid);

        // Address
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(60, 250, 150, 25);
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblAddress);

        tfAddress = new JTextField();
        tfAddress.setBounds(220, 250, 200, 25);
        add(tfAddress);

        // Gender
        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(60, 300, 150, 25);
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblGender);

        ButtonGroup genderGroup = new ButtonGroup();

        rbMale = new JRadioButton("Male");
        rbMale.setBounds(220, 300, 80, 25);
        rbMale.setBackground(Color.WHITE);
        rbMale.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(rbMale);

        rbFemale = new JRadioButton("Female");
        rbFemale.setBounds(310, 300, 100, 25);
        rbFemale.setBackground(Color.WHITE);
        rbFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(rbFemale);

        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);

        // Phone
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(60, 350, 150, 25);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(220, 350, 200, 25);
        add(tfPhone);

        // Buttons
        saveButton = new JButton("Save");
        saveButton.setBounds(220, 420, 100, 30);
        saveButton.setBackground(new Color(34, 139, 34)); // Green
        saveButton.setForeground(Color.BLUE);
        saveButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        saveButton.addActionListener(this);
        add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(340, 420, 100, 30);
        clearButton.setBackground(new Color(220, 20, 60)); // Red
        clearButton.setForeground(Color.BLUE);
        clearButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        clearButton.addActionListener(this);
        add(clearButton);

        // Optional: Add a background image
//        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/customer.png"));
//        JLabel image = new JLabel(imageIcon);
//        image.setBounds(450, 80, 400, 400);
//        add(image);

        // JFrame settings
        setTitle("Add Customer");
        setSize(900, 600);
        setLocation(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveButton) {
            // Retrieve values from input fields
            String name = tfName.getText();
            String nationality = tfNationality.getText();
            String phone = tfPhone.getText();
            String address = tfAddress.getText();
            String id = tfid.getText();
            String gender = rbMale.isSelected() ? "Male" : (rbFemale.isSelected() ? "Female" : null);

            // Input validation
            if (name.isEmpty() || nationality.isEmpty() || phone.isEmpty() || address.isEmpty() || id.isEmpty() || gender == null) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Save to database
                Conn conn = new Conn();
                String query = "INSERT INTO passenger(name, nationality, phone, address, id, gender) " +
                        "VALUES ('" + name + "', '" + nationality + "', '" + phone + "', '" + address + "', '" + id + "', '" + gender + "')";
                conn.s.executeUpdate(query);

                // Show success message
                JOptionPane.showMessageDialog(null, "Customer Details Added Successfully!");
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving data. Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == clearButton) {
            // Clear input fields
            tfName.setText("");
            tfNationality.setText("");
            tfPhone.setText("");
            tfAddress.setText("");
            tfid.setText("");
            rbMale.setSelected(false);
            rbFemale.setSelected(false);
        }
    }

    public static void main(String[] args) {
        new AddCustomer();
    }
}
