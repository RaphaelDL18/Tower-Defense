package raph.projects.towerdefense.scenes;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import raph.projects.towerdefense.*;
import raph.projects.towerdefense.entities.Enemy;
import raph.projects.towerdefense.entities.TowerType;

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
    private TowerType selectedTower;
    private ImageView ghostTower;
    private Pane paneMap;
    private Pane paneEnemies;
    private Pane paneEffects;

    public GameScene(Stage stage, int id) throws IOException
    {
        this.level = new Level(id, new Map(id));
        this.lastUpdate = 0;
        this.enemies = new ArrayList<>();
        this.waveHandler = new WaveHandler(level.getWaves(), 1.5, 3.0);
        this.selectedTower = null;

        this.paneEffects = new Pane();
        this.paneEffects.setMouseTransparent(true);

        this.ghostTower = new ImageView();
        this.ghostTower.setFitWidth(64);
        this.ghostTower.setFitHeight(64);
        this.ghostTower.setOpacity(0.5);
        ghostTower.setVisible(false);      // caché par défaut
        paneEffects.getChildren().add(ghostTower);

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

        StackPane gameRoot = new StackPane(paneMap, paneTowers, paneEnemies, paneEffects);
        gameRoot.setPrefSize(1920, 896);
        gameRoot.setMinSize(1920, 896);
        gameRoot.setMaxSize(1920, 896);

        gameRoot.setOnMouseMoved(event -> {
            if (selectedTower == null) return;

            int col = (int) (event.getX() / Tile.TILE_SIZE);
            int row = (int) (event.getY() / Tile.TILE_SIZE);

            ghostTower.setX(col * Tile.TILE_SIZE);
            ghostTower.setY(row * Tile.TILE_SIZE);

            Circle range = buildRangeCircle(3);
            range.setCenterX(col * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
            range.setCenterY(row * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
            paneEffects.getChildren().add(range);

        });

        gameRoot.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                cancelPlacement();
            }
        });

        HBox hud = buildHUD();
        VBox.setVgrow(hud, Priority.NEVER);
        hud.setMinHeight(184);
        hud.setMaxHeight(184);
        hud.setPrefHeight(184);

        VBox root = new VBox(gameRoot, hud);
        root.setPrefSize(1920, 1080);

        this.scene = new Scene(root, 1920, 1080, Color.BLACK);

        // --- Game loop ---
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (lastUpdate == 0)
                {
                    lastUpdate = now;
                    return;
                }
                double dt = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                update(dt);
            }
        };
    }

    private HBox buildHUD()
    {
        Button tower_1 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        tower_1.setOnAction(e ->selectTower(TowerType.CANNON));

        Button tower_2 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_3 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_4 = makeButton("/raph/projects/towerdefense/Images/Blank.png");
        Button tower_5 = makeButton("/raph/projects/towerdefense/Images/Blank.png");

        Button play = makeButton("/raph/projects/towerdefense/Images/HUD/Play.png");
        play.setOnAction(e -> { this.startAttackPhase();});

        Label goldLabel  = makeLabel("💰 250");
        Label livesLabel = makeLabel("❤️ 20");
        Label waveLabel  = makeLabel("Wave 1/5");

        HBox towersBox = new HBox(10, tower_1, tower_2, tower_3, tower_4, tower_5);
        towersBox.setAlignment(Pos.CENTER_LEFT);

        HBox infoBox = new HBox(40, goldLabel, livesLabel, waveLabel);
        infoBox.setAlignment(Pos.CENTER);

        Region spacerLeft  = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft,  Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

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

        // TODO: Désactiver les boutons lorsque les tours ne peuvent pas etre placées

        return b;
    }

    private Label makeLabel(String text)
    {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");
        return l;
    }

    public Scene getScene()
    {
        return this.scene;
    }

    public void startAttackPhase()
    {
        level.setPhase(Phase.DEFENSE);
        gameLoop.start();
    }

    public void stopAttackPhase()
    {
        gameLoop.stop();
        level.setPhase(Phase.CONSTRUCTION);
   }

    private void update(double dt)
    {
        if (level.getPhase() == Phase.DEFENSE)
        {
            waveHandler.update(dt);
            if (waveHandler.shouldSpawn())
            {
                spawnEnemy();
                waveHandler.decrementSpawn();
            }
            updateEnemies(dt);
            if (waveHandler.isWaveFinished() && enemies.isEmpty())
            {
                if (waveHandler.isLevelFinished())
                {
                    // TODO : victoire

                }
                else
                {
                    waveHandler.notifyWaveCleared();
                    stopAttackPhase();
                }
            }
        }
        else
        {
            if(this.selectedTower != null)
            {

            }
        }
    }

    private void updateEnemies(double dt)
    {
        for (Enemy e : enemies)
        {
            e.update(dt);
        }
        enemies.removeIf(e ->
        {
            if (!e.isAlive() || e.hasReachedBase())
            {
                paneEnemies.getChildren().remove(e.getSprite().getCurrentFrame());
                return true;
            }
            return false;
        });
    }

    private void spawnEnemy()
    {
        Enemy e = waveHandler.getNextEnemy();
        enemies.add(e);
        e.getSprite().play();
        paneEnemies.getChildren().add(e.getSprite().getCurrentFrame());
    }

    private void selectTower(TowerType t)
    {
        String path;
        this.selectedTower = t;
        switch(this.selectedTower)
        {
            case CANNON -> path ="/raph/projects/towerdefense/Images/Towers/Icons/Cannon_Icon.png";
            default -> path = "/raph/projects/towerdefense/Images/Blank.png";
        }
        Image img = new Image(getClass().getResourceAsStream(path));
        ghostTower.setImage(img);
        ghostTower.setVisible(true);
    }

    private void cancelPlacement()
    {
        selectedTower = null;
        ghostTower.setVisible(false);
    }

    private Circle buildRangeCircle(int range) {
        Circle circle = new Circle();
        circle.setRadius(range * Tile.TILE_SIZE);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);
        circle.setMouseTransparent(true);
        return circle;
    }
}