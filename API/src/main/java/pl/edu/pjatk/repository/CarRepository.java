package pl.edu.pjatk.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.edu.pjatk.model.Car;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE LOWER(c.brand) = LOWER(:brand)")
    List<Car> findByBrand(@Param("brand") String brand);
    @Query("SELECT c FROM Car c WHERE LOWER(c.color) = LOWER(:color)")
    List<Car> findByColor(String color);
}
