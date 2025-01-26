package pl.edu.pjatk.Spring_Boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.edu.pjatk.exception.CarNotFoundException;
import pl.edu.pjatk.exception.EmptyInputException;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.repository.CarRepository;
import pl.edu.pjatk.service.CarService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {
    private CarRepository carRepository;
    private CarService underTest;

    @BeforeEach
    void setUp() {
        this.carRepository = Mockito.mock(CarRepository.class);
        this.underTest = new CarService(carRepository);
    }

    @Test
    void shouldReturnAListOfAllCars() {
        underTest.getCars();
        verify(carRepository).findAll();
    }

    @Test
    void shouldAddCar() {
        Car car = new Car("Toyota", "Blue");
        underTest.addCar(car);
        verify(carRepository).save(car);
    }

    @Test
    void shouldCaptureCarAndAddItToTheRepository() {
        Car car = new Car("Toyota", "Blue");

        underTest.addCar(car);

        ArgumentCaptor<Car> carArgumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar.getBrand()).isEqualTo("Toyota");
        assertThat(capturedCar.getColor()).isEqualTo("Blue");
    }

    @Test
    void shouldThrowEmptyInputExceptionBecauseOfAnEmptyInputWhileTryingToAddACar() {
        Car car = new Car("", "");
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldNotAddCarWhenBrandIsEmpty() {
        Car car = new Car("", "Midnight blue");
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldNotAddCarWhenColorIsEmpty() {
        Car car = new Car("BMW", "");
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldNotAddCarWhenBrandIsNull() {
        Car car = new Car(null, "Midnight blue");
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldNotAddCarWhenColorIsNull() {
        Car car = new Car("BMW", null);
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldNotAddCarWhenBrandAndColorAreNull() {
        Car car = new Car(null, null);
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void shouldReturnListOfCarsByBrand() {
        String brand = "Toyota";
        List<Car> expectedCars = Arrays.asList(new Car("Toyota", "Red"), new Car("Toyota", "White"));
        when(carRepository.findByBrand(brand)).thenReturn(expectedCars);

        List<Car> actualCars = underTest.getCarByBrand(brand);

        assertNotNull(actualCars);
        assertEquals(expectedCars.size(), actualCars.size());
        assertEquals(expectedCars.getFirst().getBrand(), actualCars.getFirst().getBrand());
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhileTryingToGetListOfCarsByBrand() {
        String brand = "Toyota";
        when(carRepository.findByBrand(brand)).thenReturn(List.of());

        assertThrows(CarNotFoundException.class, () -> underTest.getCarByBrand(brand));
    }

    @Test
    void shouldReturnListOfCarsByColor() {
        String color = "Red";
        List<Car> expectedCars = Arrays.asList(new Car("Toyota", "Red"), new Car("Honda", "Red"));
        when(carRepository.findByColor(color)).thenReturn(expectedCars);

        List<Car> actualCars = underTest.getCarByColor(color);

        assertNotNull(actualCars);
        assertEquals(expectedCars.size(), actualCars.size());
        assertEquals(expectedCars.getFirst().getColor(), actualCars.getFirst().getColor());
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhileTryingToGetListOfCarsByColor() {
        String color = "Red";
        when(carRepository.findByColor(color)).thenReturn(List.of());

        assertThrows(CarNotFoundException.class, () -> underTest.getCarByColor(color));
    }

    @Test
    void shouldReturnCarById() {
        Long id = 1L;
        Car expectedCar = new Car("Toyota", "Red");
        when(carRepository.findById(id)).thenReturn(Optional.of(expectedCar));

        Car actualCar = underTest.getCarById(id);

        assertNotNull(actualCar);
        assertEquals(expectedCar.getId(), actualCar.getId());
        assertEquals(expectedCar.getBrand(), actualCar.getBrand());
    }

    @Test
    void shouldThrowInvalidInputExceptionWhileTryingToGetCarWithNegativeId() {
        Long invalidId = -1L;

        assertThrows(InvalidInputException.class, () -> underTest.getCarById(invalidId));
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhileTryingToGetCarWithIdThatDoesNotExist() {
        Long id = 1L;
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> underTest.getCarById(id));
    }

    @Test
    void shouldThrowCarNotFoundExceptionBecauseCarWithSuchIdDoesNotExistWhileTryingToGetACar() {
        Long carId = 1L;
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> underTest.getCarById(carId));
    }

    @Test
    void shouldThrowInvalidInputExceptionForNegativeOrZeroIdWhileTryingToGetACar() {
        Long carId = -1L;
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> underTest.getCarById(carId));
    }

    @Test
    void shouldDeleteCarById() {
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        underTest.deleteCar(carId);
        verify(carRepository).deleteById(carId);
    }

    @Test
    void shouldThrowCarNotFoundExceptionBecauseCarWithSuchIdDoesNotExistWhileTryingToDeleteACar() {
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> underTest.deleteCar(carId));
    }

    @Test
    void testUpdateCar_validUpdate() {
        Long id = 1L;
        Car existingCar = new Car("Toyota", "Red");
        Car updatedCar = new Car("Honda", "Blue");

        when(carRepository.findById(id)).thenReturn(Optional.of(existingCar));

        underTest.updateCar(id, updatedCar);

        verify(carRepository).save(existingCar);

        assertEquals("Honda", existingCar.getBrand());
        assertEquals("Blue", existingCar.getColor());
    }



    @Test
    void testUpdateCar_carNotFound() {
        Long id = 1L;
        Car updatedCar = new Car("Honda", "Blue");
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> underTest.updateCar(id, updatedCar));
    }

    @Test
    void testUpdateCar_emptyInput() {
        Long id = 1L;
        Car existingCar = new Car("Toyota", "Red");
        Car updatedCar = new Car("", "");
        when(carRepository.findById(id)).thenReturn(Optional.of(existingCar));

        assertThrows(EmptyInputException.class, () -> underTest.updateCar(id, updatedCar));
    }

    @Test
    void shouldNotUpdateCarWhenBrandIsEmpty() {
        Car existingCar = new Car("Tesla", "Midnight blue");
        Car updatedCar = new Car("", "Black");
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }

    @Test
    void shouldNotUpdateCarWhenColorIsEmpty() {
        Car existingCar = new Car("Tesla", "Midnight blue");
        Car updatedCar = new Car("BMW", "");
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }

    @Test
    void shouldNotUpdateCarWhenBrandIsNull() {
        Car existingCar = new Car("Tesla", "Midnight blue");
        Car updatedCar = new Car(null, "Black");
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }

    @Test
    void shouldNotUpdateCarWhenColorIsNull() {
        Car existingCar = new Car("Tesla", "Midnight blue");
        Car updatedCar = new Car("BMW", null);
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }

    @Test
    void shouldNotUpdateCarWhenColorAndBrandAreNull() {
        Car existingCar = new Car("Tesla", "Midnight blue");
        Car updatedCar = new Car(null, null);
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }
}