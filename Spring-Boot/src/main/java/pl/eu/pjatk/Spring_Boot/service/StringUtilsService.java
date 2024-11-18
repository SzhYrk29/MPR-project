package pl.eu.pjatk.Spring_Boot.service;

import org.springframework.stereotype.Service;

@Service
public class StringUtilsService {
    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public String toLowerCaseExceptFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }
}