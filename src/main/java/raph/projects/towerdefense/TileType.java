package raph.projects.towerdefense;

public enum TileType
{
    ROAD_HORIZONTAL(0),
    ROAD_VERTICAL(1),
    ROAD_TOP_LEFT(2),
    ROAD_TOP_RIGHT(3),
    ROAD_BOTTOM_RIGHT(4),
    ROAD_BOTTOM_LEFT(5),
    TOWER_SLOT(6),
    OBSTACLE(7),
    BASE(8),

    LAKE_TOP_LEFT(10),
    LAKE_TOP(11),
    LAKE_TOP_RIGHT(12),
    LAKE_RIGHT(13),
    LAKE_BOTTOM_RIGHT(14),
    LAKE_BOTTOM(15),
    LAKE_BOTTOM_LEFT(16),
    LAKE_LEFT(17),
    LAKE(18),

    BORDER_TOP_LEFT(20),
    BORDER_TOP(21),
    BORDER_TOP_RIGHT(22),
    BORDER_RIGHT(23),
    BORDER_BOTTOM_RIGHT(24),
    BORDER_BOTTOM(25),
    BORDER_BOTTOM_LEFT(26),
    BORDER_LEFT(27),
    BORDER_ROAD(28);



    private final int type;

    TileType(int t)
    {
        this.type=t;
    }

    public int getType()
    {
        return this.type;
    }

}
