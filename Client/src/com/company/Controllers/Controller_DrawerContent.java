package com.company.Controllers;

import com.company.Main;
import com.company.Messages.Message;
import com.company.Messages.MessageDecryption;
import com.company.Messages.MessageEncryption;
import com.company.Messages.MessageStatus;
import com.company.alertBox.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Window;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Controller_DrawerContent {
    @FXML
    private GridPane inputGridPane;

    @FXML
    private TextField Material_Optional;

    @FXML
    private TextField Material_Name;

    @FXML
    private TextField Material_Gender;

    @FXML
    private TextField Material_DOB;

    @FXML
    private TextField Material_Contact;

    @FXML
    private TextField Material_Qualification;

    @FXML
    private TextField Material_College;

    @FXML
    private Button submitButtonStatus;


    @FXML
    public void initialize() throws IOException {
        inputGridPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("blue"), CornerRadii.EMPTY, Insets.EMPTY)));

        MessageStatus messageServer;
        try {
            messageServer = (MessageStatus) Main.echoes.readObject();
            System.out.println("Received server input for Status Update: ");

            MessageDecryption usernameDec = new MessageDecryption(messageServer.getUsername(), Main.key);
            MessageDecryption nameDec = new MessageDecryption(messageServer.getName(), Main.key);
            MessageDecryption genderDec = new MessageDecryption(messageServer.getGender(), Main.key);
            MessageDecryption dobDec = new MessageDecryption(messageServer.getDob(), Main.key);
            MessageDecryption contactDec = new MessageDecryption(messageServer.getContact(), Main.key);
            MessageDecryption qualificationDec = new MessageDecryption(messageServer.getQualification(), Main.key);
            MessageDecryption collegeDec = new MessageDecryption(messageServer.getCollege(), Main.key);
            MessageDecryption optionalDec = new MessageDecryption(messageServer.getOptional(), Main.key);

            Material_Name.setText(nameDec.getMessage());
            Material_Gender.setText(genderDec.getMessage());
            Material_DOB.setText(dobDec.getMessage());
            Material_Contact.setText(contactDec.getMessage());
            Material_Qualification.setText(qualificationDec.getMessage());
            Material_College.setText(collegeDec.getMessage());
            Material_Optional.setText(optionalDec.getMessage());

            System.out.println("FROM: " + usernameDec.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void getItFromShivam(ActionEvent event) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        MessageEncryption statusUsername = new MessageEncryption(Main.sessionUsername, Main.key);
        MessageEncryption statusName = new MessageEncryption(Material_Name.getText(), Main.key);
        MessageEncryption statusGender = new MessageEncryption(Material_Gender.getText(), Main.key);
        MessageEncryption statusDob = new MessageEncryption(Material_DOB.getText(), Main.key);
        MessageEncryption statusContact = new MessageEncryption(Material_Contact.getText(), Main.key);
        MessageEncryption statusQualification = new MessageEncryption(Material_Qualification.getText(), Main.key);
        MessageEncryption statusCollege = new MessageEncryption(Material_College.getText(), Main.key);
        MessageEncryption statusOptional = new MessageEncryption(Material_Optional.getText(), Main.key);

        String usernameEnc = statusUsername.getMessage();
        String nameEnc = statusName.getMessage();
        String genderEnc = statusGender.getMessage();
        String dobEnc = statusDob.getMessage();
        String contactEnc = statusContact.getMessage();
        String qualificationEnc = statusQualification.getMessage();
        String collegeEnc = statusCollege.getMessage();
        String optionalEnc = statusOptional.getMessage();

        Main.stringToEcho.writeObject("MessageStatus");
        Main.stringToEcho.flush();
        MessageStatus messageStatusPacket = new MessageStatus(usernameEnc, nameEnc, genderEnc, dobEnc, contactEnc, qualificationEnc, collegeEnc, optionalEnc);
        Main.stringToEcho.writeObject(messageStatusPacket);
        Main.stringToEcho.flush();
        System.out.println("Message sent");

        Message messageServerStatusUpdate;
        try {
            messageServerStatusUpdate = (Message) Main.echoes.readObject();
            System.out.println("Received server input for Status Update: ");
            MessageDecryption messDec = new MessageDecryption(messageServerStatusUpdate.getMessage(), Main.key);
            System.out.println(messDec.getMessage() + "FROM " + messageServerStatusUpdate.getFrom());

            Window owner = submitButtonStatus.getScene().getWindow();
            if (messDec.getMessage().equals("Unable to update status")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Unable to update status.");
            } else {
                AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Status Successfully updated !", "Status Successfully updated! ");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
