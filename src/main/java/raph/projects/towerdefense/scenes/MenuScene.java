package raph.projects.towerdefense.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import raph.projects.towerdefense.Map;
import raph.projects.towerdefense.app.MainApp;

import java.io.IOException;
import java.util.Objects;

public class MenuScene
{

    private Scene scene;

    public MenuScene(Stage stage) throws IOException {
        Map level_1 = new Map(1);

        Region spacerTop = new Region();
        Region spacerMiddle = new Region();

        spacerTop.setPrefHeight(100);
        spacerTop.setMaxHeight(100);

        spacerMiddle.setPrefHeight(100);
        spacerMiddle.setMaxHeight(100);

        Button play = new Button("Play");
        play.setPrefSize(400, 80);
        play.setMinSize(400, 80);
        play.setMaxSize(400, 80);

        Button quit = new Button("Quit");
        quit.setPrefSize(400, 80);
        quit.setMinSize(400, 80);
        quit.setMaxSize(400, 80);

        ImageView mainTitle = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/raph/projects/towerdefense/Images/MainTitle.png"))));
        mainTitle.setFitWidth(1200);
        mainTitle.setFitHeight(300);
        mainTitle.setPreserveRatio(true);

        play.setOnAction(e -> {
                    try {
                        GameScene gameScene = new GameScene(stage, 1);
                        stage.setScene(gameScene.getScene());

                        stage.setScene(gameScene.getScene());
                        gameScene.startAttackPhase();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    MainApp.applyScale(stage);
                                          }
                        );
        quit.setOnAction(e -> stage.close());

        VBox layout = new VBox(spacerTop, mainTitle, spacerMiddle, play, quit);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(40);
        //layout.setPrefSize(1920, 1080);

        this.scene = new Scene(layout, 1920, 1080, Color.BLACK);
    }

    public Scene getScene() { return scene; }
}

