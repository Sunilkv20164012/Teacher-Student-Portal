package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBConnect {
    private Statement st;

    // Constructor
    public DBConnect(){
        try{
            Connection con;
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/softaQuiz","root", "");
            st = con.createStatement();

        }catch (Exception e){
            System.out.println("Error in connecting database " + e);
        }
    }

    public void getData(){
        try {
            ResultSet rs;
            String query = "select * from users";
            rs = st.executeQuery(query);
            System.out.println("Records from Database:");
            while(rs.next()){
                String name = rs.getString("username");
                String email = rs.getString("email");

                System.out.println("Hello "+ name + " your email is " + email);
            }
        }catch (Exception e){
            System.out.println("No result found");
        }
    }

    // Data for Controller_registration
    public void newUser(String Username, String Email, String Password){
        try {
            String query = "insert into users(username, email, password) values ('"+Username+"', '"+Email+"', '"+Password+"')";
            st.executeUpdate(query);
            System.out.println("Successfully registered");
        }catch (Exception e){
            System.out.println("Unable to register");
        }
    }
    // Validate existing user for Sign Up
    public boolean checkUser(String Username, String Email){
        try {
            int count = 0;
            ResultSet rs;
            String query = "select * from users where username = '"+Username+"' or email = '"+Email+"'";
            rs = st.executeQuery(query);
            while (rs.next()){
                ++count;
            }
            System.out.println("Number of users for signUp: "+ count);
            return count == 0;
        }catch (Exception e){
            System.out.println("Unable to check register");
            return false;
        }
    }

    // Validate existing User for Log In
    public boolean checkUserLogIn(String Username, String Password) {
        try {
            int count = 0;
            ResultSet rs;
            String query = "select * from users where username = '"+Username+"' and password = '"+Password+"'";
            rs = st.executeQuery(query);
            while (rs.next()){
                ++count;
            }
            System.out.println("Number of users for login: "+ count);

            return count == 1;
        }catch (Exception e){
            System.out.println("Unable to check register");
            return false;
        }
    }

    // Data for Inserting and Updating Status of Teacher
    public boolean updateTeacherStatus(String Username, String Name, String Gender, String Dob, String Contact, String Qualification, String College, String Optional){
        try {
            int count_row = 0;
            ResultSet rs;
            String query = "select * from users where username = '"+Username+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                ++count_row;
            }
            if (count_row != 0) {
                System.out.println(count_row);
                String query2 = "update users SET name = '"+Name+"', gender = '"+Gender+"', dob = '"+Dob+"', contact = '"+Contact+"' , qualification = '"+Qualification+"', college = '"+College+"', optional = '"+Optional+"' where username = '"+Username+"'";
                st.executeUpdate(query2);
                System.out.println("Status Successfully updated");
                return true;
            }
            else {
                String query1 = "insert into users(username, name, gender, dob, contact, qualification, college, optional) values ('" + Username + "', '" + Name + "', '" + Gender + "', '" + Contact + "', '" + Qualification + "', '" + College + "', '" + Optional + "' )";
                st.executeUpdate(query1);
                return true;
            }
        }catch (Exception e){
            System.out.println("Unable to update teacher status");
            return false;
        }
    }

    public ArrayList returnStatusTeacher(String Username) {
        try {
            ResultSet rs;
            String query = "select * from users where username = '"+Username+"'";
            rs = st.executeQuery(query);
            System.out.println("Records from Database for Teacher status update:");
            ArrayList<String> list = new ArrayList<String>();
            while(rs.next()){
                String name = rs.getString("username");
                System.out.println(name);
//                String email = rs.getString("name");
//                String name = rs.getString("gender");
//                String email = rs.getString("dob");
//                String name = rs.getString("contact");
//                String email = rs.getString("qualification");
//                String name = rs.getString("college");
//                String email = rs.getString("optional");
                list.add(rs.getString("username"));
                list.add(rs.getString("name"));
                list.add(rs.getString("gender"));
                list.add(rs.getString("dob"));
                list.add(rs.getString("contact"));
                list.add(rs.getString("qualification"));
                list.add(rs.getString("college"));
                list.add(rs.getString("optional"));
            }
            return list;
        }catch (Exception e){
            ArrayList<String> list = new ArrayList<String>();
            System.out.println("No result found");
            list.add(" ");
            return list;
        }
    }

    public void insertNewSubject(String Username, String Subject) {
        try {
            int count_row = 0;
            ResultSet rs;
            String query = "select * from teachers where username = '"+Username+"' and subject = '"+Subject+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                ++count_row;
            }
            if (count_row == 0) {
                String query2 = "insert into teachers(username, subject) values ('"+Username+"', '"+Subject+"')";
                st.executeUpdate(query2);
                System.out.println("Successfully added new subject");
            }
        }catch (Exception e){
            System.out.println("Unable to add new subject");
        }
    }

    public int getIdFromTeacher(String Username, String Subject) {
        try {
            int count_row = 0;
            int returnId = 0;
            ResultSet rs;
            String query = "select * from teachers where username = '"+Username+"' and subject = '"+Subject+"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                ++count_row;
                returnId = rs.getInt("id");
            }
            if (count_row == 1) {
                System.out.println("Successfully returned the id " + returnId);
                return returnId;
            }
            else
                return 0;
        }catch (Exception e){
            System.out.println("Unable to return id");
            e.printStackTrace();
            return 0;
        }
    }

    public void insertNewTopic(int Id, String Topic, String Date, String NoOfSingleCorrect, String SingleTimeLimit, String NoOfMultipleCorrect, String MultipleTimeLimit, String NoOfTrueFalse, String TrueFalseTimeLimit) {
        try {
            String query = "insert into topics(id, topic, topicDate, single, singleTime, multiple, multipleTime, trueFalse, trueFalseTime) values ("+Id+", '"+Topic+"', '"+Date+"', '"+NoOfSingleCorrect+"', '"+SingleTimeLimit+"', '"+NoOfMultipleCorrect+"', '"+MultipleTimeLimit+"', '"+NoOfTrueFalse+"', '"+TrueFalseTimeLimit+"' )";
            st.executeUpdate(query);
            System.out.println("Successfully added new subject");
        } catch (SQLException e1) {
            System.out.println("Unable to add new subject");
        }
    }

    public List<String[]> returnTeacherTable() {
        List<String[]> rowList = new ArrayList<>();

        rowList.add(new String[] { "Saurabh", "Kumar", "Soni", "Shivam" });
        rowList.add(new String[] { "Saurabh", "gaurabh", "Soni", "Suhas" });
        rowList.add(new String[] { "Saurabh", "Kumar", "Soni" , "Shivam"});

        for (String[] row : rowList) {
            System.out.println("Row = " + Arrays.toString(row));
        }

        System.out.println(rowList.get(1)[1]);
        return rowList;
    }
}
