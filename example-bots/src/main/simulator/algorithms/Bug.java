package simulator.algorithms;
import simulator.*;

public class Bug implements Algo{

    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius){
        int dx = goal.x - initial.x;
        int dy = goal.y - initial.y;
        dx = dx == 0 ? 0 : dx > 0 ? 1 : -1;
        dy = dy == 0 ? 0 : dy > 0 ? 1 : -1;
        return bugMove(grid, initial.x, initial.y, dx, dy, 0);
    }

    private Coords bugMove(Grid grid, int x, int y, int dx, int dy, int depth) {
        int newX = x + dx;
        int newY = y + dy;

        if (!inBounds(grid, newX, newY)) {
            return new Coords(x, y);
        }

        if (grid.getLocation(newX, newY).getType() != LocationType.OBSTACLE) {
            return new Coords(newX, newY);
        }

        int newDX = -dy;
        int newDY = dx;

        return depth < 16 ? bugMove(grid, x, y, newDX, newDY, depth + 1) : new Coords(x, y);
    }

    private boolean inBounds(Grid grid, int x, int y) {
        return !(x < 0 || y < 0 || x >= grid.world.length || y >= grid.world[0].length);
    }
}
