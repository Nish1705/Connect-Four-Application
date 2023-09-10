package com.intenshala.connectfour;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {

    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("hello-view.fxml"));
        GridPane rootNode = loader.load();
        Scene scene = new Scene(rootNode);

        stage.setScene(scene);
        controller = loader.getController();
        controller.createPlayground();


        MenuBar mb = createMenu();

        MenuBar empty = emptyMenu();
        VBox myVbox = (VBox) rootNode.getChildren().get(0);
//        empty.prefWidthProperty().bind(myVbox.prefWidthProperty());
        mb.prefWidthProperty().bind(stage.widthProperty());

        myVbox.getChildren().addAll(empty,mb);


        rootNode.getChildren().add(mb);


        stage.setResizable(false);
        stage.setTitle("Connect Four");
        stage.show();
    }

    private MenuBar emptyMenu() {

        Menu menu = new Menu();
        MenuBar empty = new MenuBar(menu);
        return empty;
    }

    public MenuBar createMenu() {
        Menu file = new Menu("File");
        MenuItem ne = new MenuItem("New Game");
        MenuItem resetGame = new MenuItem("Reset Game");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem quit = new MenuItem("Exit Game");

        ne.setOnAction(actionEvent -> controller.resetGame());

        quit.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });
        resetGame.setOnAction(actionEvent -> {
            controller.resetGame();

        });

        file.getItems().addAll(ne, resetGame, sep, quit);

        Menu help = new Menu("Help");
        MenuItem about1 = new MenuItem("About Connect 4");
        MenuItem about2 = new MenuItem("About Me");
        help.getItems().addAll(about1,about2);

        about1.setOnAction(actionEvent -> aboutApp());
        about2.setOnAction(actionEvent -> aboutDev());


        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(file, help);

        return mb;

    }

    private void aboutApp() {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("Connect Four Game");
        alertDialog.setHeaderText("How to play ?");

        alertDialog.setContentText("Connect Four is a two-player connection game in which the \n" +
                "players first choose a color and then take turns dropping colored discs \n" +
                "from the top into a seven-column, six-row vertically suspended grid.\n" +
                "The pieces fall straight down, occupying the next available space within the column.\n" +
                "The objective of the game is to be the first to form a horizontal, vertical, \n" +
                "or diagonal line of four of one's own discs. Connect Four is a solved game. \n" +
                "The first player can always win by playing the right moves.");
        alertDialog.show();
    }

    public void aboutDev() {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("Developer Information ");
        alertDialog.setHeaderText("About Me");
        alertDialog.setContentText("This App is made by Nish Parikh Studying in Ahmedabad University");
        alertDialog.show();
//        ButtonType y = new ButtonType("Yes");
//        ButtonType n = new ButtonType("No");
//
//        alertDialog.getButtonTypes().setAll(y,n);
//
//        Optional<ButtonType> buttonType = alertDialog.showAndWait();
//
//        if (buttonType.get() == y ){
//            System.out.println("Yes Clicked");
//        }else{
//            System.out.println("No Clicked");
//
//        }
    }
}