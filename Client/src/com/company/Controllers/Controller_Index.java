package com.company.Controllers;

import com.company.Main;
import com.company.StudentsModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_Index {
    @FXML
    private AnchorPane anchorpane1;

    @FXML
    private JFXHamburger hamburger1;

    @FXML
    private JFXDrawer drawer1;

    @FXML
    public Button submitButtonToAddTopic;

    @FXML
    private TableView<StudentsModel> tbData;
    @FXML
    public TableColumn<StudentsModel, String> subjectColumn;

    @FXML
    public TableColumn<StudentsModel, String> topicColumn;

    @FXML
    public TableColumn<StudentsModel, String> dateColumn;

    @FXML
    public TableColumn<StudentsModel, String> ratingColumn;

    @FXML
    private PieChart pieChart;

    private void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/com/company/icons/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChart() {

        PieChart.Data slice1 = new PieChart.Data("Classes", 213);
        PieChart.Data slice2 = new PieChart.Data("Attendance", 67);
        PieChart.Data slice3 = new PieChart.Data("Teachers", 36);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);

    }


    private ObservableList<StudentsModel> studentsModels = FXCollections.observableArrayList(
            new StudentsModel("Data Structure", "Amos", "Chepchieng", "Data "),
            new StudentsModel("Data Structure", "Amos", "Mors", "Full "),
            new StudentsModel("Data Structure", "Amos", "Chepchieng", "Data "),
            new StudentsModel("Data Structure", "Amos", "Mors", "Data ")
    );


    private void loadStudents() {
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventdate"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tbData.setItems(studentsModels);
    }

    @FXML
    public void initialize() throws IOException {
        Main.stringToEcho.writeObject("TeacherIndexPage");
        Main.stringToEcho.flush();

        loadChart();
        loadStudents();

        VBox box1 = FXMLLoader.load(getClass().getResource("/com/company/fxml/DrawerContent.fxml"));
        drawer1.setSidePane(box1);
        for (Node node : box1.getChildren()) {
            if (node.getAccessibleText() != null) {
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    switch (node.getAccessibleText()) {
                        case "Material_Six": {
                            anchorpane1.setBackground(new Background(new BackgroundFill(Paint.valueOf("orange"), CornerRadii.EMPTY, Insets.EMPTY)));
                            break;
                        }
                    }
                });
            }
        }

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger1);
        transition.setRate(-1);
        hamburger1.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (drawer1.isOpened()) {
                drawer1.close();
            } else {
                drawer1.open();
            }
        });
    }

    public void openPopUpToAddTopic(ActionEvent event) {
        if (event.getSource() == submitButtonToAddTopic) {
            loadStage("/com/company/fxml/popUpTopic.fxml");
        }
    }
}
