package view;

import dao.CarDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Car;
import model.PrevInvoice;
import java.sql.SQLException;
import model.CarInvoice;

public class SearchCarFrm extends JFrame implements ActionListener {
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd;
    private JTable tblResult;
    private DefaultTableModel tableModel;
    private PrevInvoice invoice;
    public SearchCarFrm(PrevInvoice invoice){
        super("Search car");
        this.invoice=invoice;

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Search car");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout(10, 0));

        JLabel lblName = new JLabel("Plate number");
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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Bảng kết quả
        String[] columnNames = {"ID","Plate number", "Name", "Brand", "Type"
                + ""};
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
                        String platenum = (String) tableModel.getValueAt(selectedRow, 1);
                        String name = (String) tableModel.getValueAt(selectedRow, 2);
                        String brand = (String) tableModel.getValueAt(selectedRow, 3);
                        String type = (String) tableModel.getValueAt(selectedRow, 4);
                        Car c = new Car();
                        c.setId(id);
                        c.setPlateNum(platenum);
                        c.setName(name);
                        c.setBrand(brand);
                        c.setType(type);
                        
                        CarInvoice carinvoice = new CarInvoice();
                        carinvoice.setCar(c);
                        
                        if(invoice.getCarBill()==null){
                            invoice.setCarBill(new ArrayList<>());
                        }
                        invoice.getCarBill().add(carinvoice);
                        (new SearchSerComFrm(invoice)).setVisible(true);
                        SearchCarFrm.this.dispose();
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
        btnAdd = new JButton("Add new car");
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
    public void actionPerformed(ActionEvent e){
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnSearch)){
            String keyword = txtSearch.getText().trim();
            if(keyword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter plate number to search");
                return;
            }
            CarDAO cd = new CarDAO();
            try{
                ArrayList<Car> list = cd.searchCar(keyword);
                tableModel.setRowCount(0);
                for(Car c:list){
                    Object[] row = {c.getId(), c.getPlateNum(), c.getName(), c.getBrand(), c.getType()};
                    tableModel.addRow(row);
                }
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }else if(btnClicked.equals(btnAdd)){
            (new AddCarFrm(this.invoice)).setVisible(true);
            this.dispose();
        }
    }
}
