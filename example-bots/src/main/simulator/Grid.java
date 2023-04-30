package simulator;

import java.util.Random;

public class Grid {
    // World Statistics
    public GridLocation[][] world;
    public Random rng;
    public int rows;
    public int columns;
    public int obstacles;
    public Coords agentPos;
    public Coords goalPos;
    
    // Constants for world generation
    final int OBSTACLE_COST = 10000;
    final int MAX_RUBBLE = 100;
    final int MIN_RUBBLE = 1;
    final int MAX_SIZE = 25;
    final int MIN_SIZE = 5;

    public Grid(long seed, double congestion, int rows, int columns) {

        this.rng = new Random(seed);

        this.rows = rows;
        this.columns = columns;

        // Generate two random numbers to make the seed equal to that of the random size
        randomIntInRange(rng, MAX_SIZE, MIN_SIZE);
        randomIntInRange(rng, MAX_SIZE, MIN_SIZE);

        this.obstacles = (int) Math.round(rows * columns * congestion);
        this.world = new GridLocation[rows][columns];

        initializeGrid();
    }

    public Grid(long seed, double congestion) {
        this.rng = new Random(seed);

        this.rows = randomIntInRange(rng, MAX_SIZE, MIN_SIZE);
        this.columns = randomIntInRange(rng, MAX_SIZE, MIN_SIZE);
        this.obstacles = (int) Math.round(rows * columns * congestion);
        this.world = new GridLocation[rows][columns];

        initializeGrid();
    }

    private void initializeGrid() {
        // Set agent location
        this.agentPos = new Coords(rng.nextInt(rows), rng.nextInt(columns));
        this.world[this.agentPos.x][this.agentPos.y] = new GridLocation(LocationType.AGENT, 0);

        // Set goal location (could generate same location as agent)
        this.goalPos = new Coords(rng.nextInt(rows), rng.nextInt(columns));
        while (this.agentPos.equals(goalPos)) {
            this.goalPos = new Coords(rng.nextInt(rows), rng.nextInt(columns));
        }
        this.world[this.goalPos.x][this.goalPos.y] = new GridLocation(LocationType.GOAL, 0);

        // Set obstacles on map
        for (int i = 0; i < obstacles; i++) {
            Coords obstaclePos = new Coords(rng.nextInt(rows), rng.nextInt(columns));
            while (this.world[obstaclePos.x][obstaclePos.y] != null) {
                obstaclePos = new Coords(rng.nextInt(rows), rng.nextInt(columns));
            }
            this.world[obstaclePos.x][obstaclePos.y] = new GridLocation(LocationType.OBSTACLE, OBSTACLE_COST);
        }

        // Randomize the map
        LocationType[] options = {LocationType.EMPTY, LocationType.RUBBLE};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.world[i][j] == null) {
                    LocationType type = options[rng.nextInt(options.length)];
                    if (type == LocationType.RUBBLE) {
                        this.world[i][j] = new GridLocation(
                                type, randomIntInRange(rng, MAX_RUBBLE, MIN_RUBBLE)
                        );
                    } else {
                        this.world[i][j] = new GridLocation(LocationType.RUBBLE, 1);
                    }
                }
            }
        }
    }

    private int randomIntInRange(Random rng, int max, int min) {
        return rng.nextInt(max - min) + min;
    }

    public GridLocation getLocation(int x, int y) {
        return world[x][y];
    }

    public void setLocation(int x, int y, GridLocation location) {
        this.world[x][y] = location;
    }

    @Override
    public String toString() {
        String res = String.format("Grid: %d x %d with %d obstacles\n", rows, columns, obstacles);
        for (GridLocation[] row : world) {
            StringBuilder rowString = new StringBuilder();
            for (GridLocation location : row) {
                rowString.append(String.format("%-4s", location));
            }
            res += rowString.toString() + "\n";
        }
        return res;
    }
}
