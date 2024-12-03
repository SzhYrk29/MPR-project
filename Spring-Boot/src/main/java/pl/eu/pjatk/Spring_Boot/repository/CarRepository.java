package pl.eu.pjatk.Spring_Boot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.eu.pjatk.Spring_Boot.model.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface CarRepository extends CrudRepository <Car, Long> {
    @Query("SELECT c FROM Car c WHERE LOWER(c.brand) = LOWER(:brand)")
    List<Car> findByBrand(@Param("brand") String brand);
    @Query("SELECT c FROM Car c WHERE LOWER(c.color) = LOWER(:color)")
    List<Car> findByColor(String color);
}