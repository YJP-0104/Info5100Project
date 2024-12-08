package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.util.Random;

public class BookFlight extends JFrame implements ActionListener {
    private JTextField idTextField;
    private JLabel nameLabel, nationalityLabel, addressLabel, genderLabel, flightNameLabel, flightCodeLabel;
    private JButton fetchUserButton, fetchFlightsButton, bookFlightButton;
    private Choice sourceChoice, destinationChoice;
    private JDateChooser travelDateChooser;

    public BookFlight() {
        // Frame settings
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        setTitle("Book Flight");
        setSize(1100, 700);
        setLocation(200, 50);

        // Heading
        JLabel heading = new JLabel("Book Flight");
        heading.setBounds(420, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.BOLD, 32));
        heading.setForeground(Color.BLUE);
        add(heading);

        // Aadhar field
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(60, 80, 150, 25);
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(idLabel);

        idTextField = new JTextField();
        idTextField.setBounds(220, 80, 150, 25);
        add(idTextField);

        fetchUserButton = new JButton("Fetch User");
        fetchUserButton.setBounds(380, 80, 120, 25);
        fetchUserButton.setBackground(Color.BLACK);
        fetchUserButton.setForeground(Color.BLUE);
        fetchUserButton.addActionListener(this);
        add(fetchUserButton);

        // User information fields
        addField("Name", 130);
        nameLabel = addDynamicLabel(130);

        addField("Nationality", 180);
        nationalityLabel = addDynamicLabel(180);

        addField("Address", 230);
        addressLabel = addDynamicLabel(230);

        addField("Gender", 280);
        genderLabel = addDynamicLabel(280);

        // Flight source and destination
        addField("Source", 330);
        sourceChoice = addChoice(330);

        addField("Destination", 380);
        destinationChoice = addChoice(380);

        fetchFlightsButton = new JButton("Fetch Flights");
        fetchFlightsButton.setBounds(380, 380, 120, 25);
        fetchFlightsButton.setBackground(Color.BLACK);
        fetchFlightsButton.setForeground(Color.BLUE);
        fetchFlightsButton.addActionListener(this);
        add(fetchFlightsButton);

        // Flight details
        addField("Flight Name", 430);
        flightNameLabel = addDynamicLabel(430);

        addField("Flight Code", 480);
        flightCodeLabel = addDynamicLabel(480);

        addField("Date of Travel", 530);
        travelDateChooser = new JDateChooser();
        travelDateChooser.setBounds(220, 530, 150, 25);
        add(travelDateChooser);

        // Book flight button
        bookFlightButton = new JButton("Book Flight");
        bookFlightButton.setBounds(220, 580, 150, 30);
        bookFlightButton.setBackground(Color.BLACK);
        bookFlightButton.setForeground(Color.BLUE);
        bookFlightButton.addActionListener(this);
        add(bookFlightButton);

        // Load flight sources and destinations
        loadFlightDetails();

        setVisible(true);
    }

    private void addField(String label, int yPosition) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(60, yPosition, 150, 25);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(lbl);
    }

    private JLabel addDynamicLabel(int yPosition) {
        JLabel lbl = new JLabel();
        lbl.setBounds(220, yPosition, 300, 25);
        add(lbl);
        return lbl;
    }

    private Choice addChoice(int yPosition) {
        Choice choice = new Choice();
        choice.setBounds(220, yPosition, 150, 25);
        add(choice);
        return choice;
    }

    private void loadFlightDetails() {
        try {
            Conn conn = new Conn();
            String query = "SELECT DISTINCT source, destination FROM flight";
            ResultSet rs = conn.s.executeQuery(query);

            while (rs.next()) {
                sourceChoice.add(rs.getString("source"));
                destinationChoice.add(rs.getString("destination"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchUserButton) {
            fetchUserDetails();
        } else if (ae.getSource() == fetchFlightsButton) {
            fetchFlightDetails();
        } else if (ae.getSource() == bookFlightButton) {
            bookFlight();
        }
    }

    private void fetchUserDetails() {
        String id = idTextField.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter  ID!");
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM passenger WHERE id = '" + id + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                nationalityLabel.setText(rs.getString("nationality"));
                addressLabel.setText(rs.getString("address"));
                genderLabel.setText(rs.getString("gender"));
            } else {
                JOptionPane.showMessageDialog(null, "User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchFlightDetails() {
        String src = sourceChoice.getSelectedItem();
        String dest = destinationChoice.getSelectedItem();

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM flight WHERE source = '" + src + "' AND destination = '" + dest + "'";
            ResultSet rs = conn.s.executeQuery(query);

            if (rs.next()) {
                flightNameLabel.setText(rs.getString("f_name"));
                flightCodeLabel.setText(rs.getString("f_code"));
            } else {
                JOptionPane.showMessageDialog(null, "No flights available for the selected route!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookFlight() {
        Random random = new Random();

        String id = idTextField.getText();
        String name = nameLabel.getText();
        String nationality = nationalityLabel.getText();
        String flightName = flightNameLabel.getText();
        String flightCode = flightCodeLabel.getText();
        String src = sourceChoice.getSelectedItem();
        String dest = destinationChoice.getSelectedItem();
        String travelDate = ((JTextField) travelDateChooser.getDateEditor().getUiComponent()).getText();

        if (id.isEmpty() || name.isEmpty() || travelDate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all details!");
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "INSERT INTO reservation VALUES('PNR-" + random.nextInt(1000000) + "', " +
                    "'TIC-" + random.nextInt(10000) + "', '" + id + "', '" + name + "', '" + nationality + "', " +
                    "'" + flightName + "', '" + flightCode + "', '" + src + "', '" + dest + "', '" + travelDate + "')";
            conn.s.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Flight booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BookFlight();
    }
}
