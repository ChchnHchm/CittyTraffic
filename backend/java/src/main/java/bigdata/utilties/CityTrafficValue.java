package bigdata.utilties;
import java.io.Serializable;

public class CityTrafficValue implements Serializable{
    private int countVELO=0;
    private int countPL=0;
    private int countVL=0;
    private int countMOTO=0;
    private int countBUS=0;
    private int countUT=0;
    private int count=1;
    private int sensSortie=0;
    private int sensEntree=0;
    private int speed;

    public CityTrafficValue(int speed,int sens,String type){
        this.speed=speed;
        initialiseCount(type, sens);
        
    }

    public CityTrafficValue(CityTrafficValue val){
        this.countVELO=val.countVELO;
        this.countPL=val.countPL;
        this.countMOTO=val.countMOTO;
        this.countBUS=val.countBUS;
        this.countUT=val.countUT;
        this.count=val.count;
        this.sensSortie=val.sensSortie;
        this.sensEntree=val.sensEntree;
        this.speed=val.speed;
        this.countVL=val.sensSortie;
    }

    private void initialiseCount(String type ,int sens){
        switch (type) {
            case "MOTO":
            countMOTO=1;
                break;

            case "VELO":
            countVELO=1;
                break;

            case "PL":
            countPL=1;
                break;

            case "VL":
            countVL=1;
                break;

            case "BUS":
            countBUS=1;
                break;

            case "UT":
            countUT=1;
                break;
            default:
                break;
        }
        if(sens==1){
            sensEntree=1;
        }
        else if(sens==2){
            sensSortie=1;
        }
    }

   public int getCountVELO() {
        return countVELO;
    }

    public int getCountPL() {
        return countPL;
    }

    public int getCountVL() {
        return countVL;
    }

    public int getCountMOTO() {
        return countMOTO;
    }

    public int getCountBUS() {
        return countBUS;
    }

    public int getCountUT() {
        return countUT;
    }

    public int getSensSortie() {
        return sensSortie;
    }

    public int getSensEntree() {
        return sensEntree;
    }

    public int getSpeed() {
        return speed;
    }

    public void sumTwoValues(CityTrafficValue val2){

    //TYPE  
        this.countBUS=val2.countBUS+this.countBUS;
        this.countMOTO=this.countMOTO+val2.countMOTO;
        this.countPL=this.countPL+val2.countPL;
        this.countUT=this.countUT+val2.countUT;
        this.countVL=this.countVL+val2.countVL;
        this.countVELO=this.countVELO+val2.countVELO;
        //SENS
        this.sensEntree+=val2.sensEntree;
        this.sensSortie+=val2.sensSortie;

        //Compteur total
        this.count+=val2.count;
        //Vitesse
        this.speed+=val2.speed;

    }
    public void meanOfSpeed(){
        this.speed=this.speed/this.count;
    }

    @Override
    public String toString() {
        return countBUS+","+countMOTO+","+countPL+","+countUT+","+countVL+","+countVELO+","+sensEntree+","+sensSortie+","+speed;
    }
}
    
