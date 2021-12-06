package deliveryService.cargo;

public class Cargo {
    private final double width;
    private final double height;
    private final double depth;
    private final boolean isFragile;

    public Cargo(double width, double height, double depth, boolean isFragile){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.isFragile = isFragile;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public double getVolume(){
        return width * height * depth;
    }
}
