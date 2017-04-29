package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Settings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2017-03-11.
 */
@Repository
public interface SettingsRepository extends CrudRepository<Settings, Long>{
}
