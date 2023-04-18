import java.util.Random;

public class Grid {
    GridLocation[][] world;

    public Grid(long seed) {
        Random rng = new Random(seed);
    }
}
