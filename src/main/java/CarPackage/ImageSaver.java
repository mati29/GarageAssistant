package main.java.CarPackage;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by Mati on 2017-03-26.
 */
public class ImageSaver {

    static final String saveImage(MultipartFile imageStream,Image image,int pictureNumber){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String imageName = "repair"+image.getRepair().getId()+"type"+image.getStore().getType()+"number"+pictureNumber+imageStream.getContentType().replaceAll("/",".");
        File directory = new File("images");
        try {
            if (!directory .exists()) {
                directory.mkdirs();
            }
            File pictureToSave = new File(directory.getAbsolutePath()+File.separator+imageName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(pictureToSave));
            stream.write(imageStream.getBytes());
            stream.close();
            return imageName.replace(".","`");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
