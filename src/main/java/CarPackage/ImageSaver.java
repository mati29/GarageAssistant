package main.java.CarPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by Mati on 2017-03-26.
 */
@Component
public class ImageSaver {

    ImageService imageService;

    @Autowired
    public ImageSaver(ImageService imageService){
        this.imageService = imageService;
    }

    final String saveImage(MultipartFile imageStream,Image image,int pictureNumber){
        String imageName = createFullImageName(image,pictureNumber,imageStream.getContentType().replaceAll("/","."));
        File directory = new File("images");
        try {
            createDirectoryIfNecessary(directory);
            File pictureToSave = new File(createFullPathToImage(directory.getAbsolutePath(),imageName));
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(pictureToSave));
            stream.write(imageStream.getBytes());
            stream.close();
            return imageName.replace(".","`");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    final String getImageNameFromImageService(Image image){
        return imageService.createInitialImageName(image);
    }

    final String createFullImageName(Image image,int pictureNumber,String extension){
        return getImageNameFromImageService(image)+"number"+pictureNumber+extension;
    }

    final String createFullPathToImage(String absolutePath,String imageName){
        return absolutePath+File.separator+imageName;
    }

    final File createDirectoryIfNecessary(File directory){
        if (!directory .exists()) {
            directory.mkdirs();
        }
        return directory;
    }

}
