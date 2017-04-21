package main.java.CarPackage;

import java.util.List;

/**
 * Created by Mati on 2017-03-23.
 */
public class ListRepair {
    private List<Repair> repairList;

    public List<Repair> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<Repair> repairList) {
        this.repairList = repairList;
    }

    public ListRepair() {}

    public ListRepair(List<Repair> repairList){
        this.repairList = repairList;
    }
}
