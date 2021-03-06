package main.java.GarageAssistantApp.StandardPackage;

import main.java.GarageAssistantApp.EntityPackage.Image;
import main.java.GarageAssistantApp.ServicesPackage.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by Mati on 2017-03-26.
 */
@Component
public class ImageSaver {

    private ImageService imageService;

    @Autowired
    public ImageSaver(ImageService imageService){
        this.imageService = imageService;
    }

    final public String saveImage(MultipartFile imageStream, Image image, int pictureNumber){
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

    final String getImagePathName(String imageName){
        String directory = new File("images").getAbsolutePath();
        String[] fileandFileType = imageName.split("`");
        return directory+File.separator+fileandFileType[0]+"."+fileandFileType[1];
    }

    final File getFileFromPathName(String pathName){
        return new File(pathName);
    }
    final byte[] getImageBytes(File serverFile) throws IOException{
        return Files.readAllBytes(serverFile.toPath());
    }

    final public byte[] getImageInByteFromImageName(String imageName) throws IOException{
        return getImageBytes(getFileFromPathName(getImagePathName(imageName)));
    }

}
