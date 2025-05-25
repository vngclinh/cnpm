package dao;

import static dao.DAO.con;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Customer;

public class CustomerDAO extends DAO{
    public ArrayList<Customer> searchCustomer(String key) throws SQLException{
        ArrayList<Customer> result = new ArrayList<Customer>();
        String sql = "Select * from tblCustomer where fullname like ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+key+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFullname(rs.getString("fullname"));
                c.setTelnum(rs.getString("telnum"));
                c.setAddress(rs.getString("address"));
                c.setNote(rs.getString("note"));
                result.add(c);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public void addCustomer(Customer c) throws SQLException{
        String sql = "Insert into tblCustomer(fullname, telnum, address, note) values(?,?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getFullname());
            ps.setString(2, c.getTelnum());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getNote());
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
