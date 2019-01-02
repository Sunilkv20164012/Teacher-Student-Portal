package com.company.Controllers;

import com.company.*;
import com.company.Messages.Message;
import com.company.Messages.MessageAuth;
import com.company.Messages.MessageDecryption;
import com.company.Messages.MessageEncryption;
import com.company.alertBox.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Controller_registration implements Initializable {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button switchToSignIn;


    private boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Stage stage;
        Parent root;
        if(event.getSource()==switchToSignIn){
            stage = (Stage) switchToSignIn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/com/company/fxml/login_form.fxml"));
            Scene scene = new Scene(root, 800, 500);
            stage.setScene(scene);
            stage.show();
            return;
        }
        Window owner = submitButton.getScene().getWindow();
        if(nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your name");
            return;
        }
        if(emailField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your email id");
            return;
        }
        if (!validateEmailAddress(emailField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter a valid email id");
            return;
        }
        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter a password");
            return;
        }

        MessageEncryption messUsername = new MessageEncryption(nameField.getText(), Main.key);
        MessageEncryption messEmail = new MessageEncryption(emailField.getText(), Main.key);
        MessageEncryption messPassword = new MessageEncryption(passwordField.getText(), Main.key);

        String usernameEnc = messUsername.getMessage();
        String emailEnc = messEmail.getMessage();
        String passwordEnc = messPassword.getMessage();

        MessageAuth messageFirstPacket = new MessageAuth(usernameEnc, emailEnc, passwordEnc);
        Main.stringToEcho.writeObject(messageFirstPacket);
        Main.stringToEcho.flush();
        System.out.println("Message sent");

        Message messageServer;
        try {
            messageServer = (Message) Main.echoes.readObject();
            System.out.println("Received server input for Registration: ");
            MessageDecryption messDec = new MessageDecryption(messageServer.getMessage(), Main.key);
            System.out.println(messDec.getMessage() + "FROM " + messageServer.getFrom());

            if (messDec.getMessage().equals("Username or email already exists.")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Username or email already exists.");
            } else {
                AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!", "Welcome " + nameField.getText());
                Main.sessionUsername = nameField.getText();
                stage = (Stage) switchToSignIn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/com/company/fxml/dashBoard.fxml"));
                Scene scene = new Scene(root, 800, 500);
                stage.setScene(scene);
                stage.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
    }
}

