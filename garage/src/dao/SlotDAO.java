package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.*;
import model.Slot;

public class SlotDAO extends DAO {
    public ArrayList<Slot> getAvailableSlots(LocalDateTime start, LocalDateTime end){
        ArrayList<Slot> availableSlots = new ArrayList<>();
        String sql = """
            SELECT s.id, s.name
            FROM tblSlot s
            WHERE s.id NOT IN (
                SELECT DISTINCT ci.slotid
                FROM tblCarInvoice ci
                JOIN tblAddedSerCom ad ON ci.id = ad.carInvoiceID
                JOIN tblTechService ts ON ad.id = ts.serviceID
                GROUP BY ci.id, ci.slotid
                HAVING MAX(ts.timestart) < ? AND MIN(ts.timeend) > ?
            )
        """;
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(end));
            ps.setTimestamp(2, Timestamp.valueOf(start));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Slot x = new Slot();
                x.setId(id);
                x.setName(name);
                availableSlots.add(x);
            }
        }catch(SQLException e){
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
