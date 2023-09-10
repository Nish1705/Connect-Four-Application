package com.intenshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final  int COLUMNS = 7;
    private static final  int ROWS = 6;
    private static final  int CIRCLE_DIAMETER = 80;
    private static final  String discColor1 = "#24303E";
    private static final  String discColor2 = "#4CAA88";

    private static String PLAYER_ONE  = "Player One";

    private static String PLAYER_TWO  = "Player Two";

    private  static boolean isPlayerOneTurn = true;
    private boolean isAllowedToInsert = true;


    @FXML
    public Button setNameBtn;
    public GridPane grid;
    public Label playerNameLabel;
    public Pane insertedDiscs;
    public VBox vBox;
    public TextField player1;
    public TextField player2;
    private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS];






    public void createPlayground(){

        Shape rectangleWithHoles = createGameStructure();
        grid.add(rectangleWithHoles,0,1);

        List<Rectangle> rectangleList = createClickableRectangle();




        for (Rectangle rect: rectangleList) {
            grid.add(rect,0,1);

        }
        setNameBtn.setOnAction(actionEvent -> setNames());
        player1.setOnAction(actionEvent -> setNames());
        player2.setOnAction(actionEvent -> setNames());


    }


    private Shape createGameStructure(){

        Shape rectangleWithHoles = new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);

        Circle circle = new Circle();
        circle.setCenterX(CIRCLE_DIAMETER/2);
        circle.setCenterY(CIRCLE_DIAMETER);
        circle.setRadius(CIRCLE_DIAMETER/2);
        circle.setSmooth(true);

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {


                rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);
                circle.setCenterX(circle.getCenterX()+CIRCLE_DIAMETER+CIRCLE_DIAMETER/8);

            }
            circle.setCenterX(CIRCLE_DIAMETER/2);
            circle.setCenterY(circle.getCenterY()+CIRCLE_DIAMETER+CIRCLE_DIAMETER/12);


        }

        playerNameLabel.setText(PLAYER_ONE);

        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }

    private List<Rectangle> createClickableRectangle(){

        List<Rectangle> rectangleList = new ArrayList<>();


        for (int col = 0; col < COLUMNS; col++) {

            Rectangle rect  = new Rectangle(CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
            rect.setX(CIRCLE_DIAMETER/2);
            rect.setFill(Color.TRANSPARENT);

            rect.setTranslateX(col*(CIRCLE_DIAMETER+CIRCLE_DIAMETER/8));
            rect.setOnMouseEntered(event -> rect.setFill(Color.valueOf("eeeeee26")));
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
            final int column = col;
            rect.setOnMouseClicked(mouseEvent -> {
                if(isAllowedToInsert) {
                    isAllowedToInsert = false;
                    insertDisc(new Disc(isPlayerOneTurn), column);
                    if (isPlayerOneTurn == true) {
                        isPlayerOneTurn = false;
                    } else {
                        isPlayerOneTurn = true;
                    }
                }


            });


            rectangleList.add(rect);




        }

        return rectangleList;
    }
    private  void insertDisc(Disc disc,int column){
        int row = ROWS-1;
        while(row>=0){
            if (getDiscIfPresent(row,column) == null) {
                break;
            }
            row--;
        }
        if (row<0)
        {
            if (isPlayerOneTurn == true) {
                isPlayerOneTurn = false;
            } else {
                isPlayerOneTurn = true;
            }
            isAllowedToInsert=true;
            return;
        }


        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);

        insertedDiscArray[row][column]=disc;
        disc.setCenterX(disc.getCenterX()+column*(CIRCLE_DIAMETER+CIRCLE_DIAMETER/8));
        insertedDiscs.getChildren().add(disc);

        translateTransition.setToY(CIRCLE_DIAMETER/2+CIRCLE_DIAMETER/8+row*(CIRCLE_DIAMETER+CIRCLE_DIAMETER/12));

        int currentRow = row;
        translateTransition.setOnFinished(actionEvent -> {
            if (gameEnded(currentRow,column)){
                gameOver();
            }
            if(allPlacesFull() && gameEnded(currentRow,column)==false){
                gameDrawn();
            }

            isAllowedToInsert = true;
//            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE:PLAYER_TWO);


        });
        translateTransition.play();



    }

    private void gameDrawn() {


        Alert showWinner = new Alert(Alert.AlertType.INFORMATION);
        showWinner.setTitle("GAME OVER");
        showWinner.setHeaderText("The Game Is Drawn as all the places have been full");
        showWinner.setContentText("Do you want to play the game again? ");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        showWinner.getButtonTypes().setAll(yes,no);

//        showWinner.show();
        Platform.runLater(() -> {
            Optional<ButtonType> response = showWinner.showAndWait();
            if(response.get() == no){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to quit the game?");
                Optional<ButtonType> finalResponse = alert.showAndWait();

                if(finalResponse.get() == ButtonType.OK ){
                    Platform.exit();
                    System.exit(0);
                }else{
                    resetGame();
                }
            }else {
                resetGame();
            }
        });

    }

    private void gameOver() {
        String winner = isPlayerOneTurn? PLAYER_TWO:PLAYER_ONE;
        Alert showWinner = new Alert(Alert.AlertType.INFORMATION);
        showWinner.setTitle("GAME OVER");
        showWinner.setHeaderText("The Winner of this Connect Four game is..."+winner);
        showWinner.setContentText("Do you want to play the game again? ");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        showWinner.getButtonTypes().setAll(yes,no);

//        showWinner.show();
        Platform.runLater(() -> {
            Optional<ButtonType> response = showWinner.showAndWait();
            if(response.get() == no){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to quit the game?");
                Optional<ButtonType> finalResponse = alert.showAndWait();

                if(finalResponse.get() == ButtonType.OK ){
                    Platform.exit();
                    System.exit(0);
                }else{
                    resetGame();
                }
            }else {
                resetGame();
            }
        });
    }

    public void resetGame() {

        insertedDiscs.getChildren().clear();
        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {
                insertedDiscArray[row][column]=null;

            }
        }
        isPlayerOneTurn=true;
        playerNameLabel.setText(PLAYER_ONE);
        createPlayground();

    }

    private boolean gameEnded(int row,int column) {
        List<Point2D> verticalPoint = IntStream.rangeClosed(row-3,row+3)
                .mapToObj(r -> new Point2D(r,column))
                .collect(Collectors.toList());

        List<Point2D> horizontalPoint = IntStream.rangeClosed(column-3,column+3)
                .mapToObj(col -> new Point2D(row,col))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row-3,column+3);
        List<Point2D> diagonalPoint1 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint1.add(i,-i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row-3,column-3);
        List<Point2D> diagonalPoint2 = IntStream.rangeClosed(0,6)
                .mapToObj(i -> startPoint2.add(i,i))
                .collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalPoint)  || checkCombinations(horizontalPoint)
                || checkCombinations(diagonalPoint1) || checkCombinations(diagonalPoint2);

        return isEnded;
    }


    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;
        for (Point2D point : points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);

            if(disc != null && disc.isPlayerOneMove != isPlayerOneTurn ){
                chain++;
                if (chain==4){
                    return true;
                }
            }else
                chain = 0;
        }


        return false;
    }

    private boolean allPlacesFull() {
        int n = 0;

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                if (getDiscIfPresent(row, column) != null) {
                    n+=0;
                }else {
                    n+=1;
                    break;
                }

            }
        }
        if (n==0){
            return true;
        }
        return false;
    }

    private Disc getDiscIfPresent(int row, int column){

        if(row<0 || row>=ROWS ||column<0 || column>=COLUMNS){
            return null;
        }
        return insertedDiscArray[row][column];
    }

    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove=isPlayerOneMove;

            setRadius(CIRCLE_DIAMETER/2);

            setFill(isPlayerOneMove? Color.valueOf(discColor1):Color.valueOf(discColor2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
        }
    }

    private void setNames(){
        inValidName();

        if (player1.getText()==""){
            PLAYER_ONE ="Player 1";
        }else{
            PLAYER_ONE = player1.getText();
            playerNameLabel.setText(PLAYER_ONE);
        }
        if (player2.getText()==""){
            PLAYER_TWO="Player 2";
        }else{
            PLAYER_TWO= player2.getText();
        }

    }
    private void inValidName(){

        String b = player2.getText();
        String a = player1.getText();


        try {
            if(a.length()>=10 || b.length()>=10){
                if(a.length()>10 ){
                    player1.setText("");
                }
                if(b.length()>10 ){
                    player2.setText("");
                }
                Alert invName = new Alert(Alert.AlertType.INFORMATION,"Please Enter a Smaller name.");
                invName.show();
            }

        }catch (Exception e){
            System.out.println("Error");
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
