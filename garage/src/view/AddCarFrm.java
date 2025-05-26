package view;
import dao.CarDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Car;
import model.PrevInvoice;
import java.sql.SQLException;
import java.util.ArrayList;
import model.CarInvoice;

public class AddCarFrm extends JFrame implements ActionListener {
    private JTextField txtPlate, txtName, txtBrand, txtType;
    private JButton btnAdd, btnCancel;
    private PrevInvoice invoice;

    public AddCarFrm(PrevInvoice invoice) {
        super("Add Car");
        this.invoice = invoice;

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Add new car", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form input
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel lblPlate = new JLabel("Plate number");
        JLabel lblName = new JLabel("Car name");
        JLabel lblBrand = new JLabel("Brand");
        JLabel lblType = new JLabel("Type");

        lblPlate.setFont(lblPlate.getFont().deriveFont(Font.PLAIN, 16f));
        lblName.setFont(lblName.getFont().deriveFont(Font.PLAIN, 16f));
        lblBrand.setFont(lblBrand.getFont().deriveFont(Font.PLAIN, 16f));
        lblType.setFont(lblType.getFont().deriveFont(Font.PLAIN, 16f));

        txtPlate = new JTextField(20);
        txtName = new JTextField(20);
        txtBrand = new JTextField(20);
        txtType = new JTextField(20);

        Color bg = new Color(173, 216, 230);
        txtPlate.setBackground(bg);
        txtName.setBackground(bg);
        txtBrand.setBackground(bg);
        txtType.setBackground(bg);

        txtPlate.setPreferredSize(new Dimension(100, 30));
        txtName.setPreferredSize(new Dimension(100, 30));
        txtBrand.setPreferredSize(new Dimension(100, 30));
        txtType.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblPlate, gbc); gbc.gridx = 1;
        formPanel.add(txtPlate, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblName, gbc); gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblBrand, gbc); gbc.gridx = 1;
        formPanel.add(txtBrand, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblType, gbc); gbc.gridx = 1;
        formPanel.add(txtType, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
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

        this.setContentPane(mainPanel);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn == btnAdd) {
            String plate = txtPlate.getText().trim();
            String name = txtName.getText().trim();
            String brand = txtBrand.getText().trim();
            String type = txtType.getText().trim();

            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the plate number.");
                return;
            }
            if(!plate.matches("^[0-9]{2}[A-Z]{1}-[0-9]{3,5}(\\.[0-9]{2,3})?$")){
                JOptionPane.showMessageDialog(this, "Invalid plate number format, must be like '30G-123.45' or '30G-123.123");
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the car name.");
                return;
            }
            if (brand.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the brand.");
                return;
            }
            if (type.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the type.");
                return;
            }

            CarDAO cd = new CarDAO();
            try {
                if(cd.isPlateNumberExists(plate)){
                    JOptionPane.showMessageDialog(this, "Plate number already existed!");
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Car c = new Car(plate, name, brand, type);
            try{
                cd.addCar(c);
                CarInvoice carinvoice = new CarInvoice();
                carinvoice.setCar(c);

                if(invoice.getCarBill()==null){
                    invoice.setCarBill(new ArrayList<>());
                }
                invoice.getCarBill().add(carinvoice);
                this.dispose();
                new SearchSerComFrm(this.invoice).setVisible(true);
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        } else {
            this.dispose();
            new SearchCarFrm(this.invoice).setVisible(true);
        }
    }
}