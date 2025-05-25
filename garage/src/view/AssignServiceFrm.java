package view;

import dao.SerComDAO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.CarInvoice;
import model.PrevInvoice;
import model.ServiceComponent;
import dao.TechnicianDAO;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import model.AddedSerCom;
import model.TechService;
import model.Technician;

public class AssignServiceFrm extends JFrame implements ActionListener{
    private PrevInvoice invoice;
    private CarInvoice carbill;
    private JComboBox<String> cboTimeslot;
    private JComboBox<ServiceComponent> cboService;
    private JTextField txtDate;
    private JButton btnSearchTimeslot, btnNext, btnNextDay;
    private JTable tblTechnicians;
    private DefaultTableModel tableModel;
    private ServiceComponent selectedService = null;
    private LinkedHashMap<String, List<Technician>> slotMap = new LinkedHashMap<>();
    public AssignServiceFrm(PrevInvoice invoice){
        super("Assign service to technician");
        this.invoice=invoice;
        this.carbill=this.invoice.getCarBill().getLast();
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // Title
        JLabel lblTitle = new JLabel("Assign service to technician", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Service & Date Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, -10, 0, 0));
        topPanel.add(new JLabel("Select service"));
        cboService = new JComboBox<>();
        SerComDAO dao = new SerComDAO();
        if(carbill.getAddedSerCom()!=null){
            for(model.AddedSerCom asc : carbill.getAddedSerCom()){
                ServiceComponent sc = asc.getSerCom();
                cboService.addItem(sc);
            }
        }

        cboService.addActionListener(e -> {
            selectedService = (ServiceComponent) cboService.getSelectedItem();
        });
        cboService.setPreferredSize(new Dimension(150, 25));
        topPanel.add(cboService);

        topPanel.add(new JLabel("Select date"));
        txtDate = new JTextField(10);
        txtDate.setText(LocalDate.now().toString());
        btnNextDay = new JButton("+");
        btnNextDay.addActionListener(e -> {
            LocalDate current = LocalDate.parse(txtDate.getText());
            txtDate.setText(current.plusDays(1).toString());
        });
        topPanel.add(txtDate);
        topPanel.add(btnNextDay);

        btnSearchTimeslot = new JButton("Search timeslot");
        btnSearchTimeslot.addActionListener(this);
        topPanel.add(btnSearchTimeslot);
        mainPanel.add(topPanel);

        // Timeslot Selection
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        timePanel.setBorder(BorderFactory.createEmptyBorder(0, -15, 0, 0));
        timePanel.add(new JLabel("Select timeslot"));
        cboTimeslot = new JComboBox<>();
        cboTimeslot.setPreferredSize(new Dimension(200, 25));
        cboTimeslot.addActionListener(e -> updateTechnicianTable());
        timePanel.add(cboTimeslot);
        mainPanel.add(timePanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Technician Table
        String[] columns = {"ID", "Name", "Select"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // chỉ checkbox được sửa
            }
        };
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if(column==2){
                Boolean selected = (Boolean) tableModel.getValueAt(row, 2);
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                String selectedSlot = (String) cboTimeslot.getSelectedItem();
                String[] parts = selectedSlot.split(" - ");
                LocalTime startTime = LocalTime.parse(parts[0].trim(), fmt);
                LocalTime endTime = LocalTime.parse(parts[1].trim(), fmt);
                LocalDate date = LocalDate.parse(txtDate.getText().trim());
                LocalDateTime timeStart = LocalDateTime.of(date, startTime);
                LocalDateTime timeEnd = LocalDateTime.of(date, endTime);
                TechService ts = new TechService();
                ts.setTimeStart(timeStart);
                ts.setTimeEnd(timeEnd);
                int techid = Integer.parseInt((String) tableModel.getValueAt(row, 0));
                String techName=(String) tableModel.getValueAt(row, 1);
                Technician selectedTech = new Technician();
                selectedTech.setId(techid);
                selectedTech.setFullname(techName);
                ts.setTech(selectedTech);
                selectedService = (ServiceComponent) cboService.getSelectedItem();
                for(AddedSerCom asc : this.carbill.getAddedSerCom()){
                    if(asc.getSerCom().getId() == selectedService.getId()){
                        if(asc.getTechSer()==null){
                            asc.setTechSer(new ArrayList<>());
                        }
                        if(selected){
                            asc.getTechSer().add(ts);
                            this.carbill.recalcTimeRange();
                        }else{
                            asc.getTechSer().removeIf(temp ->
                                temp.getTech().getId() == techid &&
                                temp.getTimeStart().equals(timeStart) &&
                                temp.getTimeEnd().equals(timeEnd)
                            );
                            this.carbill.recalcTimeRange();
                        }
                        break;
                    }
                }
            }
        });
        tblTechnicians = new JTable(tableModel);
        tblTechnicians.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblTechnicians);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        mainPanel.add(scrollPane);

        // Next button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNext = new JButton("Next");
        btnNext.addActionListener(this);
        bottomPanel.add(btnNext);
        mainPanel.add(bottomPanel);

        // Setup frame
        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnSearchTimeslot)){
            ServiceComponent selectedService = (ServiceComponent) cboService.getSelectedItem();
            if(selectedService == null){
                JOptionPane.showMessageDialog(this, "Please select a service");
                return;
            }
            LocalDate date;
            try{
                date = LocalDate.parse(txtDate.getText().trim());
            }catch(DateTimeParseException ex){
                JOptionPane.showMessageDialog(this, "Invalid date format, please try again");
                return;
            }
            try{
                TechnicianDAO td = new TechnicianDAO();
                slotMap = td.getFreeTimeslotWithTech(date, selectedService.getEstimatedTime(), this.carbill.getAddedSerCom());
                cboTimeslot.removeAllItems();
                tableModel.setRowCount(0);
                if(slotMap.isEmpty()){
                    JOptionPane.showMessageDialog(this, "No available timeslots found");
                    
                }else{
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                    // Tạo list các slot rồi sắp xếp theo thời gian bắt đầu
                    List<String> sortedSlots = new ArrayList<>(slotMap.keySet());
                    sortedSlots.sort((a, b) -> {
                        LocalTime aStart = LocalTime.parse(a.split(" - ")[0], fmt);
                        LocalTime bStart = LocalTime.parse(b.split(" - ")[0], fmt);
                        return aStart.compareTo(bStart);
                    });

                    // Thêm vào combobox theo thứ tự đã sắp xếp
                    for(String slot : sortedSlots){
                        cboTimeslot.addItem(slot);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }System.out.println("Slot map size: " + slotMap.size());
            for (String slot : slotMap.keySet()) {
            }
            
        } else if(btnClicked.equals(btnNext)){
            this.dispose();
            new SelectSlotFrm(this.invoice).setVisible(true);
        }
    }
    private void updateTechnicianTable(){
        String selectedSlot = (String) cboTimeslot.getSelectedItem();
        if(selectedSlot == null || !slotMap.containsKey(selectedSlot)){
            return;
        }
        List<Technician> techs = slotMap.get(selectedSlot);
        tableModel.setRowCount(0);
        for(Technician t: techs){
            Object[] row = {String.valueOf(t.getId()), t.getFullname(), false};
            tableModel.addRow(row);
        }
    }
}

