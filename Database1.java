package sk.kosickaakademmia.stovcikova.mysql;

import sk.kosickaakademmia.stovcikova.mysql.entity.City;
import sk.kosickaakademmia.stovcikova.mysql.entity.Country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url = "jdbc:mysql://itsovy.sk:3306/world_x";
    /*private String username = "---";
    private String password = "---";*/
    private String username = " username";
    private String password = "Kosice2021!";
    public List<City> getCities(String country){
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
                    while(rs.next()){
                        String city = rs.getString("Name");
                        String code = rs.getString("CountryCode");
                        System.out.println(city +" "+ code);
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
        public Country getCountryInfo(String country){
        String query = " SELECT country.name, country.code, city.name"+
                " JSON_UNQUOTE(JSON_EXTRACT(doc, '$.geography.Continent')) AS Continent, " +
                " JSON_EXTRACT(doc, '$.geography.SurfaceArea') AS Area"+
                " FROM country "+
                " INNER JOIN city ON country.Capital = city.ID"+
                " INNER JOIN countryinfo ON country.code = countryinfo._id "+
                " WHERE country.name like ?";
            Country countryInfo = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, country);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    String code3 = rs.getString("country.code");
                    String capitalCity = rs.getString("city.name");
                    String continent = rs.getString("Continent");
                    int area = rs.getInt("Area");
                    //System.out.println(code3 + " " + capitalCity + " " + continent + " " + area);
                    countryInfo = new Country(country, code3, capitalCity, area, continent);
                }

                conn.close();

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return countryInfo;
        }
    }
