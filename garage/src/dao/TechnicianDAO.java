package dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import model.AddedSerCom;
import model.TechService;
import model.Technician;

public class TechnicianDAO extends DAO {
    class TimeRange{
        LocalTime start;
        LocalTime end;
        public TimeRange(LocalTime start, LocalTime end){
            this.start=start;
            this.end=end;
        }
    }    

    public LinkedHashMap<String, List<Technician>> getFreeTimeslotWithTech(LocalDate date, int estimatedTime, ArrayList<AddedSerCom> currentAddedSerCom) {
        LinkedHashMap<String, List<Technician>> results = new LinkedHashMap<>();
        List<Technician> tech = new ArrayList<>();

        try {
            // 1. Lấy danh sách technician
            String techsql = "SELECT * FROM tblTechnician";
            PreparedStatement techtmt = con.prepareStatement(techsql);
            ResultSet techRs = techtmt.executeQuery();
            while (techRs.next()) {
                Technician x = new Technician();
                x.setId(techRs.getInt("id"));
                x.setFullname(techRs.getString("fullname"));
                x.setTelnum(techRs.getString("telnum"));
                x.setAddress(techRs.getString("address"));
                tech.add(x);
            }
            techtmt.close();
            System.out.println(tech.size());

            // 2. Lấy lịch bận
            String sql = "SELECT technicianid, timestart, timeend FROM tblTechService WHERE CAST(timestart AS DATE) = ?";
            PreparedStatement busy = con.prepareStatement(sql);
            busy.setDate(1, java.sql.Date.valueOf(date));
            ResultSet busyRs = busy.executeQuery();

            HashMap<Technician, List<TimeRange>> busyMap = new HashMap<>();
            for (Technician x : tech) {
                busyMap.put(x, new ArrayList<>());
            }

            while (busyRs.next()) {
                int techID = busyRs.getInt("technicianid");
                LocalTime start = busyRs.getTimestamp("timestart").toLocalDateTime().toLocalTime();
                LocalTime end = busyRs.getTimestamp("timeend").toLocalDateTime().toLocalTime();

                for (Technician x : tech) {
                    if (x.getId() == techID) {
                        busyMap.get(x).add(new TimeRange(start, end));
                        break;
                    }
                }
            }
            busy.close();
            for(AddedSerCom asc : currentAddedSerCom){
                if(asc.getTechSer()!=null){
                    for(TechService ts: asc.getTechSer()){
                        if(ts.getTimeStart().toLocalDate().isEqual(date)){
                            for(Technician t: tech){
                                if(t.getId()==ts.getTech().getId()){
                                    busyMap.get(t).add(new TimeRange(ts.getTimeStart().toLocalTime(), ts.getTimeEnd().toLocalTime()));
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // 3. Tính slot rảnh từng technician
            LocalTime now = LocalTime.now().withSecond(0).withNano(0);
            LocalTime defaultStart = LocalTime.of(8,0);
            LocalTime dayStart = date.isEqual(LocalDate.now())?
                    (now.isAfter(defaultStart) ? now:defaultStart) : defaultStart;
//            LocalTime dayStart = LocalTime.of(8, 0);
            LocalTime dayEnd = LocalTime.of(18, 0);
            HashMap<Technician, List<TimeRange>> freeMap = new HashMap<>();

            for (Technician t : tech) {
                List<TimeRange> busyList = busyMap.get(t);
                List<TimeRange> freeList = getFreeSlots(busyList, dayStart, dayEnd);
                freeMap.put(t, freeList);
            }
            for (Technician t : tech) {
                List<TimeRange> freeList = freeMap.get(t);
                for (TimeRange range : freeList) {
                    LocalTime start = range.start;
                    LocalTime end = range.end;

                    // Tính tổng số phút rảnh
                    int freeMinutes = (int) java.time.Duration.between(start, end).toMinutes();

                    // Nếu rảnh dài hơn estimatedTime, chia nhỏ ra
                    int numSlots = freeMinutes / estimatedTime;

                    for (int i = 0; i < numSlots; i++) {
                        LocalTime slotStart = start.plusMinutes(i * estimatedTime);
                        LocalTime slotEnd = slotStart.plusMinutes(estimatedTime);
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                        String slotStr = slotStart.format(fmt) + " - " + slotEnd.format(fmt);
                        results.computeIfAbsent(slotStr, k -> new ArrayList<>()).add(t);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    
    private List<TimeRange> getFreeSlots(List<TimeRange> busyList, LocalTime dayStart, LocalTime dayEnd) {
        List<TimeRange> free = new ArrayList<>();
        busyList.sort((a, b) -> a.start.compareTo(b.start));
        LocalTime current = dayStart;
        for (TimeRange busy : busyList) {
            if (current.isBefore(busy.start)) {
                free.add(new TimeRange(current, busy.start));
            }
            if (current.isBefore(busy.end)) {
                current = busy.end;
            }
        }
        if (current.isBefore(dayEnd)) {
            free.add(new TimeRange(current, dayEnd));
        }
        return free;
    }
}
