package simulator.instrumentor;

public class InstrumentedGridLocation {

    public Object gridLocation;

    public InstrumentedGridLocation(Object gridLocation) {
        this.gridLocation = gridLocation;
    }

    public Object getUnderlyingObject() {
        return gridLocation;
    }
}
