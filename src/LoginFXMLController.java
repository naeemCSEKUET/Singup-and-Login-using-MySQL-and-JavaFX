
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginFXMLController implements Initializable {

    @FXML
    private TextField idemail;
    @FXML
    private TextField idpassword;

    private Connection connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onloginbtnclick(ActionEvent event) {

        String email = idemail.getText();
        String password = idpassword.getText();
        connection = DatabaseConnection.getConnection();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.", Alert.AlertType.ERROR);
            return;
        }

        if (verify_connection(email, password)) {
            switchtohomepage();
        } else {
            showAlert("Error", "Invalid email or password.", Alert.AlertType.ERROR);
        }

    }

    private boolean verify_connection(String email, String password) {

        Connection connection = null;
        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to verify email and password
            String query = "SELECT * FROM users WHERE email = ? AND pass = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();  // If there's a matching record, the login is successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchtohomepage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) idemail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            String email=idemail.getText();
            HomePageController homePageController = loader.getController();
            homePageController.loadUserData(email);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load home page.", Alert.AlertType.ERROR);
        }
    }

}
