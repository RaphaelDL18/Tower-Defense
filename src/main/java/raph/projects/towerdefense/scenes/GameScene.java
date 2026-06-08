package raph.projects.towerdefense.scenes;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import raph.projects.towerdefense.Level;
import raph.projects.towerdefense.Map;
import raph.projects.towerdefense.Phase;
import raph.projects.towerdefense.WaveHandler;
import raph.projects.towerdefense.entities.Enemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static raph.projects.towerdefense.Map.HEIGHT;
import static raph.projects.towerdefense.Map.WIDTH;

public class GameScene {

    private Scene scene;
    private Level level;
    private AnimationTimer gameLoop;
    private long lastUpdate;
    private WaveHandler waveHandler;
    private List<Enemy> enemies;
    private Pane paneMap;
    private Pane paneEnemies;

    public GameScene(Stage stage, int id) throws IOException {
        this.level = new Level(id, new Map(id));
        this.lastUpdate = 0;
        this.enemies = new ArrayList<>();
        this.waveHandler = new WaveHandler(level.getWaves(), 1.5, 3.0);

        // --- Pane map ---
        this.paneMap = new Pane();
        for (int i = 0; i < HEIGHT; i++)
        {
            for (int j = 0; j < WIDTH; j++)
            {
                this.level.getMap().getTileTemplate()[i][j].getSprite().play();
                this.paneMap.getChildren().add(this.level.getMap().getTileTemplate()[i][j].getSprite().getCurrentFrame());
            }
        }

        this.paneEnemies = new Pane();
        Pane paneTowers  = new Pane();
        Pane paneEffects = new Pane();

        StackPane gameRoot = new StackPane(paneMap, paneTowers, paneEnemies, paneEffects);
        gameRoot.setPrefSize(1920, 896);
        gameRoot.setMinSize(1920, 896);
        gameRoot.setMaxSize(1920, 896);

        HBox hud = buildHUD();

        VBox root = new VBox(gameRoot, hud);
        root.setPrefSize(1920, 1080);
        VBox.setVgrow(hud, Priority.NEVER);
        hud.setMinHeight(184);
        hud.setMaxHeight(184);
        hud.setPrefHeight(184);

        this.scene = new Scene(root, 1920, 1080, Color.BLACK);

        // --- Game loop ---
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double dt = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                update(dt);
            }
        };
    }

    private HBox buildHUD() {
        // Boutons tours
        Button tower_1 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_2 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_3 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_4 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_5 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button play    = makeButton("/raph/projects/towerdefense/Images/HUD/Play.png");
        play.setOnAction(e -> { this.startAttackPhase();});

        // Labels
        Label goldLabel  = makeLabel("💰 250");
        Label livesLabel = makeLabel("❤️ 20");
        Label waveLabel  = makeLabel("Wave 1/5");

        // Groupe tours gauche
        HBox towersBox = new HBox(10, tower_1, tower_2, tower_3, tower_4, tower_5);
        towersBox.setAlignment(Pos.CENTER_LEFT);

        // Groupe infos centre
        HBox infoBox = new HBox(40, goldLabel, livesLabel, waveLabel);
        infoBox.setAlignment(Pos.CENTER);

        // Spacers
        Region spacerLeft  = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft,  Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        // HUD principal
        HBox hud = new HBox(20, towersBox, spacerLeft, infoBox, spacerRight, play);
        hud.setAlignment(Pos.CENTER);
        hud.setPrefSize(1920, 184);
        hud.setMinSize(1920, 184);
        hud.setMaxSize(Double.MAX_VALUE, 184);
        hud.setPadding(new Insets(0, 20, 0, 20));
        hud.setStyle("-fx-background-color: black;");

        return hud;
    }

    private Button makeButton(String path)
    {
        Button b = new Button();
        Image img = new Image(getClass().getResourceAsStream(path));
        ImageView iv = new ImageView(img);
        b.setGraphic(iv);
        iv.setFitWidth(140);
        iv.setFitHeight(140);
        iv.setPreserveRatio(true);
        b.setPrefSize(140, 140);
        b.setMinSize(140, 140);
        b.setMaxSize(140, 140);
        return b;
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");
        return l;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void startAttackPhase() {
        level.setPhase(Phase.DEFENSE);
        gameLoop.start();
    }

    public void stopAttackPhase() {
        gameLoop.stop();
        level.setPhase(Phase.CONSTRUCTION);
    }

    private void update(double dt) {
        if (level.getPhase() == Phase.DEFENSE) {
            waveHandler.update(dt);
            if (waveHandler.shouldSpawn()) {
                spawnEnemy();
                waveHandler.decrementSpawn();
            }
            updateEnemies(dt);
            if (waveHandler.isWaveFinished() && enemies.isEmpty()) {
                if (waveHandler.isLevelFinished()) {
                    // TODO : victoire
                } else {
                    waveHandler.notifyWaveCleared();
                    stopAttackPhase();
                }
            }
        }
    }

    private void updateEnemies(double dt) {
        for (Enemy e : enemies) {
            e.update(dt);
        }
        enemies.removeIf(e -> {
            if (!e.isAlive() || e.hasReachedBase()) {
                paneEnemies.getChildren().remove(e.getSprite().getCurrentFrame());
                return true;
            }
            return false;
        });
    }

    private void spawnEnemy() {
        Enemy e = waveHandler.getNextEnemy();
        enemies.add(e);
        e.getSprite().play();
        paneEnemies.getChildren().add(e.getSprite().getCurrentFrame());
    }
}