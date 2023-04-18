package simulator.src;

public class GridLocation {
    LocationType type;
    double cost;
    
    public GridLocation(LocationType type) {
        this.type = type;
    }

    public GridLocation(LocationType type, double cost) {
        this.type = type;
        this.cost = cost;
    }

    public LocationType getType() {
        return this.type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
