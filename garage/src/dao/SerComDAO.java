package dao;

import java.util.ArrayList;
import model.ServiceComponent;
import java.sql.*;

public class SerComDAO extends DAO {
    public ArrayList<ServiceComponent> searchSerCom(String key) throws SQLException{
        ArrayList<ServiceComponent> result = new ArrayList<ServiceComponent>();
        String sql = "select * from tblSerCom where name like ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+key+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                    int id = rs.getInt("id");
                    System.out.println("ID from DB: " + id); 
                ServiceComponent sc = new ServiceComponent();
                sc.setId(rs.getInt("id"));
                sc.setName(rs.getString("name"));
                sc.setPrice(rs.getDouble("price"));
                sc.setDescrption(rs.getString("description"));
                sc.setEstimatedTime(rs.getInt("estimatedTime"));
                result.add(sc);
            }
            rs.close();
            ps.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
