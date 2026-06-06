module raph.projects.towerdefense {
    requires javafx.controls;
    requires javafx.fxml;


    opens raph.projects.towerdefense to javafx.fxml;
    exports raph.projects.towerdefense;
    exports raph.projects.towerdefense.app;
    opens raph.projects.towerdefense.app to javafx.fxml;
    exports raph.projects.towerdefense.entities;
    opens raph.projects.towerdefense.entities to javafx.fxml;
    exports raph.projects.towerdefense.scenes;
    opens raph.projects.towerdefense.scenes to javafx.fxml;
}