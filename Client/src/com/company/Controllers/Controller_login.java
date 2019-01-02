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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Controller_login {
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button switchToSignUp;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Stage stage;
        Parent root;
        if(event.getSource()==switchToSignUp){
            stage = (Stage) switchToSignUp.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/com/company/fxml/registration_form.fxml"));
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
        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter a password");
            return;
        }

        MessageEncryption messUsername = new MessageEncryption(nameField.getText(), Main.key);
        MessageEncryption messEmail = new MessageEncryption("NoNeedLoginEmail", Main.key);
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
            System.out.println("Received server input for Login: ");
            MessageDecryption messDec = new MessageDecryption(messageServer.getMessage(), Main.key);
            System.out.println(messDec.getMessage() + "FROM " + messageServer.getFrom());

            if (messDec.getMessage().equals("Not a valid combination")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Not a valid combination.");
            } else {
                AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful!", "Welcome " + nameField.getText());
                Main.sessionUsername = nameField.getText();
                stage = (Stage) switchToSignUp.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/com/company/fxml/dashBoard.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String username, String password) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        MessageEncryption messUsername = new MessageEncryption(username, Main.key);
        MessageEncryption messPassword = new MessageEncryption(password, Main.key);

        String usernameEnc = messUsername.getMessage();
        String passwordEnc = messPassword.getMessage();

        MessageAuth messageFirstPacket = new MessageAuth(usernameEnc, "NoNeedLoginEmail", passwordEnc);
        Main.stringToEcho.writeObject(messageFirstPacket);
        Main.stringToEcho.flush();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter message");
            String text = scanner.nextLine();
            MessageEncryption mess = new MessageEncryption(text, Main.key);
            String cipherText = mess.getMessage();
            String from = "Client - " + username;
            Message message = new Message(cipherText, from, "Server", Main.key);
            Main.stringToEcho.writeObject(message);
            Main.stringToEcho.flush();

            Message messageServer;
            try {
                messageServer = (Message) Main.echoes.readObject();
                System.out.println("Received server input for login: ");
                MessageDecryption messDec = new MessageDecryption(messageServer.getMessage(), Main.key);
                System.out.println(messDec.getMessage() + "FROM " + messageServer.getFrom());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}