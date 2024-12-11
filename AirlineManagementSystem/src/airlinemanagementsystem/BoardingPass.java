package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Optional;

/**
 * BoardingPass - A UI for displaying passenger boarding pass details.
 *
 * @author yashpatel
 */
public class BoardingPass extends JFrame implements ActionListener {

    JTextField pnrTextField;
    JLabel nameLabel, nationalityLabel, srcLabel, destLabel, flightNameLabel, flightCodeLabel, flightDateLabel;
    JButton fetchButton, clearButton;

    public BoardingPass() {
        // Frame settings
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Heading
        JLabel heading = new JLabel("EMIRATES AIRLINES");
        heading.setBounds(350, 20, 300, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(new Color(0, 102, 204)); // Blue
        add(heading);

        // Subheading
        JLabel subheading = new JLabel("Boarding Pass");
        subheading.setBounds(390, 60, 200, 30);
        subheading.setFont(new Font("Tahoma", Font.BOLD, 22));
        subheading.setForeground(Color.DARK_GRAY);
        add(subheading);

        ImageIcon icon = new ImageIcon("E:\\NEU LABS\\Info5100\\Project\\info5100Project\\AirlineManagementSystem\\src\\airlinemanagementsystem\\boarding_pass.png"); // Replace with your icon file path
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(400, 96, 50, 50); // Adjust the bounds to position the icon below the subheading
        add(iconLabel);

        // PNR Input
        JLabel pnrLabel = new JLabel("PNR:");
        pnrLabel.setBounds(60, 150, 150, 25);
        pnrLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(pnrLabel);

        pnrTextField = new JTextField();
        pnrTextField.setBounds(220, 150, 200, 25);
        add(pnrTextField);

        fetchButton = new JButton("Fetch");
        fetchButton.setBounds(450, 150, 100, 25);
        fetchButton.setBackground(new Color(34, 139, 34)); //  Green
        fetchButton.setForeground(Color.BLUE);
        fetchButton.addActionListener(this);
        add(fetchButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(570, 150, 100, 25);
        clearButton.setBackground(new Color(220, 20, 60)); //  Red
        clearButton.setForeground(Color.BLUE);
        clearButton.addActionListener(this);
        add(clearButton);

        //  Passenger Details
        JLabel nameLabelHeading = new JLabel("Name:");
        nameLabelHeading.setBounds(60, 180, 150, 25);
        nameLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(nameLabelHeading);

        nameLabel = new JLabel();
        nameLabel.setBounds(220, 180, 200, 25);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(nameLabel);

        JLabel nationalityLabelHeading = new JLabel("Nationality:");
        nationalityLabelHeading.setBounds(60, 220, 150, 25);
        nationalityLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(nationalityLabelHeading);

        nationalityLabel = new JLabel();
        nationalityLabel.setBounds(220, 220, 200, 25);
        nationalityLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(nationalityLabel);

        // Flight Details
        JLabel srcLabelHeading = new JLabel("Source:");
        srcLabelHeading.setBounds(60, 260, 150, 25);
        srcLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(srcLabelHeading);

        srcLabel = new JLabel();
        srcLabel.setBounds(220, 260, 200, 25);
        srcLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(srcLabel);

        JLabel destLabelHeading = new JLabel("Destination:");
        destLabelHeading.setBounds(60, 300, 150, 25);
        destLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(destLabelHeading);

        destLabel = new JLabel();
        destLabel.setBounds(220, 300, 200, 25);
        destLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(destLabel);

        JLabel flightNameLabelHeading = new JLabel("Flight Name:");
        flightNameLabelHeading.setBounds(60, 340, 150, 25);
        flightNameLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(flightNameLabelHeading);

        flightNameLabel = new JLabel();
        flightNameLabel.setBounds(220, 340, 200, 25);
        flightNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(flightNameLabel);

        JLabel flightCodeLabelHeading = new JLabel("Flight Code:");
        flightCodeLabelHeading.setBounds(60, 380, 150, 25);
        flightCodeLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(flightCodeLabelHeading);

        flightCodeLabel = new JLabel();
        flightCodeLabel.setBounds(220, 380, 200, 25);
        flightCodeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(flightCodeLabel);

        JLabel flightDateLabelHeading = new JLabel("Flight Date:");
        flightDateLabelHeading.setBounds(60, 420, 150, 25);
        flightDateLabelHeading.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(flightDateLabelHeading);

        flightDateLabel = new JLabel();
        flightDateLabel.setBounds(220, 420, 200, 25);
        flightDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(flightDateLabel);

        setTitle("Boarding Pass");
        setSize(1000, 600);
        setLocation(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchButton) {
            String pnr = pnrTextField.getText().trim();

            if (pnr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a PNR!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "SELECT * FROM reservation WHERE PNR = '" + pnr + "'";
                ResultSet rs = conn.s.executeQuery(query);

                if (rs.next()) {
                    nameLabel.setText(rs.getString("name"));
                    nationalityLabel.setText(rs.getString("nationality"));
                    srcLabel.setText(rs.getString("src"));
                    destLabel.setText(rs.getString("des"));
                    flightNameLabel.setText(rs.getString("flightname"));
                    flightCodeLabel.setText(rs.getString("flightcode"));
                    flightDateLabel.setText(rs.getString("ddate"));
                } else {
                    JOptionPane.showMessageDialog(null, "PNR not found. Please enter a valid PNR.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error fetching data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == clearButton) {
            pnrTextField.setText("");
            nameLabel.setText("");
            nationalityLabel.setText("");
            srcLabel.setText("");
            destLabel.setText("");
            flightNameLabel.setText("");
            flightCodeLabel.setText("");
            flightDateLabel.setText("");
        }
    }

    public static void main(String[] args) {
        new BoardingPass();
    }
}
