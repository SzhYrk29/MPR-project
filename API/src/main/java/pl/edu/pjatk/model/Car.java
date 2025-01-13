package pl.edu.pjatk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand;
    private String color;
    private int identifier;

    public Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }

    public void setIdentifier() {
        this.identifier = 0;
        String brandAndColor = this.brand + this.color;
        for (int i = 0; i < brandAndColor.length(); i++) {
            this.identifier += brandAndColor.charAt(i);
        }
    }
}
