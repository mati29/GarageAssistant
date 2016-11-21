package main.java.CarPackage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Mati on 2016-11-21.
 */
@Repository
public interface CommissionRepository extends CrudRepository<Commission, Long> {
}
