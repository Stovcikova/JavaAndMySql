package sk.kosickaakademmia.stovcikova.mysql;

import sk.kosickaakademmia.stovcikova.mysql.entity.City;
import sk.kosickaakademmia.stovcikova.mysql.entity.Monument;
import sk.kosickaakademmia.stovcikova.mysql.entity.CapitalCity;
import sk.kosickaakademmia.stovcikova.mysql.output.Output;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        Output out= new Output();

        List<City> cities=database.getCities("Argentina");
        out.printCities(cities);

        List<CapitalCity> capitalCities;
        capitalCities = database.getCapitalCities("Europe");
        out.printCapitalCities(capitalCities);

        database.insertNewMonument("SVK","Humenne","Kastiel");
        List<Monument> list =database.getMonuments();
        out.printMonuments(list);
        
    }
}

