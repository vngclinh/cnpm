package view;

import dao.SerComDAO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.CarInvoice;
import model.PrevInvoice;
import model.ServiceComponent;
import java.sql.SQLException;
import model.AddedSerCom;

public class SearchSerComFrm extends JFrame implements ActionListener {
    private JTextField txtSearch, txtQuantity;
    private JButton btnSearch, btnAdd, btnNext;
    private JTable tblResult, tblInvoiceItems;
    private DefaultTableModel tableModel, invoiceTableModel;
    private PrevInvoice invoice;
    private ServiceComponent selected = null;

    public SearchSerComFrm(PrevInvoice invoice){
        super("Search service component");
        this.invoice = invoice;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Search service component");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout(10, 0));
        JLabel lblName = new JLabel("Name");
        lblName.setFont(lblName.getFont().deriveFont(Font.PLAIN, 16f));
        searchPanel.add(lblName, BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 30));
        txtSearch.setFont(txtSearch.getFont().deriveFont(Font.PLAIN, 16f));
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(90, 30));
        btnSearch.setFont(btnSearch.getFont().deriveFont(Font.BOLD, 14f));
        btnSearch.addActionListener(this);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] columnNames = {"ID", "Name", "Price", "Description", "Estimated time(min)"};
        tableModel = new DefaultTableModel(columnNames, 0){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tblResult = new JTable(tableModel);
        tblResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblResult.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                if(!e.getValueIsAdjusting()){
                    int selectedRow = tblResult.getSelectedRow();
                    if(selectedRow >= 0){
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        String name = (String) tableModel.getValueAt(selectedRow, 1);
                        double price  = (Double) tableModel.getValueAt(selectedRow, 2);
                        String des = (String) tableModel.getValueAt(selectedRow, 3);
                        int time = (Integer) tableModel.getValueAt(selectedRow, 4);

                        selected = new ServiceComponent();
                        selected.setId(id);
                        selected.setName(name);
                        selected.setPrice(price);
                        selected.setDescrption(des);
                        selected.setEstimatedTime(time);
                    }
                }
            }
        });
        tblResult.setFont(tblResult.getFont().deriveFont(Font.PLAIN, 14f));
        tblResult.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(tblResult);
        scrollPane.setPreferredSize(new Dimension(600, 150)); // giảm chiều cao
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel actionPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblQty = new JLabel("Quantity");
        txtQuantity = new JTextField(10);
        leftPanel.add(lblQty);
        leftPanel.add(txtQuantity);
        actionPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Add to invoice");
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 14f));
        btnAdd.addActionListener(this);
        rightPanel.add(btnAdd);
        actionPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(actionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        String[] invoiceCols = {"ID", "Name", "Unit Price", "Quantity", "Total"};
        invoiceTableModel = new DefaultTableModel(invoiceCols, 0){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tblInvoiceItems = new JTable(invoiceTableModel);
        tblInvoiceItems.setFont(tblResult.getFont().deriveFont(Font.PLAIN, 14f));
        tblInvoiceItems.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane invoiceScroll = new JScrollPane(tblInvoiceItems);
        invoiceScroll.setPreferredSize(new Dimension(600, 120));
        mainPanel.add(invoiceScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNext = new JButton("Next");
        btnNext.setFont(btnNext.getFont().deriveFont(Font.BOLD, 14f));
        btnNext.addActionListener(this);
        bottomPanel.add(btnNext);
        mainPanel.add(bottomPanel);

        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnSearch)){
            String keyword = txtSearch.getText().trim();
            if(keyword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a name to search");
                return;
            }
            SerComDAO sc = new SerComDAO();
            try{
                ArrayList<ServiceComponent> list = sc.searchSerCom(keyword);
                tableModel.setRowCount(0);
                for(ServiceComponent x : list){
                    Object[] row = {x.getId(), x.getName(), x.getPrice(), x.getDescrption(), x.getEstimatedTime()};
                    tableModel.addRow(row);
                }
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else if(btnClicked.equals(btnAdd)){
            String sl = txtQuantity.getText().trim();
            txtQuantity.setText("");
            int quantity = 0;
            try{
                quantity = Integer.parseInt(sl);
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Please enter a number");
                return;
            }
            if(quantity <= 0){
                JOptionPane.showMessageDialog(this, "Quantity must be > 0");
                return;
            }
            if(selected == null){
                JOptionPane.showMessageDialog(this, "Please select a service or component");
                return;
            }
            AddedSerCom asc = new AddedSerCom();
            asc.setQuantity(quantity);
            asc.setSerCom(selected);
            CarInvoice ci = this.invoice.getCarBill().getLast();
            if(ci.getAddedSerCom() == null){
                ci.setAddedSerCom(new ArrayList<>());
            }
            boolean found = false;
            for (int i = 0; i < ci.getAddedSerCom().size(); i++) {
                AddedSerCom temp = ci.getAddedSerCom().get(i);
                if (temp.getSerCom().getId() == asc.getSerCom().getId()) {
                    found = true;
                    int choice = JOptionPane.showConfirmDialog(
                        this,
                        "This service/component already exists. Do you want to update the quantity?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        temp.setQuantity(quantity);

                        // Cập nhật dòng trong bảng
                        for (int row = 0; row < invoiceTableModel.getRowCount(); row++) {
                            int idInTable = (int) invoiceTableModel.getValueAt(row, 0);
                            if (idInTable == temp.getSerCom().getId()) {
                                invoiceTableModel.setValueAt(temp.getQuantity(), row, 3);
                                invoiceTableModel.setValueAt(temp.getQuantity() * temp.getSerCom().getPrice(), row, 4);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            if (!found) {
                ci.getAddedSerCom().add(asc);
                Object[] newRow = {
                    selected.getId(),
                    selected.getName(),
                    selected.getPrice(),
                    quantity,
                    selected.getPrice() * quantity
                };
                invoiceTableModel.addRow(newRow);
            }
            tblResult.clearSelection();
            selected = null;
        }else{
            if (tblInvoiceItems.getRowCount()==0){
                JOptionPane.showMessageDialog(this, "You haven't add any services or components!");
                return;
            }
            (new AssignServiceFrm(this.invoice)).setVisible(true);
            this.dispose();
        }
    }
}
