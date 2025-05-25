package dao;

import static dao.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserDAO extends DAO{
    public UserDAO(){
        super();
    }
	public boolean checkLogin(User user) {
            boolean result = false;
            String sql = "SELECT id, fullname, role FROM tblUser WHERE username = ? AND password = ?";
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setFullname(rs.getString("fullname"));
                    user.setRole(rs.getString("role"));
                    result = true;
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            return result;
	}
}
