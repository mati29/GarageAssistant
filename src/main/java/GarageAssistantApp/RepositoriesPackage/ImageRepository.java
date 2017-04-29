package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Image;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mati on 2017-03-26.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {
}
