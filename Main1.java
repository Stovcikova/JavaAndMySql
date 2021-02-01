package sk.kosickaakademmia.stovcikova.mysql;

import sk.kosickaakademmia.stovcikova.mysql.entity.Country;
import sk.kosickaakademmia.stovcikova.mysql.output.Output;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        Output out= new Output();
        String name = "Italy";
        Country country = database.getCountryInfo(name);
        out.printCountryInfo(country);
    }
}