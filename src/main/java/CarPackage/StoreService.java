package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mati on 2017-04-22.
 */
@Service
public class StoreService {

    private StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    public Store getStoreFromImage(Image image){
        return image.getStore();
    }

    public String getStoreType(Store store){
        return store.getType().toLowerCase();
    }
}
