package main.java.GarageAssistantApp.RepositoriesPackage;

/**
 * Created by Mati on 2016-12-23.
 */
import main.java.GarageAssistantApp.EntityPackage.Part;
import org.springframework.data.repository.CrudRepository;
public interface PartRepository extends CrudRepository<Part, Long>  {
}
