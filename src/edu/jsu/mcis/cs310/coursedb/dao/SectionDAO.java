package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO {
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termid, String subjectid, String num) {
        
        String result = "[]";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                String query = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
                ps = conn.prepareStatement(query);
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);

                rs = ps.executeQuery();
                result = convertResultSetToJson(rs);
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
    }
    
    private String convertResultSetToJson(ResultSet rs) throws Exception {
        List<String> jsonArray = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();

        while (rs.next()) {
            StringBuilder json = new StringBuilder("{");
            for (int i = 1; i <= numColumns; i++) {
                json.append("\"").append(rsmd.getColumnName(i)).append("\":\"").append(rs.getString(i)).append("\",");
            }
            // Remove the trailing comma
            json.deleteCharAt(json.length() - 1);
            json.append("}");
            jsonArray.add(json.toString());
        }

        return "[" + String.join(",", jsonArray) + "]";
    }
}
