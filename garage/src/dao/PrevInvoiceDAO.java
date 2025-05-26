package dao;

import java.sql.*;
import model.PrevInvoice;

public class PrevInvoiceDAO extends DAO {
    public PrevInvoiceDAO(){
        super();
    }
    public boolean addInvoice(PrevInvoice invoice){
        String sql = "INSERT INTO tblInvoice(createdat, userid, customerid) VALUES (?, ?, ?)";
        String sqlcarinvoice = "INSERT INTO tblCarInvoice(carid, invoiceid, slotid, timestart, timeend) VALUES (?, ?, ?, ?, ?)";
        String sqladded = "INSERT INTO tblAddedSerCom(quantity, carInvoiceID, sercomID) VALUES (?, ?, ?)";
        String sqltechser = "INSERT INTO tblTechService(timestart, timeend, serviceID, technicianID) VALUES (?, ?, ?, ?)";

        System.out.println("User ID: " + invoice.getUser().getId());
        System.out.println("Customer ID: " + invoice.getCustomerID().getId());

        for (var carInvoice : invoice.getCarBill()) {
            System.out.println("Car ID: " + carInvoice.getCar().getId());
            System.out.println("Slot ID: " + carInvoice.getSlot().getId());
            for (var added : carInvoice.getAddedSerCom()) {
                System.out.println("ServiceComponent ID: " + added.getSerCom().getId());
                for (var techService : added.getTechSer()) {
                    System.out.println("Tech ID: " + techService.getTech().getId());
                }
            }
        }

        try {
            con.setAutoCommit(false);
            // 1. Thêm tblInvoice
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(invoice.getCreatedAt()));
            ps.setInt(2, invoice.getUser().getId());
            ps.setInt(3, invoice.getCustomerID().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                invoice.setId(rs.getInt(1));
            }

            // 2. Thêm từng carInvoice
            for (var carInvoice : invoice.getCarBill()) {
                ps = con.prepareStatement(sqlcarinvoice, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, carInvoice.getCar().getId());
                ps.setInt(2, invoice.getId());
                ps.setInt(3, carInvoice.getSlot().getId());
                ps.setTimestamp(4, Timestamp.valueOf(carInvoice.getTimeStart()));
                ps.setTimestamp(5, Timestamp.valueOf(carInvoice.getTimeEnd()));
                ps.executeUpdate();

                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    carInvoice.setId(rs.getInt(1));
                }

                // 3. Thêm các dịch vụ đã thêm vào hóa đơn (AddedSerCom)
                for (var added : carInvoice.getAddedSerCom()) {
                    ps = con.prepareStatement(sqladded, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, added.getQuantity());
                    ps.setInt(2, carInvoice.getId());
                    ps.setInt(3, added.getSerCom().getId());
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        added.setId(rs.getInt(1));
                    }

                    // 4. Thêm các technician thực hiện dịch vụ (TechService)
                    if (added.getTechSer() != null) {
                        for (var techService : added.getTechSer()) {
                            ps = con.prepareStatement(sqltechser);
                            ps.setTimestamp(1, Timestamp.valueOf(techService.getTimeStart()));
                            ps.setTimestamp(2, Timestamp.valueOf(techService.getTimeEnd()));
                            ps.setInt(3, added.getId()); // ID của AddedSerCom là serviceID
                            ps.setInt(4, techService.getTech().getId());
                            ps.executeUpdate();
                        }
                    }
                }
            }

            con.commit();
            con.setAutoCommit(true);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

}
