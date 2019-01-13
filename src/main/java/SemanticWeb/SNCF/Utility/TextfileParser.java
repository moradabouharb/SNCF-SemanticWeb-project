package SemanticWeb.SNCF.Utility;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class TextfileParser {

    public List<List<String>> FileRoutesParser()
    {
        List<String> routeid = new java.util.ArrayList<String>();
        List<String> route_long_name = new java.util.ArrayList<String>();
        List<List<String>> routes_file = new java.util.ArrayList<List<String>>();
        String fileRoute = "files/routes.txt";
        int i = 0;
        try{
            FileReader file = new FileReader(fileRoute);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] retval = line.split(",");
                routeid.add(retval[0]);
                route_long_name.add(retval[3]);
                i = i+1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        routes_file.add(routeid);
        routes_file.add(route_long_name);
        return routes_file;
    }

    public List<List<String>> FileTripParser()
    {
        List<String> routeid = new java.util.ArrayList<String>();
        List<String> TripID = new java.util.ArrayList<String>();
        List<List<String>> Trip_file = new java.util.ArrayList<List<String>>();
        String fileRoute = "files/trips.txt";
        int i = 0;
        try{
            FileReader file = new FileReader(fileRoute);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] retval = line.split(",");
                routeid.add(retval[0]);
                TripID.add(retval[2]);
                i = i+1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Trip_file.add(routeid);
        Trip_file.add(TripID);
        return Trip_file;
    }

    public List<List<String>> FileStopsParser()
    {
        List<String> stopid = new java.util.ArrayList<String>();
        List<String> stopName = new java.util.ArrayList<String>();
        List<String> stop_lat = new java.util.ArrayList<String>();
        List<String> stop_long = new java.util.ArrayList<String>();
        List<List<String>> stops_file = new java.util.ArrayList<List<String>>();
        String fileRoute = "files/stops.txt";
        int i = 0;
        try{
            FileReader file = new FileReader(fileRoute);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] retval = line.split(",");
                stopid.add(retval[0]);
                stopName.add(retval[1]);
                stop_lat.add(retval[3]);
                stop_long.add(retval[4]);
                i = i+1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stops_file.add(stopid);
        stops_file.add(stopName);
        stops_file.add(stop_lat);
        stops_file.add(stop_long);
        return stops_file;
    }


    public List<List<String>> FileStoptimesParser()
    {
        List<String> tripid = new java.util.ArrayList<String>();
        List<String> arrivaltime = new java.util.ArrayList<String>();
        List<String> dep_time = new java.util.ArrayList<String>();
        List<String> stopid = new java.util.ArrayList<String>();
        List<List<String>> stoptimes_file = new java.util.ArrayList<List<String>>();
        String fileRoute = "files/stop_times.txt";
        int i = 0;
        try{
            FileReader file = new FileReader(fileRoute);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] retval = line.split(",");
                tripid.add(retval[0]);
                arrivaltime.add(retval[1]);
                dep_time.add(retval[2]);
                stopid.add(retval[3]);
                i = i+1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stoptimes_file.add(tripid);
        stoptimes_file.add(arrivaltime);
        stoptimes_file.add(dep_time);
        stoptimes_file.add(stopid);
        return stoptimes_file;
    }
}
