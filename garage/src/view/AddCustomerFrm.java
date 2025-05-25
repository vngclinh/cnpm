package view;

import dao.CustomerDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Customer;
import model.PrevInvoice;
import java.sql.SQLException;

public class AddCustomerFrm extends JFrame implements ActionListener {
    private PrevInvoice invoice;
    private JTextField txtFullname, txtPhone, txtAddress, txtNote;
    private JButton btnAdd, btnCancel;

    public AddCustomerFrm(PrevInvoice invoice) {
        super("Add Customer");
        this.invoice=invoice;

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Add new customer", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form input
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel lblFullname = new JLabel("Fullname");
        JLabel lblPhone = new JLabel("Phone number");
        JLabel lblAddress = new JLabel("Address");
        JLabel lblNote = new JLabel("Note");
        
        lblFullname.setFont(lblFullname.getFont().deriveFont(Font.PLAIN, 16f));
        lblPhone.setFont(lblPhone.getFont().deriveFont(Font.PLAIN, 16f));
        lblAddress.setFont(lblAddress.getFont().deriveFont(Font.PLAIN, 16f));
        lblNote.setFont(lblNote.getFont().deriveFont(Font.PLAIN, 16f));

        txtFullname = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
        txtNote = new JTextField(20);

        txtFullname.setBackground(new Color(173, 216, 230));
        txtPhone.setBackground(new Color(173, 216, 230));
        txtAddress.setBackground(new Color(173, 216, 230));
        txtNote.setBackground(new Color(173, 216, 230));
        
        txtFullname.setPreferredSize(new Dimension(100, 30));
        txtPhone.setPreferredSize(new Dimension(100, 30));
        txtAddress.setPreferredSize(new Dimension(100, 30));
        txtNote.setPreferredSize(new Dimension(100, 30));

        // Dòng 1
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblFullname, gbc);
        gbc.gridx = 1;
        formPanel.add(txtFullname, gbc);

        // Dòng 2
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        // Dòng 3
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblAddress, gbc);
        gbc.gridx = 1;
        formPanel.add(txtAddress, gbc);

        // Dòng 4
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblNote, gbc);
        gbc.gridx = 1;
        formPanel.add(txtNote, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        btnCancel = new JButton("Cancel");
        btnAdd = new JButton("Add");

        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnAdd.setPreferredSize(new Dimension(120, 40));

        btnCancel.setBackground(new Color(255, 239, 150));
        btnAdd.setBackground(new Color(255, 239, 150));

        btnCancel.setFont(btnCancel.getFont().deriveFont(Font.BOLD, 16f));
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 16f));
        
        btnAdd.addActionListener(this);
        btnCancel.addActionListener(this);

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnAdd);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Cài đặt frame
        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnAdd)){
            String name = txtFullname.getText().trim();
            String telnum = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String note = txtNote.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the fullname.");
                return;
            }
            if (telnum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the phone number.");
                return;
            }
            if(telnum.length()!=10){
                JOptionPane.showMessageDialog(this, "Please enter the right phone number with 10 letters");
                return;
            }
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the address.");
                return;
            }
            CustomerDAO cd = new CustomerDAO();
            Customer c = new Customer(name, telnum, address, note);
            try {
                cd.addCustomer(c);               
                invoice.setCustomer(c);          
                JOptionPane.showMessageDialog(this, "Customer added successfully!");
                new SearchCarFrm(this.invoice).setVisible(true);
                this.dispose();               
            } catch(SQLException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while adding customer.");
            }
        }else{
            this.dispose();
            new SearchCustomerFrm(this.invoice).setVisible(true);
        }
    }
}
