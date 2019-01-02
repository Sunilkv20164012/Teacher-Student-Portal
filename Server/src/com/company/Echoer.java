package com.company;

import com.company.Messages.*;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Echoer extends Thread {
    private Socket socket;

    public Echoer(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            DBConnect connect = new DBConnect();
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            String sessionClientUsername = null;

            // get a AES private key  (Generates the key)
            System.out.println("\nStart generating AES key");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);    //In AES cipher block size is 128 bits
            Key key = keyGen.generateKey();
            System.out.println("Finish generating AES key");

            Frame frame = new Frame();
            frame.key = key;
            output.writeObject(frame);
            output.flush();
            System.out.println("Key has been sent to client");

            try {
                MessageDecryption username;
                MessageDecryption email;
                MessageDecryption password;
                boolean val = true;
                while (val) {
                    MessageAuth messageFirstPacket = (MessageAuth) input.readObject();
                    username = new MessageDecryption(messageFirstPacket.getUsername(), key);
                    email = new MessageDecryption(messageFirstPacket.getEmail(), key);
                    password = new MessageDecryption(messageFirstPacket.getPassword(), key);
                    System.out.println("Username: " + username.getMessage());
                    System.out.println("Email: " + email.getMessage());
                    System.out.println("Password : " + password.getMessage());

                    MessageEncryption messEnc;
                    // Validating for Sign Up
                    if (!email.getMessage().equals("NoNeedLoginEmail")) {
                        if (!connect.checkUser(username.getMessage(), email.getMessage())) {
                            messEnc = new MessageEncryption("Username or email already exists.", key);
                            System.out.println("Username or email already exists. Form Error!");
                        } else {
                            messEnc = new MessageEncryption("Registration Successful!", key);
                            System.out.println("Registration Successful!");
                            connect.newUser(username.getMessage(), email.getMessage(), password.getMessage());
                            sessionClientUsername = username.getMessage();
                            val = false;
                        }
                    } // Validating for Sign In
                    else {
                        if (!connect.checkUserLogIn(username.getMessage(), password.getMessage())) {
                            messEnc = new MessageEncryption("Not a valid combination", key);
                            System.out.println("Not a valid combination. Form Error!");
                        } else {
                            messEnc = new MessageEncryption("Login Successful!", key);
                            System.out.println("Login Successful!");
                            sessionClientUsername = username.getMessage();
                            val = false;
                        }
                    }
                    Message messageSend = new Message(messEnc.getMessage(), "server", username.getMessage(), key);
                    output.writeObject(messageSend);
                    output.flush();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

//            while (true) {
//                Message message = null;
//                MessageDecryption mess = null;
//                String plainMessage = "";
//                try {
//                    message = (Message) input.readObject();
//                    mess = new MessageDecryption(message.getMessage(), key);
//                    plainMessage = mess.getMessage();
//                    if (plainMessage.equals("exit")) {
//                        socket.close();
//                        break;
//                    }
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("Received client input : ");
//                System.out.println(plainMessage + " FROM " + message.getFrom());
//
//                MessageEncryption messEnc = new MessageEncryption("Server thik chal raha hai", key);
//                Message messageSend = new Message(messEnc.getMessage(), "server", message.getFrom(), key);
//                output.writeObject(messageSend);
//                output.flush();
//            }

            while (true) {
                String pageType = (String) input.readObject();
                System.out.println("Entering in Page type " + pageType);
                String pageTypeFromDefault = "";
                EXITED:
                switch (pageType){
                    case "Teachers":
                        System.out.println("Entering in Teacher type ");
                        while (true) {
                            int switchFlag = 0;
                            String messageType = (String) input.readObject();
                            switch (messageType){
                                case "TeacherIndexPage":
                                    System.out.println("Welcome to teachers case :");
                                    ArrayList list = connect.returnStatusTeacher(sessionClientUsername);
                                    // list me database se string le lo
                                    MessageEncryption usernameEnc = new MessageEncryption((String) list.get(0), key);
                                    MessageEncryption nameEnc = new MessageEncryption((String) list.get(1), key);
                                    MessageEncryption genderEnc = new MessageEncryption((String) list.get(2), key);
                                    MessageEncryption dobEnc = new MessageEncryption((String) list.get(3), key);
                                    MessageEncryption contactEnc = new MessageEncryption((String) list.get(4), key);
                                    MessageEncryption qualificationEnc = new MessageEncryption((String) list.get(5), key);
                                    MessageEncryption collegeEnc = new MessageEncryption((String) list.get(6), key);
                                    MessageEncryption optionalEnc = new MessageEncryption((String) list.get(7), key);
                                    MessageStatus messageStatusSend = new MessageStatus(usernameEnc.getMessage(), nameEnc.getMessage(), genderEnc.getMessage(), dobEnc.getMessage(), contactEnc.getMessage(), qualificationEnc.getMessage(), collegeEnc.getMessage(), optionalEnc.getMessage());
                                    output.writeObject(messageStatusSend);
                                    output.flush();
                                    break;
                                case "MessageStatus":
                                    System.out.println("Welcome to MessageStatus case :");
                                    // Receive data from client to update his/her status
                                    MessageStatus messageStatus;
                                    messageStatus = (MessageStatus) input.readObject();

                                    MessageDecryption usernameDec = new MessageDecryption(messageStatus.getUsername(), key);
                                    MessageDecryption nameDec = new MessageDecryption(messageStatus.getName(), key);
                                    MessageDecryption genderDec = new MessageDecryption(messageStatus.getGender(), key);
                                    MessageDecryption dobDec = new MessageDecryption(messageStatus.getDob(), key);
                                    MessageDecryption contactDec = new MessageDecryption(messageStatus.getContact(), key);
                                    MessageDecryption qualificationDec = new MessageDecryption(messageStatus.getQualification(), key);
                                    MessageDecryption collegeDec = new MessageDecryption(messageStatus.getCollege(), key);
                                    MessageDecryption optionalDec = new MessageDecryption(messageStatus.getOptional(), key);

                                    String username = usernameDec.getMessage();
                                    String name = nameDec.getMessage();
                                    String gender = genderDec.getMessage();
                                    String dob = dobDec.getMessage();
                                    String contact = contactDec.getMessage();
                                    String qualification = qualificationDec.getMessage();
                                    String college = collegeDec.getMessage();
                                    String optional = optionalDec.getMessage();

                                    boolean stat = connect.updateTeacherStatus(username, name, gender, dob, contact, qualification, college, optional);
                                    MessageEncryption messStatusEnc;
                                    if (stat) {
                                        messStatusEnc = new MessageEncryption("Status updated successfully", key);
                                    }
                                    else {
                                        messStatusEnc = new MessageEncryption("Unable to update status", key);
                                    }
                                    Message messageStatusUpdate = new Message(messStatusEnc.getMessage(), "server", username, key);
                                    output.writeObject(messageStatusUpdate);
                                    output.flush();
                                    break;

                                case "MessagePopUpTopic":
                                    System.out.println("Welcome to MessagePopUpTopic case :");
                                    MessagePopUpTopic messagePopUpTopic;
                                    messagePopUpTopic = (MessagePopUpTopic) input.readObject();
                                    MessageDecryption encUsername = new MessageDecryption(messagePopUpTopic.getUsername(), key);
                                    MessageDecryption encSubject = new MessageDecryption(messagePopUpTopic.getSubject(), key);
                                    MessageDecryption encTopic = new MessageDecryption(messagePopUpTopic.getTopic(), key);
                                    MessageDecryption encDate = new MessageDecryption(messagePopUpTopic.getDate(), key);
                                    MessageDecryption encNoOfSingleCorrect = new MessageDecryption(messagePopUpTopic.getNoOfSingleCorrect(), key);
                                    MessageDecryption encSingleTimeLimit = new MessageDecryption(messagePopUpTopic.getSingleTimeLimit(), key);
                                    MessageDecryption encNoOfMultipleCorrect = new MessageDecryption(messagePopUpTopic.getNoOfMultipleCorrect(), key);
                                    MessageDecryption encMultipleTimeLimit = new MessageDecryption(messagePopUpTopic.getMultipleTimeLimit(), key);
                                    MessageDecryption encNoOfTrueFalse = new MessageDecryption(messagePopUpTopic.getNoOfTrueFalse(), key);
                                    MessageDecryption encTrueFalseTimeLimit = new MessageDecryption(messagePopUpTopic.getTrueFalseTimeLimit(), key);

                                    String Username = encUsername.getMessage();
                                    String Subject = encSubject.getMessage();
                                    String Topic = encTopic.getMessage();
                                    String Date = encDate.getMessage();
                                    String NoOfSingleCorrect = encNoOfSingleCorrect.getMessage();
                                    String SingleTimeLimit = encSingleTimeLimit.getMessage();
                                    String NoOfMultipleCorrect = encNoOfMultipleCorrect.getMessage();
                                    String MultipleTimeLimit = encMultipleTimeLimit.getMessage();
                                    String NoOfTrueFalse = encNoOfTrueFalse.getMessage();
                                    String TrueFalseTimeLimit = encTrueFalseTimeLimit.getMessage();

                                    connect.insertNewSubject(Username, Subject);
                                    int id = connect.getIdFromTeacher(Username, Subject);
                                    MessageEncryption messPopUpEnc;
                                    if (id != 0) {
                                        connect.insertNewTopic(id, Topic, Date, NoOfSingleCorrect, SingleTimeLimit, NoOfMultipleCorrect, MultipleTimeLimit, NoOfTrueFalse, TrueFalseTimeLimit);
                                        messPopUpEnc = new MessageEncryption("New topic has been added successfully", key);
                                    }else {
                                        messPopUpEnc = new MessageEncryption("Database error while coding", key);
                                        System.out.println("Database error while coding");
                                    }
                                    Message messageSend = new Message(messPopUpEnc.getMessage(), "server", Username, key);
                                    output.writeObject(messageSend);
                                    output.flush();
                                    break;

                                    default:
                                        switchFlag = 1;
                                        System.out.println("This is default messageType " + messageType);
                                        pageTypeFromDefault = messageType;
                                        break;
                            }
                            if (switchFlag == 1){
                                break ;
                            }
                        }
                    case "Students":

                        break;
                }
            }
        } catch (IOException e){
            System.out.println("Ooops : " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                System.out.println("Error in closing socket");
            }
        }
    }
}