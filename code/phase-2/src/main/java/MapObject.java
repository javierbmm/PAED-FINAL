import com.google.gson.annotations.SerializedName;

public class MapObject {
    private final int id;

    @SerializedName("X1")
    private final int x1;

    @SerializedName("X2")
    private final int x2;

    @SerializedName("Y1")
    private final int y1;

    @SerializedName("Y2")
    private final int y2;

    public MapObject(int id, int x1, int x2, int y1, int y2) {
        this.id = id;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public float[] getXCoords() {
        return new float[]{x1, x2};
    }

    public float[] getYCoords() {
        return new float[]{y1, y2};
    }

    public float[] getCoords() {
        return new float[]{x1,y2};
    }

    public int[] getSize() {
        return new int[]{x2 - x1, y1 - y2};
    }

    public int getId() {
        return id;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
}
