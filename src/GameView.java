import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Random;

public class GameView extends Application {
    @FXML
    public GridPane cardTable;

    @Override
    // Override the start method in the Application class
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GameView.fxml"));
        stage.setTitle("Typewriter");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
        Card[] cardDeck = new Card[20];
        for (int i = 0; i < 20; i++){
            cardDeck[i] = new Card(i);
            System.out.println(cardDeck[i]);
        }
        int[] cardDrawn = new int[20];
        int holder = 0;
        for (int row = 0; row < 4; ++row) {
            holder = randomIntGen();
            for (int column = 0; column < 5; ++column) {
                if (!duplicateCard(cardDrawn, holder)) {
                    System.out.println(holder);
                    System.out.println(cardDeck[holder]);
                    cardTable.add(cardDeck[holder].backSide, column, row);
                    cardDrawn[row * 5 + column] = holder;
                    row++;
                    column++;
                }
            }
        }
    }




//        Rectangle msg = new Rectangle(50,50);
//        Pane pane = new Pane(msg);
//        pane.setPrefSize(500, 70);
//        Scene scene = new Scene(pane);
//        stage.setScene(scene);
//        stage.setTitle("Scrolling Text");
//        stage.show();
//        double sceneWidth = scene.getWidth();
//        double msgWidth = msg.getLayoutBounds().getWidth();
//        KeyValue initKeyValue1 = new KeyValue(msg.translateXProperty(), sceneWidth);
//        KeyValue initKeyValue2 = new KeyValue(msg.scaleXProperty(), 1.0);
//        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue1, initKeyValue2);
//        KeyValue endKeyValue1 = new KeyValue(msg.translateXProperty(), -1.0 * msgWidth);
//        KeyValue endKeyValue2 = new KeyValue(msg.scaleXProperty(), 0.2);
//        KeyFrame endFrame = new KeyFrame(Duration.millis(15000), endKeyValue1, endKeyValue2);
//        Timeline timeline = new Timeline(25 , initFrame, endFrame);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.setRate(1);
//        timeline.setAutoReverse(false);
//        timeline.play();

    public int randomIntGen(){
        Random rand = new Random();
        int rand_int = rand.nextInt(20);
        return rand_int - 1;
    }

    public boolean duplicateCard(int[] cardDrawn, int holder){
        for (int i = 0; i < cardDrawn.length;i++){
            if (holder == cardDrawn[i]) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

