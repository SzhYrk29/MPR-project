package pl.eu.pjatk.Spring_Boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.eu.pjatk.Spring_Boot.exception.CarNotFoundException;
import pl.eu.pjatk.Spring_Boot.exception.EmptyInputException;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.repository.CarRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CarServiceTest {

    private CarRepository carRepository;
    private StringUtilsService stringUtilsService;
    private CarService underTest;

    @BeforeEach
    void setUp() {
        this.carRepository = Mockito.mock(CarRepository.class);
        this.stringUtilsService = Mockito.mock(StringUtilsService.class);
        this.underTest = new CarService(carRepository, stringUtilsService);

        Mockito.when(stringUtilsService.toUpperCase(Mockito.anyString()))
                .thenAnswer(invocation -> ((String) invocation.getArgument(0)).toUpperCase());
    }

    @Test
    void getAllCars() {
        underTest.getCars();
        verify(carRepository).findAll();
    }

    @Test
    void addCar() {
        Car car = new Car("Toyota", "Blue");
        underTest.addCar(car);
        verify(carRepository).save(car);
    }

    @Test
    void addCarButInAFancyWay() {
        Car car = new Car("Toyota", "Blue");

        Mockito.when(stringUtilsService.toUpperCase("Toyota")).thenReturn("TOYOTA");
        Mockito.when(stringUtilsService.toUpperCase("Blue")).thenReturn("BLUE");

        underTest.addCar(car);

        ArgumentCaptor<Car> carArgumentCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository, times(1)).save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar.getBrand()).isEqualTo("TOYOTA");
        assertThat(capturedCar.getColor()).isEqualTo("BLUE");
    }

    @Test
    void cannotAddCarBecauseOfAnEmptyInput() {
        Car car = new Car("", "");
        assertThrows(EmptyInputException.class, () -> underTest.addCar(car));
    }

    @Test
    void getCarByBrand() {
        Car car1 = new Car("TOYOTA", "BLUE");
        Car car2 = new Car("TOYOTA", "WHITE");
        String brand = "TOYOTA";
        List<Car> cars = Arrays.asList(car1, car2);

        Mockito.when(carRepository.findByBrand(brand)).thenReturn(cars);
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("TOYOTA")).thenReturn("Toyota");
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("BLUE")).thenReturn("Blue");

        List<Car> result = underTest.getCarByBrand(brand);

        assertThat(result.get(0).getBrand()).isEqualTo("Toyota");
        assertThat(result.get(1).getBrand()).isEqualTo("Toyota");
    }

    @Test
    void cannotGetCarByBrandBecauseBrandDoesNotExist() {
        String brand = "TOYOTA";
        assertThrows(CarNotFoundException.class, () -> underTest.getCarByBrand(brand));
    }

    @Test
    void getCarByColor() {
        Car car1 = new Car("TOYOTA", "BLUE");
        Car car2 = new Car("TOYOTA", "WHITE");
        String color1 = "BLUE";
        String color2 = "WHITE";
        List<Car> cars = Arrays.asList(car1, car2);

        Mockito.when(carRepository.findByColor(color1)).thenReturn(cars);
        Mockito.when(carRepository.findByColor(color2)).thenReturn(cars);
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("TOYOTA")).thenReturn("Toyota");
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("BLUE")).thenReturn("Blue");
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("WHITE")).thenReturn("White");

        List<Car> result1 = underTest.getCarByColor(color1);
        assertThat(result1.get(0).getColor()).isEqualTo("Blue");
        List<Car> result2 = underTest.getCarByColor(color2);
        assertThat(result2.get(1).getColor()).isEqualTo("White");
    }

    @Test
    void cannotGetCarByColorBecauseColorDoesNotExist() {
        String color = "PINK";
        assertThrows(CarNotFoundException.class, () -> underTest.getCarByColor(color));
    }

    @Test
    void getCarById() {
        Car car = new Car("TOYOTA", "BLUE");
        Long carId = 1L;
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("TOYOTA")).thenReturn("Toyota");
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("BLUE")).thenReturn("Blue");

        Car result = underTest.getCar(carId);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("Toyota");
        assertThat(result.getColor()).isEqualTo("Blue");
        verify(carRepository).findById(carId);
    }

    @Test
    void cannotGetCarByIdBecauseIdWasNotFound() {
        Long carId = 1L;
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> underTest.getCar(carId));
    }

    @Test
    void deleteCarById() {
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        underTest.deleteCar(carId);
        verify(carRepository).deleteById(carId);
    }

    @Test
    void cannotDeleteCarByIdBecauseIdWasNotFound() {
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> underTest.deleteCar(carId));
    }

    @Test
    void updateCarById() {
        Long carId = 1L;
        Car existingCar = new Car("Toyota", "blue");
        Car updatedCar = new Car("Ford", "green");

        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        Mockito.when(carRepository.save(Mockito.any(Car.class))).thenReturn(updatedCar);

        Mockito.when(stringUtilsService.toUpperCase("Ford")).thenReturn("FORD");
        Mockito.when(stringUtilsService.toUpperCase("green")).thenReturn("GREEN");

        underTest.updateCar(carId, updatedCar);

        assertThat(existingCar.getBrand()).isEqualTo("FORD");
        assertThat(existingCar.getColor()).isEqualTo("GREEN");
        Mockito.verify(carRepository).save(existingCar);
    }

    @Test
    void cannotUpdateCarBecauseIdWasNotFound() {
        Car car = new Car();
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> underTest.updateCar(carId, car));
    }

    @Test
    void cannotUpdateCarBecauseOfAnEmptyInput() {
        Car existingCar = new Car("Toyota", "blue");
        Car updatedCar = new Car("", "");
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
    }
}