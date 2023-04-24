package simulator;

public class GridLocation {
    LocationType type;
    double cost;
    
    public GridLocation(LocationType type) {
        this.type = type;
    }

    public GridLocation(LocationType type, int cost) {
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

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case AGENT: return "A";
            case OBSTACLE: return "/";
            case EMPTY: return Integer.toString(0);
            case RUBBLE: return Double.toString(cost);
            case GOAL: return "G";
            default: return "";
        }
    }
}
