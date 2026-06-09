package raph.projects.towerdefense.entities;

public enum TowerType
{
    CANNON(0),
    ARCANE(1),
    BOMB(2),
    FROST(3),
    BALLISTA(4);

    private int type;

    TowerType(int t)
    {
        this.type=t;
    }

    public int getType()
    {
        return this.type;
    }
}
