import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationDAO {
    // Existing methods...

    // Method to register a student for a course
    public boolean create(int studentID, int termID, int crn) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection(); // Implement your getConnection() method

            // Prepare the SQL query to insert registration
            String sql = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            statement.setInt(2, termID);
            statement.setInt(3, crn);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(connection, statement, null); // Implement this method to close resources
        }
    }

    // Other existing methods...