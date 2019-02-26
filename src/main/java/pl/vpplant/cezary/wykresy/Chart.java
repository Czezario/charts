package pl.vpplant.cezary.wykresy;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Chart {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connection connect = getConnection();

        ResultSet resultSet = getResultSet(connect);

        DefaultPieDataset dataset = getDefaultPieDataset(resultSet);

        JFreeChart chart = getjFreeChart(dataset);
        setVisible(chart);

    }

    private static void setVisible(JFreeChart chart) {
        ChartFrame chart1 = new ChartFrame("",chart );  //wyświetlenie na ekranie
        chart1.pack();
        chart1.setVisible(true);
    }


    private static JFreeChart getjFreeChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Dane z Sensorów",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                true);

        int width = 1600;    /* Width of the image */
        int height = 900;   /* Height of the image */
        return chart;
    }


    private static DefaultPieDataset getDefaultPieDataset(ResultSet resultSet) throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        while (resultSet.next()){
            dataset.setValue(
                    resultSet.getString("char_id"),
                    Double.parseDouble(resultSet.getString("last_value")));

        System.out.println(resultSet.getString("char_id")+" - "+resultSet.getString("last_value")+" - " +resultSet.getString("last_change"));

        }
        return dataset;
    }

    private static ResultSet getResultSet(Connection connect) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWybierz serię czujników - podaj liczbę (od 1 do 4) \n");

        Map<Integer, String> wybor = new HashMap<>();
        wybor.put(1, "temp");
        wybor.put(2,"CO2");
        wybor.put(3,"rh");
        wybor.put(4, "Wszystkie czujniki" );

        System.out.println(wybor);

        int liczba = scanner.nextInt();
        System.out.println("\n"+wybor.get(liczba));
        String sensor = wybor.get(liczba);

        System.out.println("podałeś - "+ sensor);

        Statement statement = connect.createStatement();
        switch (sensor) {
            case "temp":
                return statement.executeQuery("SELECT * FROM data_series WHERE char_id LIKE '%temp%'");
            case "CO2":
                return statement.executeQuery("SELECT * FROM data_series WHERE char_id LIKE '%CO2%'");
            case "rh":
                return statement.executeQuery("SELECT * FROM data_series WHERE char_id LIKE '%rh%'");
                default:
                    return statement.executeQuery("SELECT * FROM data_series");
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        /* Create MySQL Database Connection */
        Class.forName("org.postgresql.Driver");
        Connection connect = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/cezary",
                "postgres",
                "Cezary2019");
        System.out.println("Opened database successfully\n");
        return connect;
    }
}


