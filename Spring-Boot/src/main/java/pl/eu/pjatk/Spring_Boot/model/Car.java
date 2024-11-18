package pl.eu.pjatk.Spring_Boot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
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
        this.setIdentifier();
    }

    public Car() {}

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
        this.setIdentifier();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.setIdentifier();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier() {
        this.identifier = 0;
        String brandAndColor = this.brand + this.color;
        for (int i = 0; i < brandAndColor.length(); i++) {
            this.identifier += brandAndColor.charAt(i);
        }
    }
}