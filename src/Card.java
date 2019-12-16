import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Card {
    public ImageView frontSide;
    public ImageView backSide;
    private int ID;

    Card(int cardNum){
        this.backSide = new ImageView(source_file(cardNum));
        this.backSide.setFitWidth(112);
        backSide.setPreserveRatio(true);
        this.frontSide = new ImageView("file:src/Vietnam.png");
        this.frontSide.preserveRatioProperty();
        this.frontSide.setFitWidth(112);
        frontSide.setPreserveRatio(true);
        this.ID = cardNum%10;
    }

    public int getID() {
        return ID;
    }

    public String source_file (int cardDrawn){
        StringBuilder src_template = new StringBuilder();
        src_template.append("file:u22/");
        src_template.replace(9,src_template.length(),Integer.toString(cardDrawn)).append(".jfif");
        return src_template.toString();
    }



}
