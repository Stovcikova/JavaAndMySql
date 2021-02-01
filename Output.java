package sk.kosickaakademmia.stovcikova.mysql.output;

import sk.kosickaakademmia.stovcikova.mysql.entity.City;
import sk.kosickaakademmia.stovcikova.mysql.entity.Country;

import java.util.List;

public class Output {
    public void printCities(List<City> list){
        System.out.println("List of cities:");
        for (City c: list){
            System.out.println(c.getName() + " " + c.getPopulation());
        }
    }

    public void printCountryInfo() {
        printCountryInfo();
    }

    public void printCountryInfo(Country country){
        if(country == null){
            System.out.println("Country does not exist");
        }else{
            System.out.println(country.getName() + " " + country.getCode3() +
                    " " + country.getCapitalCity() + " " + country.getArea());
        }

    }

}