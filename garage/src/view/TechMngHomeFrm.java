package view;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import javax.swing.*;
import model.PrevInvoice;
import model.User;


public class TechMngHomeFrm extends JFrame implements ActionListener {
    private JButton btnReceiveCar, btnTechReports;
    private JTextField txtManagerName;
    private User user;

    public TechMngHomeFrm(User user) {
        super("Technical Manager");
        this.user = user;

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Technical manager's homepage");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtManagerName = new JTextField(user.getFullname());
        txtManagerName.setEditable(false);
        txtManagerName.setPreferredSize(new Dimension(150, 25));
        txtManagerName.setBorder(null);
        txtManagerName.setBackground(new Color(240, 240, 240));
        txtManagerName.setHorizontalAlignment(JTextField.RIGHT);
        txtManagerName.setFont(txtManagerName.getFont().deriveFont(Font.PLAIN, 14f));
        namePanel.add(txtManagerName);
        mainPanel.add(namePanel);

        // Nút: Receive car
        btnReceiveCar = new JButton("Receive car");
        btnReceiveCar.setFont(btnReceiveCar.getFont().deriveFont(Font.BOLD));
        btnReceiveCar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReceiveCar.addActionListener(this);
        mainPanel.add(btnReceiveCar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Nút: Technical reports
        btnTechReports = new JButton("Technical reports");
        btnTechReports.setFont(btnTechReports.getFont().deriveFont(Font.BOLD));
        btnTechReports.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTechReports.addActionListener(this);
        mainPanel.add(btnTechReports);

        Dimension buttonSize = new Dimension(200, 40); // độ dài giống nhau
        btnReceiveCar.setPreferredSize(buttonSize);
        btnReceiveCar.setMaximumSize(buttonSize);
        btnTechReports.setPreferredSize(buttonSize);
        btnTechReports.setMaximumSize(buttonSize);
        mainPanel.add(Box.createVerticalGlue());
        // Cài đặt frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 350);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReceiveCar) {
            PrevInvoice invoice = new PrevInvoice();
            invoice.setCreatedAt(LocalDateTime.now());
            invoice.setUserID(this.user);
            (new SearchCustomerFrm(invoice)).setVisible(true);
            this.dispose();
        } else if (e.getSource() == btnTechReports) {
            JOptionPane.showMessageDialog(this, "Opening: Technical Reports Module");
        }
    }
}