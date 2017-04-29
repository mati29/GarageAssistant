package main.java.GarageAssistantApp.DTOPackage.ListDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Mati on 2017-03-26.
 */
public class ListImage {

    private List<MultipartFile> imageList;

    public List<MultipartFile> getImageList() {
        return imageList;
    }

    public void setImageList(List<MultipartFile> imageList) {
        this.imageList = imageList;
    }
}
