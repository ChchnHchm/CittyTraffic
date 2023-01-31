package bigdata;

public class CittyTrafficValue {
    private int speed;
    private int sens;

    public CittyTrafficValue(int speed,int sens){
        this.sens=sens;
        this.speed=speed;
    }
    public int getSpeed() {
        return speed;
    }

    public int getSens() {
        return sens;
    }
}
