package main.java.CarPackage;

import java.util.ArrayList;

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
}
