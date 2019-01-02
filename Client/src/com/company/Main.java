package com.company;

import com.company.Messages.Frame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.security.Key;

public class Main extends Application {

    public static Socket socket;
    public static ObjectOutputStream stringToEcho;
    public static ObjectInputStream echoes;
    public static Key key;
    public static String sessionUsername;

    @Override
    public void start(Stage primaryStage) throws IOException {
        setSocket();
        Parent root = FXMLLoader.load(getClass().getResource("/com/company/fxml/registration_form.fxml"));
        primaryStage.setTitle("Registration Form FXML Application");
        primaryStage.getIcons().add(new Image("/com/company/icons/icon.png"));
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

    }

    public static void main(String[] args) {
            launch(args);
    }

    private static void setSocket() throws IOException {
        socket = new Socket("localhost", 5700);
        stringToEcho = new ObjectOutputStream(socket.getOutputStream());
        echoes = new ObjectInputStream(socket.getInputStream());
        try {
            Frame frame = (Frame) echoes.readObject();
            key = frame.key;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}