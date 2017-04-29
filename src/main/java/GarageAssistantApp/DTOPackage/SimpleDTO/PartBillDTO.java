package main.java.GarageAssistantApp.DTOPackage.SimpleDTO;

/**
 * Created by Mati on 2017-03-28.
 */
public class PartBillDTO {
    private int position;
    private int repairNumber;
    private String partName;
    private double partNetto;
    private double partVat;
    private double partTotal;
    private final double vatMult = 0.23;

    public PartBillDTO(int position,String partName,double partNetto,int repairNumber){
        this.position = position;
        this.partName = partName;
        this.partNetto = partNetto;
        this.partVat = Math.floor(partNetto * vatMult * 100) / 100;
        this.partTotal = this.partNetto + partVat;
        this.repairNumber = repairNumber;
    }

    public int getPosition(){
        return this.position;
    }
    public int getRepairNumber(){
        return this.repairNumber;
    }
    public double getPartNetto(){
        return this.partNetto;
    }
    public double getPartVat(){
        return this.partVat;
    }
    public double getPartTotal(){
        return this.partTotal;
    }
    public String getPartName(){
        return this.partName;
    }
    public void setPosition(int position){
        this.position=position;
    }
    public void setRepairNumber(int repairNumber){
        this.repairNumber=repairNumber;
    }
    public void setPartNetto(double partNetto){
        this.partNetto=partNetto;
    }
    public void setPartVat(double partVat){
        this.partVat=partVat;
    }
    public void setPartTotal(double partTotal){
        this.partTotal=partTotal;
    }
    public void setPartName(String partName){
        this.partName=partName;
    }
}
