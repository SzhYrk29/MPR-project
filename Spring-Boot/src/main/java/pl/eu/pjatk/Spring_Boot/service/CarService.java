package pl.eu.pjatk.Spring_Boot.service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.eu.pjatk.Spring_Boot.exception.CarNotFoundException;
import pl.eu.pjatk.Spring_Boot.exception.EmptyInputException;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.repository.CarRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private CarRepository repository;
    private StringUtilsService stringUtilsService;

    @Autowired
    public CarService(CarRepository repository, StringUtilsService stringUtilsService, CarRepository carRepository) {
        this.repository = repository;
        this.stringUtilsService = stringUtilsService;
        this.carRepository = carRepository;
    }

    @PostConstruct
    public void init() {
        if (this.repository.count() == 0) {
            this.repository.save(new Car("Tesla", "white"));
            this.repository.save(new Car("Lamborghini", "red"));
            this.repository.save(new Car("BMW", "black"));
            this.repository.save(new Car("Porsche", "purple"));
            this.repository.save(new Car("Audi", "midnight blue"));
        }
    }

    public List<Car> getCarByBrand (String brand) {
        List<Car> cars = this.repository.findByBrand(brand);
        throwCarNotFoundException(cars);
        return getSortedCars(cars);
    }

    public List<Car> getCarByColor (String color) {
        List<Car> cars = this.repository.findByColor(color);
        throwCarNotFoundException(cars);
        return getSortedCars(cars);
    }

    public Iterable<Car> getCars() {
        List<Car> cars = (List<Car>) this.repository.findAll();
        return getSortedCars(cars);
    }

    public Car getCar(Long id) {
        Optional<Car> optionalCar = this.repository.findById(id);
        if (optionalCar.isEmpty() || id <= 0) {
            throw new CarNotFoundException();
        } else {
            Car car = optionalCar.get();
            car.setBrand(this.stringUtilsService.toLowerCaseExceptFirstLetter(car.getBrand()));
            car.setColor(this.stringUtilsService.toLowerCaseExceptFirstLetter(car.getColor()));
            return car;
        }
    }

    public void addCar(Car car) {
        car.setBrand(this.stringUtilsService.toUpperCase(car.getBrand()));
        car.setColor(this.stringUtilsService.toUpperCase(car.getColor()));

        if (car.getBrand().isEmpty() || car.getColor().isEmpty()) {
            throw new EmptyInputException();
        }

        this.repository.save(car);
    }

    public void deleteCar(Long id) {
        boolean exists = this.repository.existsById(id);
        if (!exists) {
            throw new CarNotFoundException();
        }
        this.repository.deleteById(id);
    }

    public void updateCar(Long id, Car car) {
        Car existingCar = this.repository.findById(id).orElseThrow(CarNotFoundException::new);
        existingCar.setBrand(this.stringUtilsService.toUpperCase(car.getBrand()));
        existingCar.setColor(this.stringUtilsService.toUpperCase(car.getColor()));

        if (existingCar.getBrand().isEmpty() || car.getColor().isEmpty()) {
            throw new EmptyInputException();
        }

        existingCar.setIdentifier();
        this.repository.save(existingCar);
    }

    public void getPdf(Long id, HttpServletResponse response) throws IOException {
        Optional<Car> cars = carRepository.findById(id);

         response.setContentType("application/pdf");
         response.setHeader("Content-Disposition", "attachment; filename=cars.pdf" + id + ".pdf");

         PDDocument document = new PDDocument();

         PDPage page = new PDPage();
         document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 750);
        contentStream.showText("Info about car with id " + cars.get().getId());
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Car ID   |   Brand   |   Color   |   Identifier");
        contentStream.newLineAtOffset(0, -15);

        cars.ifPresent(carList -> {
            try {
                        contentStream.showText(
                                cars.get().getId() + "   |   " +
                                        cars.get().getBrand() + "   |   " +
                                        cars.get().getColor() + "   |   " +
                                        cars.get().getIdentifier()
                        );
                        contentStream.newLineAtOffset(0, -15);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        contentStream.endText();
        contentStream.close();

        document.save(response.getOutputStream());
        document.close();
    }

    private List<Car> getSortedCars(List<Car> cars) {
        return cars.stream()
                .map(car -> {
                    Car sortedCar = new Car();
                    sortedCar.setId(car.getId());
                    sortedCar.setBrand(this.stringUtilsService.toLowerCaseExceptFirstLetter(car.getBrand()));
                    sortedCar.setColor(this.stringUtilsService.toLowerCaseExceptFirstLetter(car.getColor()));
                    return sortedCar;
                })
                .collect(Collectors.toList());
    }

    private void throwCarNotFoundException(List<Car> cars) {
        if (cars.isEmpty()) {
            throw new CarNotFoundException();
        }
    }
}