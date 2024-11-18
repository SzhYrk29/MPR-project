package pl.eu.pjatk.Spring_Boot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.eu.pjatk.Spring_Boot.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository <Car, Long> {
    List<Car> findByBrand(String brand);
    List<Car> findByColor(String color);
}