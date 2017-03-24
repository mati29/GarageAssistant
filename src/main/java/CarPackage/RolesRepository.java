package main.java.CarPackage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2016-11-09.
 */
@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {
}
