import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

public class GameController {
    @FXML
    public GridPane cardTable;
    @FXML
    public Label timer, gameScore, gameScore1, gameScore2, P1Score, P2Score, player2Win, player1Win, tie;
    @FXML
    private ProgressBar timeProgress;
    @FXML
    public HBox Score,PlayerScore;
    @FXML
    public Button Start, Start1, musicOffBtn, musicOnBtn;
    @FXML
    public VBox gameChoices, time, startOptions, winGame, lostGame;
    @FXML
    public Slider gameLevel;


    private MediaPlayer music;
    private int numOfPlayer;
    private int currentPlayer = 1;
    private GameModel model = new GameModel();
    private Timeline textTimeline = new Timeline();
    private Timeline barTimeline = new Timeline();
    private ArrayList<Node> clickNodes;
    private int[][] clickNodesCoor = new int[2][2];

    public void startGame(){
        numOfPlayer = 1;
        setMediaPlayer();
        time.setVisible(true);
        Score.setVisible(true);
        arrangeCard();
    }

    public void start2PlayerGame(){
        model.initializePlayers();
        numOfPlayer = 2;
        setMediaPlayer();
        PlayerScore.setVisible(true);
        arrangeCard();
    }

    public void RestartGame() {
        startOptions.setVisible(true);
        Score.setVisible(false);
        gameChoices.setVisible(false);
        time.setVisible(false);
        cardTable.setVisible(false);
    }

    public void arrangeCard(){
        System.out.println(numOfPlayer);;
        if(numOfPlayer == 1) {
            runTime();
        }
        setUpScene();

        while (!cardTable.getChildren().isEmpty()) {
            cardTable.getChildren().clear();
        }
        for (int row = 0; row < 4;row++) {
            for (int column = 0; column < 5;column++) {
                cardTable.add(model.getCardDeck()[row * 5 + column].backSide, column, row);
                getNodeByRowColumnIndex(column, row).setScaleX(1);
            }
        }
        cardTable.setVisible(true);
        model.setCardMatch(0);
    }

    public void  setUpScene(){
        player1Win.setVisible(false);
        player2Win.setVisible(false);
        startOptions.setVisible(false);
        winGame.setVisible(false);
        lostGame.setVisible(false);
        tie.setVisible(false);
        gameChoices.setVisible(true);
        clickNodes = new ArrayList<>();
        model.shuffleCard();
    }

    public void flipCardUp(MouseEvent e) throws InterruptedException {
        Node clikedNode = e.getPickResult().getIntersectedNode();
        if (clikedNode != cardTable && !checkClickedNode(clikedNode) && model.getNumOfCardDrawn() < 2) {
            ScaleTransition stShowBack = new ScaleTransition(Duration.millis(300), (ImageView)clikedNode);
            stShowBack.setFromX(1);
            stShowBack.setToX(0);
            stShowBack.play();
            stShowBack.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        flipCardDown(clikedNode);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void flipCardDown(Node clickedNode) throws InterruptedException {
        int[] nodeCoordinate = {GridPane.getColumnIndex(clickedNode),GridPane.getRowIndex(clickedNode)};
        clickNodesCoor[model.getNumOfCardDrawn()] = nodeCoordinate;
        Node replaceNode = replaceNode(clickedNode,model.getCardDeck()[nodeCoordinate[0] + nodeCoordinate[1]*5].backSide,nodeCoordinate);
        clickNodes.add(replaceNode);
        model.setCardDrawnID(model.getCardDeck()[nodeCoordinate[0] + nodeCoordinate[1]*5].getID());
        replaceNode.setScaleX(0);
        ScaleTransition stShowFront = new ScaleTransition(Duration.millis(300),replaceNode);
        stShowFront.setFromX(0);
        stShowFront.setToX(1);
        stShowFront.play();
        stShowFront.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    matchNode();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void matchNode() throws InterruptedException {
        if (model.getNumOfCardDrawn() == 2){
            if (model.matchCard()){
                matchedNodes();
                if (model.getCardMatch() == 20) {
                    System.out.println("a");
                    if (numOfPlayer == 1) {
                        winGame();
                    } else {
                        MultiPlayerWin();
                    }
                }
            } else {
                PauseTransition pause = new PauseTransition(Duration.seconds((int)gameLevel.getValue()));
                pause.play();
                pause.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        notMatchedNodes();
                        if (currentPlayer == 2){
                            currentPlayer = 1;
                        }else {
                            currentPlayer = 2;
                        }
                    }
                });
            }
        }

    }

    public void matchedNodes() {
        if (numOfPlayer == 2) {
            model.setPlayerScore(currentPlayer);
            if (currentPlayer == 2) {
                P2Score.setText(Integer.toString(model.getPlayerScore(currentPlayer)));
            } else {
                P1Score.setText(Integer.toString(model.getPlayerScore(currentPlayer)));
            }
        }
        for (int i = 1; i > -1; i--) {
            model.setCardMatch();
            model.setNumOfCardDrawn(0);
        }
    }

    public void notMatchedNodes(){
        for (int i = 1; i > -1; i--) {
            replaceNode(getNodeByRowColumnIndex(clickNodesCoor[i][0], clickNodesCoor[i][1]), model.getCardDeck()[clickNodesCoor[i][0] + clickNodesCoor[i][1] * 5].frontSide, clickNodesCoor[i]);
            clickNodes.remove(clickNodes.size() - 1);
            model.setNumOfCardDrawn(0);
        }
    }

    private Node replaceNode(Node nodeNeedReplace, ImageView replaceNode,int[] nodeCoordinate){
        while (!cardTable.getChildren().remove(nodeNeedReplace)) {
            cardTable.getChildren().remove(nodeNeedReplace);
        }
        cardTable.add(replaceNode,nodeCoordinate[0],nodeCoordinate[1]);
        getNodeByRowColumnIndex(nodeCoordinate[0],nodeCoordinate[1]).setScaleX(1);
        return getNodeByRowColumnIndex(nodeCoordinate[0],nodeCoordinate[1]);
    }

    public Node getNodeByRowColumnIndex (final int column, final int row) {
        Node result = null;
        ObservableList<Node> childrens = cardTable.getChildren();

        for (Node node : childrens) {
            if(cardTable.getRowIndex(node) == row && cardTable.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    public void runTime() {
        barCountDown();
        textCountDown();
    }

    public void textCountDown() {
        timer.setText(model.resetTime());
        textTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0/100), e -> countDownText(timer)));
        textTimeline.setCycleCount(textTimeline.INDEFINITE);
        textTimeline.playFromStart();
    }

    public void countDownText(Label timer) {
        if (model.getTime().equals("00 : 00 : 00")) {
                lostGame();
        } else {
            setTimerText(model.changeTimeText(), timer);
        }
    }

    public void barCountDown() {
        barTimeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(timeProgress.progressProperty(), 1)),
                new KeyFrame(Duration.minutes(2), new KeyValue(timeProgress.progressProperty(), 0))
        );
        barTimeline.setCycleCount(1);
        barTimeline.playFromStart();
    }

    private boolean checkClickedNode(Node clickedNode){
        for (int i = 0; i < clickNodes.size(); i++){
            if (clickedNode == clickNodes.get(i)){
                return true;
            }
        }
        return false;
    }

    public void setTimerText(String time, Label timer) {
        timer.setText(time);
    }

    public void endGame() {
        textTimeline.stop();
        textTimeline.getKeyFrames().clear();
        barTimeline.stop();
        barTimeline.getKeyFrames().clear();
        startOptions.setVisible(true);
        Score.setVisible(false);
        gameChoices.setVisible(false);
        time.setVisible(false);
        cardTable.setVisible(false);
    }

    public void MultiPlayerWin(){
        if (model.getPlayerScore(1) > model.getPlayerScore(2)){
            player1Win.setVisible(true);
        } else if (model.getPlayerScore(1) < model.getPlayerScore(2)){
            player2Win.setVisible(true);
        } else {
            tie.setVisible(true);
        }
        startOptions.setVisible(true);
        Score.setVisible(false);
        gameChoices.setVisible(false);
        time.setVisible(false);
        cardTable.setVisible(false);
    }

    public void winGame() {
        endGame();
        gameScore1.setText(model.getNewScore());
        gameScore.setText(gameScore1.getText());
        model.resetTime();
    }

    public void lostGame(){
        endGame();
        lostGame.setVisible(true);
        gameScore2.setText(model.getNewScore());
        gameScore.setText(gameScore2.getText());
        model.resetTime();
    }

    public void soundOff(ActionEvent event) {
        musicOffBtn.setVisible(false);
        musicOnBtn.setVisible(true);
        music.setMute(true);
    }

    public void soundOn(ActionEvent event) {
        musicOnBtn.setVisible(false);
        musicOffBtn.setVisible(true);
        music.setMute(false);
    }

    private void setMediaPlayer() {
        String musicFile = "src/backgroundMusic.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        music = new MediaPlayer(sound);
        music.play();
    }
}
