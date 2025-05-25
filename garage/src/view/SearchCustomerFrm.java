package view;

import dao.CustomerDAO;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import model.Customer;
import javax.swing.event.*;
import model.PrevInvoice;

public class SearchCustomerFrm extends JFrame implements ActionListener {
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd;
    private JTable tblResult;
    private DefaultTableModel tableModel;
    private PrevInvoice invoice;

    public SearchCustomerFrm(PrevInvoice invoice) {
        super("Customer Search");
        this.invoice=invoice;

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Search customer");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout(10, 0));

        JLabel lblName = new JLabel("Name");
        lblName.setFont(lblName.getFont().deriveFont(Font.PLAIN, 16f)); 
        searchPanel.add(lblName, BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 30)); // to hơn
        txtSearch.setFont(txtSearch.getFont().deriveFont(Font.PLAIN, 16f));

        searchPanel.add(txtSearch, BorderLayout.CENTER);

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(90, 30));
        btnSearch.setFont(btnSearch.getFont().deriveFont(Font.BOLD, 14f));
        btnSearch.addActionListener(this);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Bảng kết quả
        String[] columnNames = {"ID","Fullname", "Phone number", "Address", "Note"};
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
                    if(selectedRow>=0){
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        String fullname = (String) tableModel.getValueAt(selectedRow, 1);
                        String telnum = (String) tableModel.getValueAt(selectedRow, 2);
                        String address = (String) tableModel.getValueAt(selectedRow, 3);
                        String note = (String) tableModel.getValueAt(selectedRow, 4);
                        Customer c = new Customer();
                        c.setId(id);
                        c.setFullname(fullname);
                        c.setTelnum(telnum);
                        c.setAddress(address);
                        c.setNote(note);
                        invoice.setCustomer(c);
                        (new SearchCarFrm(invoice)).setVisible(true);
                        SearchCustomerFrm.this.dispose();
                    }
                }
            }
        });
        tblResult.setFont(tblResult.getFont().deriveFont(Font.PLAIN, 14f));
        tblResult.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(tblResult);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Add New
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Add new");
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 14f));
        btnAdd.addActionListener(this);
        bottomPanel.add(btnAdd);
        mainPanel.add(bottomPanel);

        // Cài đặt frame
        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnSearch)){
            String keyword = txtSearch.getText().trim();
            if(keyword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a name to search");
                return;
            }
            CustomerDAO cd = new CustomerDAO();
            try{
                ArrayList<Customer>list = cd.searchCustomer(keyword);
                tableModel.setRowCount(0);
                for(Customer c:list){
                    Object[] row = {c.getId(), c.getFullname(), c.getTelnum(), c.getAddress(), c.getNote()};
                    tableModel.addRow(row);
                }
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        } else if(btnClicked.equals(btnAdd)){
            (new AddCustomerFrm(this.invoice)).setVisible(true);
            this.dispose();
        }
    }
}
