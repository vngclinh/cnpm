package view;

import dao.SlotDAO;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.CarInvoice;
import model.PrevInvoice;
import model.Slot;

public class SelectSlotFrm extends JFrame {
    private JTextField txtTimeRange;
    private JTable tblSlots;
    private DefaultTableModel tableModel;
    private PrevInvoice invoice;
    private CarInvoice carbill;
    public SelectSlotFrm(PrevInvoice invoice){
        super("Select slot");
        this.invoice=invoice;
        this.carbill=invoice.getCarBill().getLast();
                // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Title
        JLabel lblTitle = new JLabel("Select slot");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 24f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Time range panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTimeRange = new JLabel("Time range:");
        lblTimeRange.setFont(lblTimeRange.getFont().deriveFont(Font.PLAIN, 16f));
        timePanel.add(lblTimeRange);

        txtTimeRange = new JTextField(25);
        txtTimeRange.setFont(txtTimeRange.getFont().deriveFont(Font.PLAIN, 16f));
        txtTimeRange.setEditable(false); 
        LocalDateTime timestart = LocalDateTime.parse(this.carbill.getTimeStart().toString());
        LocalDateTime timeend = LocalDateTime.parse(this.carbill.getTimeEnd().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM");
        String text = timestart.format(formatter)+" - "+timeend.format(formatter);
        txtTimeRange.setText(text);
        timePanel.add(txtTimeRange);
        mainPanel.add(timePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Table slot
        String[] columnNames = {"ID", "Slot name"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSlots = new JTable(tableModel);
        tblSlots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSlots.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                if(e.getValueIsAdjusting()){
                    int selectedRow = tblSlots.getSelectedRow();
                    if(selectedRow >= 0){
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
                        String name = (String) tableModel.getValueAt(selectedRow, 1);
                        Slot s = new Slot();
                        s.setId(id);
                        s.setName(name);
                        SelectSlotFrm.this.carbill.setSlot(s);
                        (new ConfirmFrm(SelectSlotFrm.this.invoice)).setVisible(true);
                        SelectSlotFrm.this.dispose();
                    }
                }
            }
        });
        
        tblSlots.setFont(tblSlots.getFont().deriveFont(Font.PLAIN, 14f));
        tblSlots.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tblSlots.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(tblSlots);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        mainPanel.add(scrollPane);

        // Frame settings
        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTable();
    }
    public void setTable(){
        SlotDAO sd = new SlotDAO();
        ArrayList<Slot> availableSlots = sd.getAvailableSlots(this.carbill.getTimeStart(), this.carbill.getTimeEnd());
        for (Slot s:availableSlots){
            tableModel.addRow(new Object[]{s.getId(), s.getName()});
        }
        if(tableModel.getRowCount()==0){
            LocalDateTime next = sd.getNextAvailableTime();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd/MM");
            JOptionPane.showMessageDialog(this, "No available slot, go back and choose again.\nEarliest free slot is at "+next.format(fmt));
        }
    }
}
