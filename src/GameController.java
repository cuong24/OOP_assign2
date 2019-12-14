import com.sun.prism.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class GameController {
    @FXML
    public GridPane cardTable;
    GameView view = new GameView();

    public void arrangeCard(){

        Card[] cardDeck = new Card[20];
        for (int i = 0; i < 20; i++){
            cardDeck[i] = new Card(i);
            System.out.println(cardDeck[i]);
        }
        int[] cardDrawn = new int[20];
        int holder = 0;
        for (int row = 0; row < 4; ++row) {
            holder = view.randomIntGen();
            for (int column = 0; column < 5; ++column) {
                if (!view.duplicateCard(cardDrawn, holder)) {
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

    public void RestartGame(ActionEvent actionEvent) {
        System.out.println("a");
    }



}
