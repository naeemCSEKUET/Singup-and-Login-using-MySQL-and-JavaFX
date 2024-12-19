
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomePageController implements Initializable {

    @FXML
    private Label idname;
    @FXML
    private Label idemail;
    private Connection connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void loadUserData(String userEmail) {
        try {
            // Establish the database connection
            connection = DatabaseConnection.getConnection();

            // Query to get the name and email from the users table based on email
            String query = "SELECT name, email FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userEmail);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the name and email from the result set
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                // Display the name and email in labels on the homepage
                idname.setText("Welcome, " + name + "!");
                idemail.setText(email);
            } else {
                // If no user is found, display an error message
                idname.setText("User not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            idname.setText("Error retrieving data.");
        }
    }
}
