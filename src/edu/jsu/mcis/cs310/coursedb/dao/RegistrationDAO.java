import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationDAO {

    private static final Logger LOGGER = Logger.getLogger(RegistrationDAO.class.getName());

    // Other existing methods...

    // Method to drop a previous registration for a single course
    public boolean delete(int studentID, int termID, int crn) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection(); // Implement your getConnection() method

            // Prepare the SQL query to delete registration
            String sql = "DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            statement.setInt(2, termID);
            statement.setInt(3, crn);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if deletion was successful
        } catch (SQLException e) {
            // Log the exception
            LOGGER.log(Level.SEVERE, "Error in RegistrationDAO.delete", e);
            return false;
        } finally {
            // Close resources
            closeResources(connection, statement, null); // Implement this method to close resources
        }
    }

    // Overloaded method to withdraw from all registered courses
    public boolean delete(int studentID, int termID) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection(); // Implement your getConnection() method

            // Prepare the SQL query to delete all registrations for a student in a term
            String sql = "DELETE FROM registration WHERE studentid = ? AND termid = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            statement.setInt(2, termID);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if deletion was successful
        } catch (SQLException e) {
            // Log the exception
            LOGGER.log(Level.SEVERE, "Error in RegistrationDAO.delete", e);
            return false;
        } finally {
            // Close resources
            closeResources(connection, statement, null); // Implement this method to close resources
        }
    }

    // Method to list all registered courses for a given student ID and term ID
    public String list(int studentID, int termID) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = getConnection(); // Implement your getConnection() method

            // Prepare the SQL query to list registered courses
            String sql = "SELECT * FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            statement.setInt(2, termID);

            rs = statement.executeQuery();
            return convertResultSetToJson(rs);
        } catch (SQLException e) {
            // Log the exception
            LOGGER.log(Level.SEVERE, "Error in RegistrationDAO.list", e);
            return "[]";
        } finally {
            // Close resources
            closeResources(connection, statement, rs); // Implement this method to close resources
        }
    }

    // Other existing methods...

    // Helper method to convert ResultSet to JSON
    private String convertResultSetToJson(ResultSet rs) throws SQLException {
        List<String> jsonArray = new ArrayList<>();
        while (rs.next()) {
            StringBuilder json = new StringBuilder("{");
            json.append("\"studentid\":\"").append(rs.getInt("studentid")).append("\",");
            json.append("\"termid\":\"").append(rs.getInt("termid")).append("\",");
            json.append("\"crn\":\"").append(rs.getInt("crn")).append("\"");
            json.append("}");
            jsonArray.add(json.toString());
        }
        return "[" + String.join(",", jsonArray) + "]";
    }
}

