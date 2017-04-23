package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mati on 2017-04-23.
 */
@Service
public class PartService {

    private PartRepository partRepository;
    private RepairService repairService;
    private StoreService storeService;

    @Autowired
    public void setPartRepository(PartRepository partRepository){
        this.partRepository = partRepository;
    }

    @Autowired
    public void setRepairService(RepairService repairService){
        this.repairService = repairService;
    }

    @Autowired
    public void setStoreService(StoreService storeService){
        this.storeService = storeService;
    }


    public Part getPartFromChangePart(ChangePart part){
        return partRepository.findOne(part.getPartId());
    }

    public void savePart(Part part){
        partRepository.save(part);
    }

    public void setStore(Part part,Store store){
        part.setStore(store);
    }

    public boolean getResolved(Part part){
        return part.getResolved();
    }

    public Long getId(Part part){
        return part.getId();
    }

    public boolean isReadyForRepair(Part part){
        return null!=getId(part);
    }

    public Part getPartFromId(Long id){
        return partRepository.findOne(id);
    }

    public Store getStoreFromPart(Part part){
        return part.getStore();
    }

    public void setResolved(Part part,boolean status){
        part.setResolved(status);
    }

    public boolean isSavePartProperly(Part part){
        Part repairedPart = getPartFromId(getId(part));
        storeService.orderUniquePartIfNecessary(getStoreFromPart(repairedPart));
        if(storeService.isPartInStore(getStoreFromPart(repairedPart))){
            setResolved(repairedPart,true);
            savePart(repairedPart);
            return true;
        }
        else
            return false;
    }


    public List<Part> inResolvingPart(long id){
        List<Part> partsFromRepair = repairService.getPartFromRepair(repairService.getRepairFromId(id));
        partsFromRepair = partsFromRepair.stream().filter(part -> getResolved(part) == false).collect(Collectors.toList());
        return partsFromRepair;
    }

    public List<Part> partToRepair(List<Part> partList){
        partList = partList.stream().filter(p -> p.getStore().getId()>8 && p.getResolved()==false).collect(Collectors.toList());
        return partList;
    }

    public List<SinglePart> getSinglePartListFromListPartRepair(ListPartRepair listPartRepair){
        return listPartRepair.getPartRepair();
    }

    public List<List<Store>> allPartToChose(List<Part> partList){
        List<List<Store>> allToChoose = new ArrayList<>();
        for(Part singlePart : partList){
            List<Store> storeList = storeService.getAllStoreFromType(storeService.getStoreType(getStoreFromPart(singlePart)));
            allToChoose.add(storeList);
        }
        return allToChoose;
    }

    public ArrayList<ChangePart> getChangePartList(List<Part> partList){
        ArrayList<ChangePart> changeParts = new ArrayList<>();
        partList
                .stream()
                .forEach(
                            part->
                                    {
                                        changeParts.add(new ChangePart(getId(part)));
                                    }
                        )
                ;
        return changeParts;
    }
}
