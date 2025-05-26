package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.*;
import model.Slot;

public class SlotDAO extends DAO {
    public ArrayList<Slot> getAvailableSlots(LocalDateTime start, LocalDateTime end) {
        ArrayList<Slot> availableSlots = new ArrayList<>();
        String sql = """
            SELECT s.id, s.name
            FROM tblSlot s
            WHERE s.id NOT IN (
                SELECT ci.slotid
                FROM tblCarInvoice ci
                WHERE NOT (
                    ci.timeend <= ? OR ci.timestart >= ?
                )
            )
        """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(start)); 
            ps.setTimestamp(2, Timestamp.valueOf(end));  
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setId(rs.getInt("id"));
                slot.setName(rs.getString("name"));
                availableSlots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableSlots;
    }

    public LocalDateTime getNextAvailableTime(){
        String sql = """
            SELECT MIN(ts.timeend) AS next_available
            FROM tblTechService ts
            JOIN tblAddedSerCom ad ON ts.serviceID = ad.id
            JOIN tblCarInvoice ci ON ad.carInvoiceID = ci.id
        """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("next_available");
                if (ts != null) {
                    return ts.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
