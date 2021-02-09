package sk.kosickaakademmia.stovcikova.mysql;

import sk.kosickaakademmia.stovcikova.mysql.entity.City;
import sk.kosickaakademmia.stovcikova.mysql.entity.Country;
import sk.kosickaakademmia.stovcikova.mysql.entity.CapitalCity;
import sk.kosickaakademmia.stovcikova.mysql.entity.Monument;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url = "jdbc:mysql://itsovy.sk:3306/world_x";
    private String username = " username"; //na git neukladať
    private String password = "Kosice2021!"; //na git neukladat

    public List<City> getCities(String country) {
        String query = "SELECT city.Name, JSON_EXTRACT(Info,'$.Population') AS Population " +
                "FROM city " +
                "INNER JOIN country ON country.code = city.CountryCode " +
                "WHERE country.name LIKE ? ORDER BY Population DESC";
        List<City> list = new ArrayList<City>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                System.out.println("Success");
                PreparedStatement ps = conn.prepareStatement(query);
                //osetrenie otaznikom
                ps.setString(1, country);

                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String city = rs.getString("Name");
                    String code = rs.getString("CountryCode");
                    System.out.println(city + " " + code);
                    String name = rs.getString("Name");
                    //String code = rs.getString("CountryCode");
                    //System.out.println(city +" "+ code);
                    int pop = rs.getInt("Population");
                    //System.out.println(name + "(" + pop + ")");
                    City c = new City(name, pop);
                    list.add(c);
                }
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public Country getCountryInfo(String country) {
        String query = " SELECT country.name, country.code, city.name" +
                " JSON_UNQUOTE(JSON_EXTRACT(doc, '$.geography.Continent')) AS Continent, " +
                " JSON_EXTRACT(doc, '$.geography.SurfaceArea') AS Area" +
                " FROM country " +
                " INNER JOIN city ON country.Capital = city.ID" +
                " INNER JOIN countryinfo ON country.code = countryinfo._id " +
                " WHERE country.name like ?";
        Country countryInfo = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String code3 = rs.getString("country.code");
                String capitalCity = rs.getString("city.name");
                String continent = rs.getString("Continent");
                int area = rs.getInt("Area");
                //System.out.println(code3 + " " + capitalCity + " " + continent + " " + area);
                countryInfo = new Country(country, code3, capitalCity, area, continent);
            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return countryInfo;
    }

    public String getCountryCode(String name) {
        if (name == null || name.equalsIgnoreCase("")) ;
        return null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,username,password);
            String query = "SELECT code FROM country WHERE name LIKE ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        String code = rs.getString("code");
                        return code;
                    }
                con.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void updatePopulation(String country, String city, int population) {
        if (population <= 0) {
            System.out.println("Error: population can not be in the minus"); //populacia je zaporna
            return;
        }
        //overenie krajiny
        String query = "SELECT country.name AS Name1, city.name AS Name2 " +
                " FROM country " +
                " INNER JOIN city ON country.Code = city.CountryCode " +
                " WHERE country.name like ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                System.out.println("Success");
                PreparedStatement ps = conn.prepareStatement(query);
                //osetrenie otaznikom
                ps.setString(1, country);
                //System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String nameCountry = rs.getString("Name1");
                    String nameCity = rs.getString("Name2");
                    System.out.println(nameCountry + " " + nameCity);
                    if (nameCountry.equalsIgnoreCase(country) && nameCity.equalsIgnoreCase(city)) {
                        query = "UPDATE city SET Info = ? " +
                                " WHERE city.Name like ?";

                        ps = conn.prepareStatement(query);
                        String json = "{\"Population\":" + population + "}";
                        ps.setString(1, json);
                        ps.setString(2, city);

                        int result = ps.executeUpdate();
                        System.out.println("Result" + result);

                        conn.close();
                        return;
                    }

                }
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("I am sorry but you wrote it wrong!!!");
    }
    public List<CapitalCity> getCapitalCities(String continent){
        List<CapitalCity> = new ArrayList<>();
        String query = "SELECT JSON_EXTRACT(doc,'$.geography.Continent') AS Continent, " +
                " JSON_EXTRACT(doc,'$.Name') AS Name, " +
                " JSON_EXTRACT(doc,'$.demographics.Population') AS Population, " +
                " city.Name AS Capital " +
                " FROM countryinfo " +
                " INNER JOIN city ON city.CountryCode = JSON_UNQUOTE(JSON_EXTRACT(doc,'$._id')) "+
                " INNER JOIN country ON country.Capital = city.ID "+
                " WHERE JSON_UNQUOTE(JSON_EXTRACT(doc,'$.geography.Continent')) like ?";
        List<CapitalCity> list;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            if (con != null){
                System.out.println("Success");
                PreparedStatement ps = con.prepareStatement(query);
                //osetrenie otaznikom
                ps.setString(1, continent);

                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while(rs.next()){
                    String name = rs.getString("Name");
                    String capital = rs.getString("Capital");
                    int pop = rs.getInt("Population");
                    CapitalCity capitalCity = new CapitalCity(name, capital,pop );
                    i++;
                    list.add(capitalCity);
                }
                System.out.println("I have found: " + i + " countries.");
                con.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    public boolean insertNewMonument(String code3, String city, String name){
        //kontrola
        String query = "SELECT city.Name AS Name, city.CountryCode AS CountryCode, city.ID AS ID " +
                " FROM city " +
                " WHERE city.name like ? AND city.CountryCode like ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            if(con != null){
                System.out.println("Success");
                PreparedStatement ps = con.prepareStatement(query);
                //osetrenie otaynika
                ps.setString(1, city);
                ps.setString(2, code3);
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String nameCity = rs.getString("Name");
                    String code = rs.getString("CountryCode");
                    int cityID = rs.getInt("ID");
                    //System.out.println(nameCountry + " " + nameCity);

                    if(nameCity.equalsIgnoreCase(city) && code.equalsIgnoreCase(code3)){
                        query = "INSERT INTO monument (name, city) VALUES (?, ?)";

                        ps = con.prepareStatement(query);
                        ps.setString(1, name);
                        ps.setInt(2, cityID);

                        int result = ps.executeUpdate();
                        System.out.println("Result" + result);

                        con.close();
                        return true;
                    }

                }
                con.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("I am sorry but you wrote it wrong");
        return false;
    }

    public List<Monument> getMonuments(){
        List<Monument> list = new ArrayList<>();
        String query = "SELECT country.name AS CountryName, city.name AS CityName, monument.name AS MonumentName, monument.id AS MonumentID " +
                " FROM monument " +
                " INNER JOIN city ON city.ID = monument.city" +
                " INNER JOIN country ON country.code = city.CountryCode ";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            if(con != null){
                System.out.println("Success");
                PreparedStatement ps = con.prepareStatement(query);
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    String countryName = rs.getString("CountryName");
                    String cityName = rs.getString("CityName");
                    String monumentName = rs.getString("MonumentName");
                    int idMonument = rs.getInt("MonumentID");
                    Monument monument = new Monument(countryName, cityName, monumentName, idMonument);
                    list.add(monument);
                }
                con.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}