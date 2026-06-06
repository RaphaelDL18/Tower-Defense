package raph.projects.towerdefense.app;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import raph.projects.towerdefense.scenes.MenuScene;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException
    {
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("");
        stage.setScene(new MenuScene(stage).getScene());

        stage.fullScreenProperty().addListener((obs, wasFullScreen, isNowFullScreen) ->
        {
            if (isNowFullScreen) applyScale(stage);
        });

        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null)
            {
                javafx.application.Platform.runLater(() -> {
                                                                stage.setFullScreen(true);
                                                                applyScale(stage);
                                                            });
            }
        });

        stage.show();

        // Délai pour laisser la fenêtre s'initialiser
        javafx.application.Platform.runLater(() -> {
            stage.setFullScreen(true);
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void applyScale(Stage stage)
    {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double scale = Math.min(bounds.getWidth() / 1920.0, bounds.getHeight() / 1200.0);

        stage.getScene().getRoot().setScaleX(scale);
        stage.getScene().getRoot().setScaleY(scale);
    }
}