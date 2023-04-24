package simulator.instrumentor;

public class InstrumentedGrid {

    private Object grid;

    public InstrumentedGrid(Object grid) {
        this.grid = grid;
    }

    public Object getUnderlyingObject() {
        return grid;
    }
}
