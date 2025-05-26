package view;

import dao.PrevInvoiceDAO;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmFrm extends JFrame implements ActionListener{
    private PrevInvoice invoice;
    private JLabel lblTotal;
    private JTable tblDetails;
    private DefaultTableModel tableModel;
    private JButton btnConfirm, btnCancel;

    public ConfirmFrm(PrevInvoice invoice) {
        super("Confirm Invoice");
        this.invoice = invoice;

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();            
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Invoice Confirmation");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        infoPanel.add(new JLabel("Created At:"));
        infoPanel.add(new JLabel(invoice.getCreatedAt().format(formatter)));

        infoPanel.add(new JLabel("Customer Name:"));
        infoPanel.add(new JLabel(invoice.getCustomerID().getFullname()));

        infoPanel.add(new JLabel("Phone Number:"));
        infoPanel.add(new JLabel(invoice.getCustomerID().getTelnum()));

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        CarInvoice carBill = invoice.getCarBill().getLast();
        Car car = carBill.getCar();

        JPanel carPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        carPanel.add(new JLabel("Car Plate Number:"));
        carPanel.add(new JLabel(car.getPlateNum()));

        carPanel.add(new JLabel("Car Name:"));
        carPanel.add(new JLabel(car.getName()));

        mainPanel.add(carPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel userSlotPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        userSlotPanel.add(new JLabel("Received by: " + invoice.getUser().getFullname()));
        userSlotPanel.add(new JLabel("Slot: " + carBill.getSlot().getName()));
        mainPanel.add(userSlotPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Table
        String[] columns = {"ID", "Name", "Unit Price", "Quantity", "Total", "Technicians"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetails = new JTable(tableModel);
        tblDetails.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tblDetails.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(40);   
        tblDetails.getColumnModel().getColumn(1).setPreferredWidth(120); 
        tblDetails.getColumnModel().getColumn(2).setPreferredWidth(80);   
        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(80);  
        tblDetails.getColumnModel().getColumn(4).setPreferredWidth(80);   
        tblDetails.getColumnModel().getColumn(5).setPreferredWidth(230);  
        JScrollPane scrollPane = new JScrollPane(tblDetails);
        scrollPane.setPreferredSize(new Dimension(650, 150));
        mainPanel.add(scrollPane);

        // Populate table
    for (AddedSerCom asc : carBill.getAddedSerCom()) {
        String techIds = "N/A";
        if (asc.getTechSer() != null && !asc.getTechSer().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TechService ts : asc.getTechSer()) {
                if (ts != null && ts.getTech() != null) {
                    sb.append(ts.getTech().getFullname()).append(", ");
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2); // Xoá dấu ", " cuối
                techIds = sb.toString();
            }
        }

        Object[] row = {
            asc.getSerCom().getId(),
            asc.getSerCom().getName(),
            asc.getSerCom().getPrice(),
            asc.getQuantity(),
            asc.getTotalAmount(),
            techIds
        };
        tableModel.addRow(row);
    }

        // Bottom panel
        JPanel totalPanel = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Total: " + this.invoice.getTotal());
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        totalPanel.add(lblTotal, BorderLayout.EAST);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        mainPanel.add(totalPanel);

        // Panel chứa Cancel và Confirm ở hai bên
        JPanel buttonPanel = new JPanel(new BorderLayout());

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(btnCancel.getFont().deriveFont(Font.BOLD, 14f));
        buttonPanel.add(btnCancel, BorderLayout.WEST);

        btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(this);
        btnCancel.addActionListener(this);
        btnConfirm.setFont(btnConfirm.getFont().deriveFont(Font.BOLD, 14f));
        buttonPanel.add(btnConfirm, BorderLayout.EAST);

        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnConfirm)){
            PrevInvoiceDAO pi = new PrevInvoiceDAO();
            boolean success = pi.addInvoice(invoice);
            if (success) {
                JOptionPane.showMessageDialog(this, "Create temporary invoice successfully");
                new TechMngHomeFrm(this.invoice.getUser()).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create invoice. Please check again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel the invoice? This action cannot be undone!",
                "Confirm cancel",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                (new TechMngHomeFrm(this.invoice.getUser())).setVisible(true);
                this.dispose();
            }
        }
    }
}
