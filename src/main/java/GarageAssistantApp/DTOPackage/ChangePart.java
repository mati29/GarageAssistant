package main.java.GarageAssistantApp.DTOPackage;

/**
 * Created by Mati on 2017-02-04.
 */
public class ChangePart {
    private Long partId;
    private Long storeId;
    public void setPartId(Long partId) {
        this.partId = partId;
    }
    public Long getPartId() {
        return partId;
    }
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public Long getStoreId() {
        return storeId;
    }

    public ChangePart() {}
    public ChangePart(Long partId){
        this.partId = partId;
    }
}
