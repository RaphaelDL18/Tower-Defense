package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Map;
import raph.projects.towerdefense.Sprite;
import raph.projects.towerdefense.Tile;

public abstract class Entity
{
    protected double x;
    protected double y;

    protected int gridX;
    protected int gridY;

    protected Sprite sprite;

    public Entity(Sprite s)
    {
        this.x = 0.0;
        this.y = 0.0;
        this.gridX = 0;
        this.gridY = 0;
        this.sprite = s;
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }

    public void move(double newX, double newY)
    {
        this.x = newX;
        this.y = newY;
        this.updateTilePosition();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateTilePosition()
    {
        for(int i = 0; i < Map.HEIGHT ; i++)
        {
            for(int j = 0; j < Map.WIDTH; j++)
            {
                if((this.x >= i * Tile.TILE_SIZE && this.x < i * Tile.TILE_SIZE + Tile.TILE_SIZE) && (this.y >= j * Tile.TILE_SIZE && this.y < j * Tile.TILE_SIZE + Tile.TILE_SIZE))
                {
                    this.gridX = j;
                    this.gridY = i;
                }
            }
        }
    }


}
