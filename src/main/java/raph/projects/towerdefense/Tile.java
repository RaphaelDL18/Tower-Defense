package raph.projects.towerdefense;

public class Tile
{
    public static int TILE_SIZE = 64;

    private int gridX;
    private int gridY;
    private TileType type;
    private Sprite sprite;

    public Tile(int x, int y, TileType t,String path)
    {
        this.gridX = x;
        this.gridY = y;
        this.type = t;
        this.sprite = new Sprite(path,1,TILE_SIZE,TILE_SIZE,1);
        this.sprite.getCurrentFrame().setX(y * TILE_SIZE);
        this.sprite.getCurrentFrame().setY(x * TILE_SIZE);
    }

    public int getGridX()
    {
        return this.gridX;
    }

    public int getGridY()
    {
        return this.gridY;
    }

    public TileType getType()
    {
        return this.type;
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }
}

