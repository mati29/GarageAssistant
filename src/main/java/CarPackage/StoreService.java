package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void saveStore(Store store){
        storeRepository.save(store);
    }

    public List<Store> getAllStore(){
        return storeRepository.findAll();
    }

    public List<Store> getStoreFromList(ListStore listStore){
        return listStore.getStoreList();
    }

    public ListStore getListStore(){
        return new ListStore(storeSortByType(getFilteredStore()));
    }

    public List<Store> getFilteredStore(){//without unique part and main type of pary
        List<Store> stores = getAllStore();
        stores = stores.stream().filter(s -> !s.getType().matches("[A-Z]*") && s.getPrice()!=0D).collect(Collectors.toList());
        return stores;
    }

    public List<Store> storeSortByType(List<Store> stores){
        stores.sort(new StoreComparator());
        return stores;
    }

    public List<Store> storeSortByModel(List<Store> stores){
        stores.sort((s1,s2)->getModelFromStore(s1).compareTo(getModelFromStore(s2)));
        return stores;
    }

    public List<Store> storeSortByBrand(List<Store> stores){
        stores.sort((s1,s2)->getBrandFromStore(s1).compareTo(getBrandFromStore(s2)));
        return stores;
    }

    public List<Store> storeSortByPrice(List<Store> stores){
        stores.sort((s1,s2)->(int)(getPriceFromStore(s1)-getPriceFromStore(s2)));//possible problematic int from double
        return stores;
    }

    public List<Store> storeSortById(List<Store> stores){
        stores.sort((s1,s2)->(int)(getIdFromStore(s1)-getIdFromStore(s2)));
        return stores;
    }

    public List<Store> storeSortByAmount(List<Store> stores){
        stores.sort((s1,s2)->(int)(getAmountFromStore(s1)-getAmountFromStore(s2)));
        return stores;
    }

    public long getIdFromStore(Store store){
        return store.getId();
    }

    public String getModelFromStore(Store store){
        return store.getModel();
    }

    public String getBrandFromStore(Store store){
        return store.getBrand();
    }

    public double getPriceFromStore(Store store){
        return store.getPrice();
    }

    public int getAmountFromStore(Store store){
        return store.getAmount();
    }

}
