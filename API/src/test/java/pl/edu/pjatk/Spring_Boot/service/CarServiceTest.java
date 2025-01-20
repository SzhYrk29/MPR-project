package pl.edu.pjatk.Spring_Boot.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.edu.pjatk.exception.CarNotFoundException;
import pl.edu.pjatk.exception.EmptyInputException;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.repository.CarRepository;
import pl.edu.pjatk.service.CarService;
import pl.edu.pjatk.service.StringUtilsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarServiceTest {
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
    void shouldReturnAListOfAllCars() {
        underTest.getCars();
        verify(carRepository).findAll();
    }

    @Test
    void shouldAddCarToTheRepository() {
        Car car = new Car("Toyota", "Blue");
        underTest.addCar(car);
        verify(carRepository).save(car);
    }

    @Test
    void shouldCaptureCarAndAddItToTheRepository() {
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

    @Disabled
    @Test
    void shouldReturnAListOfCarsSortedByBrand() {
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
    void shouldThrowCarNotFoundExceptionBecauseSuchBrandDoesNotExist() {
        String brand = "TOYOTA";
        assertThrows(CarNotFoundException.class, () -> underTest.getCarByBrand(brand));
    }

    @Disabled
    @Test
    void shouldReturnAListOfCarsSortedByColor() {
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
    void shouldThrowCarNotFoundExceptionBecauseSuchColorDoesNotExist() {
        String color = "PINK";
        assertThrows(CarNotFoundException.class, () -> underTest.getCarByColor(color));
    }

    @Test
    void shouldReturnACarById() {
        Car car = new Car("TOYOTA", "BLUE");
        Long carId = 1L;
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("TOYOTA")).thenReturn("Toyota");
        Mockito.when(stringUtilsService.toLowerCaseExceptFirstLetter("BLUE")).thenReturn("Blue");

        Car result = underTest.getCarById(carId);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("Toyota");
        assertThat(result.getColor()).isEqualTo("Blue");
        verify(carRepository).findById(carId);
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
    void shouldUpdateCarById() {
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
    void shouldThrowCarNotFoundExceptionBecauseCarWithSuchIdDoesNotExistWhileTryingToUpdateACar() {
        Car car = new Car();
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> underTest.updateCar(carId, car));
    }

    @Test
    void shouldThrowEmptyInputExceptionBecauseOfAnEmptyInputWhileTryingToUpdateACar() {
        Car existingCar = new Car("Toyota", "blue");
        Car updatedCar = new Car("", "");
        Long carId = 1L;
        Mockito.when(carRepository.existsById(carId)).thenReturn(true);
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        assertThrows(EmptyInputException.class, () -> underTest.updateCar(carId, updatedCar));
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

    @Test
    void shouldFetchCorrectCarFromRepository() throws IOException {
        Car car = new Car("Tesla", "Black");
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        MockHttpServletResponse response = new MockHttpServletResponse();

        underTest.getPdf(carId, response);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(carRepository, times(1)).findById(idCaptor.capture());

        Long capturedId = idCaptor.getValue();
        assertEquals(carId, capturedId);
    }

    @Test
    void shouldThrowInvalidInputExceptionForNegativeOrZeroIdWhileTryingToGetAPdf() {
        Long carId = -1L;
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(InvalidInputException.class, () -> underTest.getPdf(carId, response));
    }

    @Test
    void shouldThrowCarNotFoundExceptionForMissingCar() {
        Long carId = 1L;
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(carRepository.existsById(carId)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> underTest.getPdf(carId, response));
    }

    @Test
    void shouldSetResponseHeaders() throws IOException {
        Long carId = 1L;
        Car car = new Car("BMW", "Black");
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        MockHttpServletResponse response = new MockHttpServletResponse();

        underTest.getPdf(carId, response);

        assertEquals("application/pdf", response.getContentType());
        assertTrue(response.getHeader("Content-Disposition").contains("cars_" + carId + ".pdf"));
    }

//    @Test
//    void shouldInitializeCarsWhenRepositoryIsEmpty() {
//        when(carRepository.count()).thenReturn(0L);
//
//        underTest.init();
//
//        verify(carRepository).save(argThat(car -> car.getBrand().equals("Tesla") && car.getColor().equals("white")));
//        verify(carRepository).save(argThat(car -> car.getBrand().equals("Lamborghini") && car.getColor().equals("red")));
//        verify(carRepository).save(argThat(car -> car.getBrand().equals("BMW") && car.getColor().equals("black")));
//        verify(carRepository).save(argThat(car -> car.getBrand().equals("Porsche") && car.getColor().equals("purple")));
//        verify(carRepository).save(argThat(car -> car.getBrand().equals("Audi") && car.getColor().equals("midnight blue")));
//    }

//    @Test
//    void shouldNotInitializeCarsWhenRepositoryIsEmpty() {
//        when(carRepository.count()).thenReturn(5L);
//
//        underTest.init();
//
//        verify(carRepository, never()).save(any(Car.class));
//    }

//    @Test
//    void shouldGenerateCorrectPdfContent() throws IOException {
//        Long carId = 1L;
//        Car car = new Car("BMW", "Black");
//        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        underTest.getPdf(carId, response);
//
//        try (PDDocument document = PDDocument.load(response.getContentAsByteArray())) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            String pdfContent = pdfStripper.getText(document);
//
//            assertTrue(pdfContent.contains("Info about car with id " + carId));
//            assertTrue(pdfContent.contains("Car ID   |   Brand   |   Color   |   Identifier"));
//            assertTrue(pdfContent.contains("1   |   BMW   |   Black   |   null"));
//        }
//    }
}
