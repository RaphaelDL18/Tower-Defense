package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;
import raph.projects.towerdefense.Tile;

public class Enemy extends Entity implements Damageable, DamageDealer
{
    private EnemyType type;
    private int health;
    private int speed;
    private int damage;
    private Tile[] path;
    private int pathIndex;
    private int goldReward;

    private double vx;
    private double vy;

    public Enemy(EnemyType t, Tile[] path, int level)
    {
        super(initSprite(t));
        this.type = t;
        this.path = path;
        this.pathIndex = 0;

        this.x = path[0].getGridY() * Tile.TILE_SIZE;
        this.y = path[0].getGridX() * Tile.TILE_SIZE;

        switch(this.type)
        {
            case GOBELIN -> {
                this.health = 20;
                this.speed = 3;
                this.damage = 3;
            }
            default -> {
                this.health = 1;
                this.speed = 1;
                this.damage = 1;
            }
        }

        this.goldReward = computeGoldReward(t, level);
    }

    public int getGoldReward() { return this.goldReward; }

    public EnemyType getType()
    {
        return this.type;
    }

    public int getHealth()
    {
        return this.health;
    }

    public int getSpeed()
    {
        return this.speed;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public void setHealth(int h)
    {
        this.health = h;
    }

    public void setSpeed(int s)
    {
        this.speed = s;
    }

    private static int computeGoldReward(EnemyType t, int level)
    {
        int[] goblinGold = {10, 12, 15, 18, 22};
        int[] wraithGold  = {0, 15, 18, 22, 28};
        int[] ogreGold    = {0, 0, 30, 38, 45};
        int[] ratGold     = {0, 0, 0, 5, 7};
        int[] demonGold   = {0, 0, 0, 0, 80};

        int idx = level - 1;
        return switch (t) {
            case GOBELIN -> goblinGold[idx];
            case WRAITH  -> wraithGold[idx];
            case OGRE    -> ogreGold[idx];
            case RAT     -> ratGold[idx];
            case DEMON   -> demonGold[idx];
        };
    }

    public double getVelocityX() { return this.vx; }

    public double getVelocityY() { return this.vy; }

    public static Sprite initSprite(EnemyType t)
    {
        return switch(t)
        {
            case GOBELIN -> new Sprite("/raph/projects/towerdefense/Images/Enemies/gobelin.png",8,64,64,4);
            default -> new Sprite("/raph/projects/towerdefense/Images/Blank.png",1,64,64,1);
        };
    }

    public void update(double dt)
    {
        if (this.pathIndex >= this.path.length) return;

        double targetX = this.path[this.pathIndex].getGridY() * Tile.TILE_SIZE;
        double targetY = this.path[this.pathIndex].getGridX() * Tile.TILE_SIZE;

        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double pixelsPerSecond = this.speed * 64;

        double oldX = this.x;
        double oldY = this.y;

        if (distance <= pixelsPerSecond * dt) {
            this.x = targetX;
            this.y = targetY;
            pathIndex++;
        } else {
            this.x += (dx / distance) * pixelsPerSecond * dt;
            this.y += (dy / distance) * pixelsPerSecond * dt;
        }

        this.vx = (this.x - oldX) / dt;
        this.vy = (this.y - oldY) / dt;

        this.sprite.getCurrentFrame().setX(this.x);
        this.sprite.getCurrentFrame().setY(this.y);
    }

    public boolean hasReachedBase() {
        return this.pathIndex >= this.path.length;
    }

    public int getPathIndex()
    {
        return this.pathIndex;
    }

    @Override
    public void attack(int d, Damageable target)
    {
        target.damaged(d);
    }

    @Override
    public void damaged(int d)
    {
        this.health-=d;
    }

    @Override
    public boolean isAlive()
    {
        return this.health>0;
    }

}
