package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Mati on 2017-04-22.
 */
@Service
public class ImageService {

    ImageRepository imageRepository;
    RepairService repairService;
    StoreService storeService;

    @Autowired
    public void setImageRepository(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setRepairService(RepairService repairService){
        this.repairService = repairService;
    }

    @Autowired
    public void setStoreService(StoreService storeService){
        this.storeService = storeService;
    }

    public String createInitialImageName(Image image){
       return createRepairId(image)+createStoreType(image);
    }

    public String createRepairId(Image image){
        return "repair"+getRepairIdFromService(image);
    }

    public String createStoreType(Image image){
        return "type"+getStoreTypeFromService(image);
    }

    public long getRepairIdFromService(Image image){
        return repairService.getRepairId(getRepairFromService(image));
    }

    public String getStoreTypeFromService(Image image){
        return storeService.getStoreTypeNormalized(getStoreFromService(image));
    }

    public Repair getRepairFromService(Image image){
        return repairService.getRepairFromImage(image);
    }

    public Store getStoreFromService(Image image){
        return storeService.getStoreFromImage(image);
    }

    public MultipartFile getImageFileFromListById(ListImage listImage, int id){
        return listImage.getImageList().get(id);
    }
}
