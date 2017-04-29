package main.java.GarageAssistantApp.RepositoriesPackage;

import main.java.GarageAssistantApp.EntityPackage.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2016-11-09.
 */
@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {
}
