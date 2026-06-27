package raph.projects.towerdefense.entities;

public enum TowerType
{
    CANNON(0, 100, 150),
    ARCANE(1, 125, 175),
    BOMB(2, 150, 200),
    FROST(3, 100, 125),
    BALLISTA(4, 175, 225);

    private int type;
    private int cost;
    private int upgradeCost;

    TowerType(int t, int cost, int upgradeCost)
    {
        this.type = t;
        this.cost = cost;
        this.upgradeCost = upgradeCost;
    }

    public int getType()
    {
        return this.type;
    }

    public int getCost()
    {
        return this.cost;
    }

    public int getUpgradeCost()
    {
        return this.upgradeCost;
    }
}