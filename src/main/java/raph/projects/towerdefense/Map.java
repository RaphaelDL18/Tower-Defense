package raph.projects.towerdefense;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Map
{
    public static final int WIDTH = 30;
    public static final int HEIGHT = 14;

    public static final int PATH_SIZE_1 = 40;

    private Tile[][] tileTemplate;
    private Tile[] enemyPath;
    private final int id;

    public Map(int id) throws IOException
    {
        this.id = id;
        this.tileTemplate = new Tile[HEIGHT][WIDTH];
        switch(this.id)
        {
            case 1 -> this.enemyPath = new Tile[PATH_SIZE_1];
            default -> this.enemyPath = new Tile[1];
        }

        int[][] template = getTemplate();
        initTileTemplate(template);

        buildEnemyPath();

    }

    public Tile[][] getTileTemplate() {
        return tileTemplate;
    }

    public Tile[] getEnemyPath()
    {
        return this.enemyPath;
    }

    public int[][] getTemplate() throws IOException
    {
        int[][] template;
        template = new int[HEIGHT][WIDTH];

        String path = "";

        switch(this.id)
        {
            case 1 -> path = "/raph/projects/towerdefense/Maps/Level-1.txt";
            case 2 -> path = "/raph/projects/towerdefense/Maps/Level-2.txt";
            case 3 -> path = "/raph/projects/towerdefense/Maps/Level-3.txt";
            case 4 -> path = "/raph/projects/towerdefense/Maps/Level-4.txt";
            case 5 -> path = "/raph/projects/towerdefense/Maps/Level-5.txt";

            default -> System.out.println("Wrong map identifier");
        }
        InputStream is = getClass().getResourceAsStream(path);

        if (is != null)
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            int i = 0;
            while ((line = r.readLine()) != null)
            {
                template[i] = Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();
                i++;
            }
            r.close();
        }
        else
        {
            System.out.println("File not found");
        }
        return template;
    }

    public void initTileTemplate(int[][] template)
    {
        TileType t;
        String path ="";
        Random rand = new Random();
        int random;

        for(int k = 0; k < HEIGHT; k++)
        {
            for(int j = 0; j < WIDTH; j++)
            {
                path = "/raph/projects/towerdefense/Images/Blank.png";
                switch(this.id)
                {
                    default ->
                    {
                        switch (template[k][j])
                        {
                            case 0 ->
                                    {
                                        t = TileType.ROAD_HORIZONTAL;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Horizontal.png";
                                    }
                            case 1 ->
                                    {
                                        t = TileType.ROAD_VERTICAL;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Vertical.png";
                                    }
                            case 2 ->
                                    {
                                        t = TileType.ROAD_TOP_LEFT;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Top_Left.png";
                                    }
                            case 3 ->
                                    {
                                        t = TileType.ROAD_TOP_RIGHT;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Top_Right.png";
                                    }
                            case 4 ->
                                    {
                                        t = TileType.ROAD_BOTTOM_RIGHT;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Bottom_Right.png";
                                    }
                            case 5 ->
                                    {
                                        t = TileType.ROAD_BOTTOM_LEFT;
                                        path = "/raph/projects/towerdefense/Images/Level-1/Road_Bottom_Left.png";
                                    }
                            case 7 ->
                                    {
                                        t = TileType.OBSTACLE;

                                    }
                            case 8 ->
                                    {
                                        t = TileType.BASE;

                                    }

                            case 10 -> t = TileType.LAKE_TOP_LEFT;
                            case 11 -> t = TileType.LAKE_TOP;
                            case 12 -> t = TileType.LAKE_TOP_RIGHT;
                            case 13 -> t = TileType.LAKE_RIGHT;
                            case 14 -> t = TileType.LAKE_BOTTOM_RIGHT;
                            case 15 -> t = TileType.LAKE_BOTTOM;
                            case 16 -> t = TileType.LAKE_BOTTOM_LEFT;
                            case 17 -> t = TileType.LAKE_LEFT;
                            case 18 -> t = TileType.LAKE;

                            case 20 ->
                            {
                                t = TileType.BORDER_TOP_LEFT;
                                path = "/raph/projects/towerdefense/Images/Level-1/Border_Top_Left.png";
                            }

                            case 21 ->
                            {
                                t = TileType.BORDER_TOP;
                                random = rand.nextInt(3) + 1;
                                switch (random)
                                {
                                    case 1 -> path = "/raph/projects/towerdefense/Images/Level-1/Border_Top_1.png";
                                    case 2 -> path = "/raph/projects/towerdefense/Images/Level-1/Border_Top_2.png";
                                    case 3 -> path = "/raph/projects/towerdefense/Images/Level-1/Border_Top_3.png";
                                }
                            }

                            case 22 ->
                            {
                                t = TileType.BORDER_TOP_RIGHT;
                                path = "/raph/projects/towerdefense/Images/Level-1/Border_Top_Right.png";
                            }

                            case 23 ->
                            {
                                t = TileType.BORDER_RIGHT;

                            }

                            case 24 ->
                            {
                                t = TileType.BORDER_BOTTOM_RIGHT;
                                path = "/raph/projects/towerdefense/Images/Level-1/Border_Bottom_Right.png";
                            }

                            case 25 ->
                            {
                                t = TileType.BORDER_BOTTOM;

                            }

                            case 26 ->
                            {
                                t = TileType.BORDER_BOTTOM_LEFT;
                                path = "/raph/projects/towerdefense/Images/Level-1/Border_Bottom_Left.png";

                            }

                            case 27 ->
                            {
                                t = TileType.BORDER_LEFT;

                            }

                            case 28 ->
                            {
                                t = TileType.BORDER_ROAD;

                            }

                            default ->
                                     {
                                         t = TileType.TOWER_SLOT;
                                         random = rand.nextInt(3) + 1;
                                         switch (random)
                                         {
                                             case 1 -> path = "/raph/projects/towerdefense/Images/Level-1/Tower_Slot_1.png";
                                             case 2 -> path = "/raph/projects/towerdefense/Images/Level-1/Tower_Slot_2.png";
                                             case 3 -> path = "/raph/projects/towerdefense/Images/Level-1/Tower_Slot_3.png";
                                         }
                                     }
                        }
                    }

                }
                this.tileTemplate[k][j] = new Tile(k, j, t, path);
            }
        }
    }

    private void buildEnemyPath()
    {
        // 1. trouver la case de départ (BORDER_ROAD)
        Tile start = null;
        for (Tile[] row : tileTemplate)
        {
            for (Tile t : row)
            {
                if (t.getType() == TileType.BORDER_ROAD)
                {
                    start = t;
                    break;
                }
            }
        }

        // 2. suivre le chemin case par case
        List<Tile> path = new ArrayList<>();
        Tile current = start;
        Tile previous = null;

        while (current != null) {
            path.add(current);
            Tile next = getNextPathTile(current, previous);
            previous = current;
            current = next;
        }

        this.enemyPath = path.toArray(new Tile[0]);
    }

    private Tile getNextPathTile(Tile current, Tile previous) {
        int x = current.getGridX();
        int y = current.getGridY();

        // cases adjacentes (haut, bas, gauche, droite)
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};

        for (int[] d : directions) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (nx < 0 || nx >= HEIGHT || ny < 0 || ny >= WIDTH) continue;

            Tile neighbor = tileTemplate[nx][ny];

            // on ne revient pas en arrière
            if (neighbor == previous) continue;

            // la case est sur le chemin
            if (neighbor.getType().getType() <= 5) return neighbor;
        }
        return null; // fin du chemin = base atteinte
    }
}
