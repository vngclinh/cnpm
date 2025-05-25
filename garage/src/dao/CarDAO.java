package dao;

import java.util.ArrayList;
import model.Car;
import java.sql.*;

public class CarDAO extends DAO {
    public ArrayList<Car> searchCar(String key) throws SQLException{
        ArrayList<Car> result = new ArrayList<Car>();
        String sql = "select * from tblcar where plateNumber like ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+key+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Car c = new Car();
                c.setId(rs.getInt("id"));
                c.setPlateNum(rs.getString("platenumber"));
                c.setName(rs.getString("name"));
                c.setBrand(rs.getString("brand"));
                c.setType(rs.getString("type"));
                result.add(c);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public void addCar(Car c) throws SQLException{
        String sql = "insert into tblcar(platenumber, name, brand, type) values (?, ?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getPlateNum());
            ps.setString(2, c.getName());
            ps.setString(3, c.getBrand());
            ps.setString(4, c.getType());
            ps.executeUpdate();
            
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                c.setId(generatedKeys.getInt(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
