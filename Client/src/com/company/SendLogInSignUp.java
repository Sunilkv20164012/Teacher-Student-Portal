package com.company;

import com.company.Messages.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SendLogInSignUp {

    public SendLogInSignUp(Socket socket, String username, String password) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        ObjectOutputStream stringToEcho = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream echoes = new ObjectInputStream(socket.getInputStream());

        Frame frame = null;
        try {
            frame = (Frame) echoes.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assert frame != null;
        Key key = frame.key;

        MessageEncryption messUsername = new MessageEncryption(username, key);
        MessageEncryption messPassword = new MessageEncryption(password, key);

        String usernameEnc = messUsername.getMessage();
        String passwordEnc = messPassword.getMessage();

        MessageAuth messageFirstPacket = new MessageAuth(usernameEnc, "NoNeedLoginEmail", passwordEnc);
        stringToEcho.writeObject(messageFirstPacket);
        stringToEcho.flush();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter message");
            String text = scanner.nextLine();
            MessageEncryption mess = new MessageEncryption(text, key);
            String cipherText = mess.getMessage();
            String from = "Client - " + username;
            Message message = new Message(cipherText, from, "Server", key);
            stringToEcho.writeObject(message);
            stringToEcho.flush();

            Message messageServer;
            try {
                messageServer = (Message) echoes.readObject();
                System.out.println("Received server input : ");
                MessageDecryption messDec = new MessageDecryption(messageServer.getMessage(), key);
                System.out.println(messDec.getMessage() + "FROM " + messageServer.getFrom());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
