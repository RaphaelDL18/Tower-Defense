package raph.projects.towerdefense.entities;

public class Base implements Damageable
{
    private int health;

    public Base(int h)
    {
        this.health=h;
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
