package main.java.CarPackage;

/**
 * Created by Mati on 2017-03-28.
 */
public class RepairBillDTO {
    private int position;
    private double partsTotal;
    private int exchangeTime;
    private double exchangeCost;
    private double exchangeVat;
    private double exchangeTotal;
    private double repairCost;
    private String completedBy;

    private final double vatMult = 0.23;
                                                    //stawka * godzina
    RepairBillDTO(double partsTotal,int exchangeTime,int hourly,String completedBy,int position){
        this.partsTotal = partsTotal;
        this.exchangeTime = exchangeTime;
        this.exchangeCost = exchangeTime * hourly;
        this.exchangeVat = exchangeCost * vatMult;
        this.exchangeTotal = exchangeCost + exchangeVat;
        this.repairCost = partsTotal + exchangeTotal;
        this.completedBy = completedBy;
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }
    public int getExchangeTime(){
        return this.exchangeTime;
    }
    public double getPartsTotal(){
        return this.partsTotal;
    }
    public double getExchangeCost(){
        return this.exchangeCost;
    }
    public double getExchangeVat(){
        return this.exchangeVat;
    }
    public double getRepairCost(){
        return this.repairCost;
    }
    public double getExchangeTotal(){
        return this.exchangeTotal;
    }
    public String getCompletedBy(){
        return this.completedBy;
    }

    public void setPosition(int position){
        this.position=position;
    }
    public void setExchangeTime(int exchangeTime){
        this.exchangeTime=exchangeTime;
    }
    public void setPartsTotal(double partsTotal){
        this.partsTotal=partsTotal;
    }
    public void setExchangeCost(double exchangeCost){
        this.exchangeCost=exchangeCost;
    }
    public void setExchangeVat(double partNetto){
        this.exchangeVat=exchangeVat;
    }
    public void setRepairCost(double repairCost){
        this.repairCost=repairCost;
    }
    public void setExchangeTotal(double exchangeTotal){
        this.exchangeTotal=exchangeTotal;
    }
    public void setCompletedBy(String completedBy){
        this.completedBy=completedBy;
    }

}
