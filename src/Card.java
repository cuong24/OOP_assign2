import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.text.*;
import java.util.*;

public class Card {
    @FXML
    private ImageView frontSide;
    @FXML
    public ImageView backSide;
    private int ID;

    Card(int cardNum){
        backSide = new ImageView(source_file(cardNum));
        backSide.preserveRatioProperty();
        backSide.setFitWidth(112);
        frontSide = new ImageView("file:u22/Vietnam.png");
        frontSide.preserveRatioProperty();
        frontSide.setFitWidth(112);
        ID = cardNum%10;
    }

    public String source_file (int cardDrawn){
        StringBuilder src_template = new StringBuilder();
        src_template.append("file:u22/");
        src_template.replace(9,src_template.length(),Integer.toString(cardDrawn)).append(".jfif");
        return src_template.toString();
    }

}
