#Architecture
**_This part is dedicated to explain how the game's files are organised and how they work together._**  
##Summary
1. **Game files architecture**  
  a. Tree structure  
  b. Relations
2. **Files**  
##1 - Game files architecture 
###a - Tree structure
```
towerdefense/  
├── src/  
|   └── main/  
|       ├── java/
|       |   ├── raph/
|       |   |   └── projects/
|       |   |       └── towerdefense/
|       |   |           └── Base.java
|       |   |           └── Damageable.java
|       |   |           └── DamageDealer.java
|       |   |           └── Enemy.java
|       |   |           └── Entity.java
|       |   |           └── GameOverScene.java
|       |   |           └── GameScene.java
|       |   |           └── MainApp.java
|       |   |           └── Map.java
|       |   |           └── MenuScene.java
|       |   |           └── PauseScene.java
|       |   |           └── Sprite.java
|       |   |           └── Tile.java
|       |   |           └── TileType.java
|       |   |           └── Tower.java
|       |   └── module-info.java
|       └── resources/
|           └── raph/
|               └── projects/
|                   └── towerdefense/
|                       ├── Images/
|                       |   ├── Levels_Map/
|                       |   |   └── ...
|                       |   ├── Level-1
|                       |   |   └── ...
|                       |   ├── Level-2
|                       |   |   └── ...
|                       |   ├── Level-3
|                       |   |   └── ...
|                       |   ├── Level-4
|                       |   |   └── ...
|                       |   ├── Level-5
|                       |   |   └── ...
|                       |   ├── Blank.png
|                       |   └── MainTitle.png
|                       └── Maps/
|                           └── ...
├── target/  
|   └── ...
├── Documentation/
|   └── ...
├── pom.xml
└── towerdefense.iml
```
###b - Relations
        
        
