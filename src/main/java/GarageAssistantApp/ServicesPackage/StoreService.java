package main.java.GarageAssistantApp.ServicesPackage;

import main.java.GarageAssistantApp.DTOPackage.ChangePart;
import main.java.GarageAssistantApp.EntityPackage.Image;
import main.java.GarageAssistantApp.EntityPackage.Part;
import main.java.GarageAssistantApp.EntityPackage.Store;
import main.java.GarageAssistantApp.DTOPackage.ListDTO.ListStore;
import main.java.GarageAssistantApp.RepositoriesPackage.StoreRepository;
import main.java.GarageAssistantApp.StandardPackage.StoreComparator;
import main.java.GarageAssistantApp.DTOPackage.SimpleDTO.UniquePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-04-22.
 */
@Service
public class StoreService {

    private StoreRepository storeRepository;
    private PartService partService;

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    @Autowired
    public void setPartService(PartService partService){
        this.partService = partService;
    }

    public Store getStoreFromId(Long id){
        return storeRepository.findOne(id);
    }

    public Store getStoreFromImage(Image image){
        return image.getStore();
    }

    public String getStoreTypeNormalized(Store store){
        return store.getType().toLowerCase();
    }

    public String getStoreType(Store store){
        return store.getType();
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
        stores.sort((s1,s2)->(int)((getPriceFromStore(s1)-getPriceFromStore(s2))*100));
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

    public Long getIdFromStore(Store store){
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

    public void setUniqueAmount(Store store){
        store.setAmount(1);
    }

    public void setAmount(Store store,int amount){
        store.setAmount(amount);
    }

    public void setBrand(Store store,String brand){
        store.setBrand(brand);
    }

    public void setModel(Store store,String model){
        store.setModel(model);
    }

    public void setType(Store store,String type){
        store.setType(type);
    }


    public boolean checkDefault(ChangePart part){
        return part.getStoreId()==1?true:false;

    }

    public boolean checkUnique(ChangePart part){
        return part.getStoreId()==0?true:false;

    }

    public Store getStoreFromChangePart(ChangePart part){
        return storeRepository.findOne(part.getStoreId());
    }

    public void orderUniquePartIfNecessary(Store store){
        if(getStoreType(store).matches("[A-Z]+"))
            setUniqueAmount(store);
    }

    public boolean isPartInStore(Store store){
        if(getAmountFromStore(store)>=1){
            setAmount(store,getAmountFromStore(store)-1);
            return true;
        }
        else
            return false;

    }

    public Store getStoreFromType(String type){
        Store store;
        switch(type){
            case "Engine": store = getStoreFromId(2L);break;
            case "Transmission": store = getStoreFromId(3L);break;
            case "Tires": store = getStoreFromId(4L);break;
            case "Body": store = getStoreFromId(5L);break;
            case "Lights": store = getStoreFromId(6L);break;
            case "Equipment": store = getStoreFromId(7L);break;
            case "Brakes": store = getStoreFromId(8L);break;
            default: store = null;
        }
        return store;
    }
    public List<Store> getAllStoreFromType(String type){
        List<Store> storeList = new ArrayList<>();
        switch(type){
            case "ENGINE": storeList = getStoresByType("Engine");break;
            case "TRANSMISSION": storeList = getStoresByType("Transmission");break;
            case "TIRES": storeList = getStoresByType("Tires");break;
            case "BODY": storeList = getStoresByType("Body");break;
            case "LIGHTS": storeList = getStoresByType("Lights");break;
            case "EQUIPMENT": storeList = getStoresByType("Equipment");break;
            case "BRAKES": storeList = getStoresByType("Brakes");break;
            default: storeList = null;
        }
        storeList.add(selectEmpty());
        return storeList;
    }

    public List<Store> getStoresByType(String type){
        return storeRepository.findByType(type);
    }

    public Store selectEmpty(){
        return storeRepository.findByType("EMPTY").iterator().next();
    }

    public Store selectUnique(){
        return storeRepository.findByType("UNIQUE").iterator().next();
    }

    public void addUniquePart(ArrayList<UniquePart> uniqueParts){
        uniqueParts
                .forEach(
                            uniquePart ->
                                            {
                                                Store uniqueStoreToAdd = new Store();
                                                setBrand(uniqueStoreToAdd,partService.getBrandFromUniquePart(uniquePart));
                                                setModel(uniqueStoreToAdd,partService.getModelFromUniquePart(uniquePart));
                                                setType(uniqueStoreToAdd,partService.getTypeFromUniquePart(uniquePart));
                                                Part partMakeUnique = partService.getPartFromId(partService.getPartIdFromUniquePart(uniquePart));
                                                partService.setStore(partMakeUnique,uniqueStoreToAdd);
                                                saveStore(uniqueStoreToAdd);
                                            }
                        )
                ;
    }

    public Store saveCustomPart(Store store,String type,String model,String brand){
        setType(store,type);
        setModel(store,model);
        setBrand(store,brand);
        saveStore(store);
        return store;
    }
}
