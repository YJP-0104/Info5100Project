package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Cancel extends JFrame implements ActionListener {
    JTextField tfpnr;
    JLabel tfname, cancellationno, lblfcode, lbldateoftravel;
    JButton fetchButton, cancelButton;

    public Cancel() {
        setTitle("Flight Ticket Cancellation");
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout(20, 20));

        // Header
        JLabel heading = new JLabel("Cancel Flight Ticket", SwingConstants.CENTER);
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(new Color(25, 77, 77));
        add(heading, BorderLayout.NORTH);

        // Center Panel for Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblaadhar = new JLabel("PNR Number:");
        lblaadhar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblaadhar, gbc);

        gbc.gridx = 1;
        tfpnr = new JTextField(15);
        formPanel.add(tfpnr, gbc);

        gbc.gridx = 2;
        fetchButton = new JButton("Show Details");
        fetchButton.setBackground(new Color(25, 77, 77));
        fetchButton.setForeground(Color.BLUE);
        fetchButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        fetchButton.addActionListener(this);
        formPanel.add(fetchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblname = new JLabel("Name:");
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblname, gbc);

        gbc.gridx = 1;
        tfname = new JLabel();
        tfname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(tfname, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblcancelno = new JLabel("Cancellation No:");
        lblcancelno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblcancelno, gbc);

        gbc.gridx = 1;
        Random random = new Random();
        cancellationno = new JLabel("" + random.nextInt(1000000));
        cancellationno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(cancellationno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblfcodeLabel = new JLabel("Flight Code:");
        lblfcodeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblfcodeLabel, gbc);

        gbc.gridx = 1;
        lblfcode = new JLabel();
        lblfcode.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lblfcode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lbldate = new JLabel("Date of Travel:");
        lbldate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lbldate, gbc);

        gbc.gridx = 1;
        lbldateoftravel = new JLabel();
        lbldateoftravel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        formPanel.add(lbldateoftravel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Footer Panel for Cancel Button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 248, 255));

        cancelButton = new JButton("Cancel Ticket");
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.BLUE);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        cancelButton.addActionListener(this);
        footerPanel.add(cancelButton);

        add(footerPanel, BorderLayout.SOUTH);

        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchButton) {
            String pnr = tfpnr.getText();

            try {
                Conn conn = new Conn();
                String query = "select * from reservation where PNR = '" + pnr + "'";
                ResultSet rs = conn.s.executeQuery(query);

                if (rs.next()) {
                    tfname.setText(rs.getString("name"));
                    lblfcode.setText(rs.getString("flightcode"));
                    lbldateoftravel.setText(rs.getString("ddate"));
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid PNR number");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancelButton) {
            String name = tfname.getText();
            String pnr = tfpnr.getText();
            String cancelno = cancellationno.getText();
            String fcode = lblfcode.getText();
            String date = lbldateoftravel.getText();

            try {
                Conn conn = new Conn();
                String query = "insert into cancel values('" + pnr + "', '" + name + "', '" + cancelno + "', '" + fcode + "', '" + date + "')";
                conn.s.executeUpdate(query);
                conn.s.executeUpdate("delete from reservation where PNR = '" + pnr + "'");
                JOptionPane.showMessageDialog(null, "Ticket Cancelled Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Cancel();
    }
}
