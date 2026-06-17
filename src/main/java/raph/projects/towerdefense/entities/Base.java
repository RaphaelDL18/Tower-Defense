package raph.projects.towerdefense.entities;

public class Base implements Damageable
{
    private int health;

    public Base()
    {
        this.health=20;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void damaged(int d)
    {
        this.health-=d;
    }

    @Override
    public boolean isAlive()
    {
        return this.health > 0;
    }
}
