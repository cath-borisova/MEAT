package simulator.instrumentor;

public class InstrumentedCoords {

    private Object coords;

    public InstrumentedCoords(Object coords) {
        this.coords = coords;
    }

    public Object getUnderlyingObject() {
        return coords;
    }
}
