
import java.io.IOException;
import java.sql.*;
import javafx.scene.*;
import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupFXMLController implements Initializable {

    @FXML
    private TextField idname;
    @FXML
    private TextField idemail;
    @FXML
    private TextField idpass;
    @FXML
    private TextField idadress;

    private Connection connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void signupbtnclick(ActionEvent event) {
        try {
            // Retrieve data from the form fields
            String name = idname.getText();
            String email = idemail.getText();
            String password = idpass.getText();
            String address = idadress.getText();
            connection = DatabaseConnection.getConnection();

            // Insert data into the database
            String query = "INSERT INTO users (name, email, pass, address) VALUES (?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, address);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                showAlert("Success", "Data saved successfully!", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Failure", "Failed to save data.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Invalid input or database error.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void loginbtnclick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginFXML.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) idemail.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the login page.", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        idname.clear();
        idemail.clear();
        idpass.clear();
        idadress.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
