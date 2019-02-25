package pl.vpplant.cezary.wykresy;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import java.sql.*;
import java.util.Scanner;

public class Chart {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connection connect = getConnection();

        ResultSet resultSet = getResultSet(connect);

        DefaultPieDataset dataset = getDefaultPieDataset(resultSet);
        System.out.println(dataset);

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWybierz czujniki z powyższych i wpisz 2 do porównania? ");

        String sensor = scanner.next();

        System.out.println("podałeś - "+ sensor);

        JFreeChart chart = getjFreeChart(dataset);

        //setVisible(chart);

//--------XYSeries--------------------------------------------------------------------------------------------------------

       final XYSeries CO2 = new XYSeries(sensor);
       int x=1;
       while(resultSet.next()){
           CO2.add(x++, Double.parseDouble(resultSet.getString("last_value")));

       }
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(CO2);

       XYLineChart_AWT wyk = new XYLineChart_AWT("Browser Usage Statistics",
               "Which Browser are you using?", dataset2 );
       wyk.pack();
       RefineryUtilities.centerFrameOnScreen(wyk);
       wyk.setVisible(true);
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

        //System.out.println(resultSet.getString("char_id")+" - "+resultSet.getString("last_value")+" - " +resultSet.getString("last_change"));
        }
        return dataset;
    }


    private static ResultSet getResultSet(Connection connect) throws SQLException {
        Statement statement = connect.createStatement();
        return statement.executeQuery("select * from data_series");
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


