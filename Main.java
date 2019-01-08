import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    private static double g = 0.05;
    private double angle;
    private double ax;
    private double ay;
    private double F = 500;
    private double score = 0;
    private double[] angles = new double[10];
    private double v;
    private boolean paused = false;
    private String userName = "";
    private Font space = Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 24);
    private int level = 1;
    private Image landerOFF = new Image("g.png",42,47,false,false);
    private Image landerON = new Image("g2.png",42,47,false,false);
    private Image clip = new Image("clip.png",210,235,false,false);
    //private Media engine = new Media(Main.class.getResource("engine.WAV").toExternalForm());
    //private MediaPlayer mediaPlayer = new MediaPlayer(engine);
    private Lander lander;
    private DecimalFormat dc = new DecimalFormat("###.#");
    private Timeline timeline = new Timeline();
    private boolean freeFall = true;
    private boolean isLeftRotate = false;
    private boolean isRightRotate = false;



    private EventHandler<KeyEvent> input = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE && !paused) {
                freeFall = false;
                if (F > 0) {
                    lander.lander.setFill(new ImagePattern(landerON));
                    //mediaPlayer.play();
                }
            } else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                isLeftRotate = true;
            } else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                isRightRotate = true;
            } else if (event.getCode() == KeyCode.ESCAPE){
                if (paused){
                    paused = false;
                    timeline.play();
                } else {
                    paused = true;
                    timeline.pause();
                }
            }
        }
    };
    private EventHandler<KeyEvent> inputReleased = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                freeFall = true;
                lander.lander.setFill(new ImagePattern(landerOFF));
                //mediaPlayer.stop();
            } else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                isLeftRotate = false;
            } else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                isRightRotate = false;
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        Scene scene = new Scene(group, 1000, 700);
        scene.setFill(new ImagePattern(new Image("background.jpg",1000,700,false,false,false)));
        primaryStage.setScene(scene);



        Label title = new Label("LUNAR LANDER");
        title.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutY(30);
        Text titleText = new Text(title.getText());
        titleText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutX(500-titleText.getBoundsInLocal().getWidth()/2);
        title.setTextFill(Color.WHITE);
        group.getChildren().add(title);
        
        Label play = new Label("PLAY GAME");
        play.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        Text playText = new Text(play.getText());
        playText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        play.setLayoutX(500-playText.getBoundsInLocal().getWidth()/2);
        play.setLayoutY(525);
        play.setTextFill(Color.WHITE);
        group.getChildren().add(play);

        play.setOnMouseEntered(event -> play.setTextFill(Color.GREEN));

        play.setOnMouseExited(event -> play.setTextFill(Color.WHITE));

        play.setOnMouseClicked(event -> game(primaryStage));

        Label scores = new Label("SCORES");
        scores.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        Text scoresText = new Text(scores.getText());
        scoresText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        scores.setLayoutX(800-scoresText.getBoundsInLocal().getWidth()/2);
        scores.setLayoutY(300);
        scores.setTextFill(Color.WHITE);
        group.getChildren().add(scores);

        scores.setOnMouseEntered(event -> scores.setTextFill(Color.GREEN));
        scores.setOnMouseExited(event -> scores.setTextFill(Color.WHITE));
        scores.setOnMouseClicked(event -> Scores(primaryStage));

        Label rules = new Label("RULES");
        rules.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        Text rulesText = new Text(rules.getText());
        rulesText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        rules.setLayoutX(200-rulesText.getBoundsInLocal().getWidth()/2);
        rules.setLayoutY(300);
        rules.setTextFill(Color.WHITE);
        group.getChildren().add(rules);

        rules.setOnMouseEntered(event -> rules.setTextFill(Color.GREEN));
        rules.setOnMouseExited(event -> rules.setTextFill(Color.WHITE));
        rules.setOnMouseClicked(event -> Rules(primaryStage));

        Rectangle clipRect = new Rectangle(210,235);
        clipRect.setFill(new ImagePattern(clip));
        clipRect.setX(500 - clipRect.getWidth()/2);
        clipRect.setY(165);
        clipRect.setCache(true);
        clipRect.setCacheHint(CacheHint.SPEED);
        group.getChildren().add(clipRect);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), clipRect);
        translateTransition.setToY(50);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(Animation.INDEFINITE);
        translateTransition.play();

        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void game(Stage primaryStage){
        Group group = new Group();
        angle = 0;
        F = 400;
        freeFall = true;

        lander = new Lander();
        Scene scene = new Scene(group, 1000, 700);
        scene.setFill(new ImagePattern(new Image("background.jpg",1000,700,false,false,false)));

        TranslateTransition vertical = new TranslateTransition(Duration.millis(32), lander.lander);
        vertical.setInterpolator(Interpolator.LINEAR);

        RotateTransition leftRotate = new RotateTransition(Duration.millis(32), lander.lander);
        leftRotate.setAxis(Rotate.Z_AXIS);
        leftRotate.setByAngle(-2);
        leftRotate.setAutoReverse(false);
        leftRotate.stop();

        RotateTransition rightRotate = new RotateTransition(Duration.millis(32), lander.lander);
        rightRotate.setAxis(Rotate.Z_AXIS);
        rightRotate.setByAngle(2);
        rightRotate.setAutoReverse(false);
        rightRotate.stop();

        lander.setVy(0);
        lander.lander.setFill(new ImagePattern(landerOFF));
        group.getChildren().add(lander.lander);

        Polyline ground = new Polyline();
        ground.setStroke(Color.LIGHTGRAY);
        ground.setStrokeWidth(200);
        genTerrain(ground);
        group.getChildren().add(ground);

        lander.lander.setCache(true);
        lander.lander.setCacheHint(CacheHint.SPEED);

        Label fuel = new Label();
        fuel.setFont(space);
        fuel.setLayoutX(10);
        fuel.setLayoutY(10);
        fuel.setTextFill(Color.WHITE);
        group.getChildren().add(fuel);

        Label speed = new Label();
        speed.setFont(space);
        speed.setLayoutX(10);
        speed.setLayoutY(40);
        speed.setTextFill(Color.WHITE);
        group.getChildren().add(speed);

        Label lev = new Label("LEVEL: " + Integer.toString(level));
        lev.setFont(space);
        lev.setLayoutX(10);
        lev.setLayoutY(70);
        lev.setTextFill(Color.WHITE);
        group.getChildren().add(lev);

        Label Score = new Label("SCORE " + Integer.toString((int)score));
        Score.setFont(space);
        Score.setLayoutY(100);
        Score.setLayoutX(10);
        Score.setTextFill(Color.WHITE);
        group.getChildren().add(Score);

        KeyFrame keyframe = new KeyFrame(Duration.millis(32), event -> {
            v = Math.sqrt(lander.getVy()*lander.getVy() + lander.getVx()*lander.getVx());
            fuel.setText("FUEL: " + Double.toString(F));
            if (v > 1.5){
                speed.setTextFill(Color.RED);
            } else {
                speed.setTextFill(Color.WHITE);
            }
            speed.setText("SPEED (m/s): " + dc.format(v));
            if (((Path)Shape.intersect(lander.lander, ground)).getElements().size() > 0 || !lander.lander.getBoundsInParent().intersects(0,0,1000,700)){
                timeline.stop();
                endGame(primaryStage);
            }

            if (F == 0){
                lander.lander.setFill(new ImagePattern(landerOFF));
                //mediaPlayer.stop();
            }

            if (!freeFall && F > 0) {
                ax = Math.sin(angle * (Math.PI / 180)) * 0.1;
                ay = Math.cos(angle * (Math.PI / 180)) * 0.1;
                lander.setVy(lander.getVy() + g - ay);
                lander.setVx(lander.getVx() + ax);
                F -= 1;
            } else {
                lander.setVy(lander.getVy() + g);
            }


            vertical.setByY(lander.getVy());
            vertical.setByX(lander.getVx());
            vertical.stop();
            vertical.play();


            if (isLeftRotate) {
                leftRotate.play();
                lander.lander.rotateProperty().addListener((observable, oldValue, newValue) -> angle = (double) newValue);
            }


            if (isRightRotate) {
                rightRotate.play();
                lander.lander.rotateProperty().addListener((observable, oldValue, newValue) -> angle = (double) newValue);
            }

            scene.setOnKeyPressed(input);
            scene.setOnKeyReleased(inputReleased);
        });
        timeline.getKeyFrames().add(keyframe);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void Rules(Stage primaryStage){
        Group group = new Group();
        Scene scene = new Scene(group, 1000,700);
        scene.setFill(new ImagePattern(new Image("background.jpg",1000,700,false,false)));

        Label title = new Label("RULES");
        title.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutY(30);
        Text titleText = new Text(title.getText());
        titleText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutX(500-titleText.getBoundsInLocal().getWidth()/2);
        title.setTextFill(Color.WHITE);
        group.getChildren().add(title);

        Label text = new Label();
        text.setFont(space);
        text.setTextFill(Color.WHITE);
        text.setText("THE OBJECT OF THE GAME IS TO LAND THE LUNAR LANDER ON THE\n" +
                "SURFACE OF THE MOON, USING AS LITTLE FUEL AS POSSIBLE. YOU MUST\n" +
                " MATCH YOUR SHIP'S ANGLE WITH THE ANGLE OF THE SURFACE AND YOUR\n" +
                "SPEED MUST BE UNDER 1.5m/s.\n" +
                "\n" +
                "EVERY TIME YOU LAND SUCCESSFULLY, YOU WILL ADVANCE TO THE\n" +
                "NEXT LEVEL, WHERE THE TERRAIN WILL BECOME \n" +
                "INCREASINGLY TREACHEROUS.\n" +
                "\n" +
                "YOUR SCORE IS CALCULATED BASED ON YOUR LEVEL AND YOUR\n " +
                "REMAINING FUEL.\n" +
                "\n" +
                "CONTROLS:\n" +
                "SPACE: THRUST\n" +
                "A OR LEFT ARROW: ROTATE LEFT\n" +
                "D OR RIGHT ARROW: ROTATE RIGHT");
        text.setTextAlignment(TextAlignment.CENTER);
        Text text1 = new Text(text.getText());
        text1.setFont(space);
        text.setLayoutX(500-text1.getBoundsInLocal().getWidth()/2);
        text.setLayoutY(120);
        group.getChildren().add(text);
        
        Label back = new Label("BACK");
        back.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        back.setLayoutY(620);
        Text backText = new Text(back.getText());
        backText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        back.setLayoutX(500-backText.getBoundsInLocal().getWidth()/2);
        back.setTextFill(Color.WHITE);
        group.getChildren().add(back);

        back.setOnMouseEntered(event -> back.setTextFill(Color.GREEN));
        back.setOnMouseExited(event -> back.setTextFill(Color.WHITE));
        back.setOnMouseClicked(event -> start(primaryStage));
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void Scores(Stage primaryStage){
        Group group = new Group();
        Scene scene = new Scene(group, 1000,700);
        scene.setFill(new ImagePattern(new Image("background.jpg",1000,700,false,false)));

        Label title = new Label("SCORES");
        title.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutY(30);
        Text titleText = new Text(title.getText());
        titleText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 72));
        title.setLayoutX(500-titleText.getBoundsInLocal().getWidth()/2);
        title.setTextFill(Color.WHITE);
        group.getChildren().add(title);

        Label back = new Label("BACK");
        back.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        back.setLayoutY(620);
        Text backText = new Text(back.getText());
        backText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 60));
        back.setLayoutX(500-backText.getBoundsInLocal().getWidth()/2);
        back.setTextFill(Color.WHITE);
        group.getChildren().add(back);

        back.setOnMouseEntered(event -> back.setTextFill(Color.GREEN));
        back.setOnMouseExited(event -> back.setTextFill(Color.WHITE));
        back.setOnMouseClicked(event -> start(primaryStage));

        List<String[]> scores = new ArrayList<>();

        try {
            FileReader input = new FileReader("Scores.txt");
            BufferedReader bufferedReader = new BufferedReader(input);
            String myLine;

            String[] current;

            while((myLine = bufferedReader.readLine()) != null){
                current = myLine.split(" ");
                scores.add(current);
            }


            for (int i = 0 ; i < scores.size() ; i++) {
                int maxIndex = 0;
                int t = -1;
                for (int I = i ; I < scores.size() ; I ++) {
                    if (Integer.parseInt(scores.get(I)[1]) > t) {
                        t = Integer.parseInt(scores.get(I)[1]);
                        maxIndex = scores.indexOf(scores.get(I));
                    }
                }
                current = scores.get(i);
                scores.set(i, scores.get(maxIndex));
                scores.set(maxIndex, current);
            }

            for (String[] temp : scores){
                System.out.println(temp[0] + " " + temp[1]);
            }


        } catch (IOException e) {
            System.out.println("File missing");
        }

        int Counter = 0;
        
        StringBuilder scoreNames = new StringBuilder();
        StringBuilder scoreNumbers = new StringBuilder();
        for (String[] temp : scores){
            if (Counter == 8)
                break;
            scoreNames.append(temp[0]).append("\n");
            scoreNumbers.append(temp[1]).append("\n");
            Counter += 1;
        }

        Label scoreText = new Label();
        scoreText.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 48));
        scoreText.setText(scoreNames.toString());
        scoreText.setTextFill(Color.WHITE);
        scoreText.setLayoutY(120);
        scoreText.setLayoutX(100);
        group.getChildren().add(scoreText);

        Label scoreTines = new Label();
        scoreTines.setFont(Font.loadFont(Main.class.getResource("RadioSpace.ttf").toExternalForm(), 48));
        scoreTines.setText(scoreNumbers.toString());
        scoreTines.setTextFill(Color.WHITE);
        scoreTines.setLayoutY(120);
        scoreTines.setLayoutX(760);
        group.getChildren().add(scoreTines);


        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private void genTerrain(Polyline ground){
        int varies = level*10;
        double x,y, xo = 0, yo = 700;
        ground.getPoints().addAll(0.,700.);
        for (int i = 1 ; i < 10 ; i++){
            x = 100*i;
            y = 700 + (Math.random() * (varies + varies)) -varies;
            ground.getPoints().addAll(x,y );
            angles[i-1] = Math.toDegrees(Math.atan2(y-yo,x-xo));
            xo = x;
            yo = y;
        }
        ground.getPoints().addAll(1000.,700.);
        angles[9] = Math.toDegrees(Math.atan2(700-yo,1000-xo));
    }

    private void endGame(Stage primaryStage){
        lander.lander.setFill(new ImagePattern(landerOFF));
        if (v < 1.5 && Math.abs(angle-angles[(int)lander.lander.getBoundsInParent().getMinX()/100]) < 20 && lander.lander.getBoundsInParent().intersects(0,0,1000,700)){
            System.out.println("you win");
            score += level*100 + F;
            level += 1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game(primaryStage);
        } else {
            System.out.println("you lose");
            level = 1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Stage secondaryStage = new Stage();
            GridPane grid = new GridPane();
            Scene scene = new Scene(grid);
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(5);
            grid.setHgap(5);

            final TextField name = new TextField();
            name.setPromptText("Enter your name.");
            name.setPrefColumnCount(15);
            name.getText();
            GridPane.setConstraints(name, 0, 0);

            final Button submit = new Button("Submit");
            GridPane.setConstraints(submit,1,0);
            grid.getChildren().add(submit);
            grid.getChildren().add(name);

            submit.setOnAction(event -> {
                userName = name.getText();
                if (!userName.equals("")) {
                    secondaryStage.close();
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter("Scores.txt", true));
                        out.write(userName + " " + (int) score);
                        out.newLine();
                        out.close();
                        score = 0;
                    } catch (IOException e) {
                        System.out.println("File missing");
                    }
                }
            });

            start(primaryStage);
            secondaryStage.setScene(scene);
            secondaryStage.setResizable(false);
            secondaryStage.sizeToScene();
            secondaryStage.show();
        }
    }
}
