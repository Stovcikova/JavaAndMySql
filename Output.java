package sk.kosickaakademmia.stovcikova.mysql.output;

import sk.kosickaakademmia.stovcikova.mysql.entity.CapitalCity;
import sk.kosickaakademmia.stovcikova.mysql.entity.City;
import sk.kosickaakademmia.stovcikova.mysql.entity.Country;
import sk.kosickaakademmia.stovcikova.mysql.entity.Monument;

import java.util.List;

public class Output {
    public void printCities(List<City> list){
        System.out.println("List of cities:");
        for (City city: list){
            System.out.println(city.getName() + " " + city.getPopulation());
        }
    }

       public void printCountryInfo(Country country){
        if(country == null){
            System.out.println("Country does not exist");
        }else{
            System.out.println(country.getName() + " " + country.getCode3() +
                    " " + country.getCapitalCity() + " " + country.getArea());
        }

    }
    public void printCapitalCities(List<CapitalCity> capitalCity){
        for(CapitalCity i: list){
            System.out.println(i.getNameCountry()+" "+ i.getNameCapitalCity()+" "+i.getCountOfCity());
        }
    }

    public void printMonuments( List<Monument> list ){
        for(Monument i: list){
            System.out.println("CityName: " + i.getCityName() + " CountryName: " + i.getCountryName()
                    + " Idmonumet: " + i.getIdMonument() + " MonumentName: " + i.getMonumentName());
        }
    }

}
