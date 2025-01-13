package pl.edu.pjatk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Car {
    private Long id;
    private String brand;
    private String color;
    private int identifier;

    public Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
        this.setIdentifier();
    }

    public void setIdentifier() {
        this.identifier = 0;
        String brandAndColor = this.brand + this.color;
        for (int i = 0; i < brandAndColor.length(); i++) {
            this.identifier += brandAndColor.charAt(i);
        }
    }
}
