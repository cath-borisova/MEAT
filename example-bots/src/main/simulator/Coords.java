package simulator;

import java.util.Objects;

public class Coords {
    public int x, y;
    public double pathCost;
    
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
        this.pathCost = 0;
    }

    public Coords(int x, int y, double pathCost) {
        this.x = x;
        this.y = y;
        this.pathCost = pathCost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        Coords otherCoords = (Coords) other;
        return this.x == otherCoords.x && this.y == otherCoords.y;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "x=" + x +
                ", y=" + y +
                ", pathCost=" + pathCost +
                '}';
    }
}
