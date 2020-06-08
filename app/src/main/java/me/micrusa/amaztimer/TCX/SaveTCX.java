package me.micrusa.amaztimer.TCX;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import me.micrusa.amaztimer.TCX.data.Lap;
import me.micrusa.amaztimer.TCX.data.TCXData;
import me.micrusa.amaztimer.TCX.data.Trackpoint;

public class SaveTCX {

    private String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "AmazTimer/";

    private String data;

    private void addToFile(String addData){
        this.data = data + addData;
    }

    public void saveToFile(Context paramContext, TCXData TCXData){
        ArrayList<Lap> laps = TCXData.getLaps();
        this.data = "";
        addToFile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"
                + "<TrainingCenterDatabase xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/ActivityExtension/v2 http://www.garmin.com/xmlschemas/ActivityExtensionv2.xsd http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\">");
        addToFile("<Activities>"
                + "<Activity Sport=\"" + TCXData.getSportName() + "\">"
                + "<id>" + TCXData.getTime() + "<id>");
        for (Lap lap : laps){
            if(!lap.isLapEmpty())
                fillLap(lap);
        }
        //End of TCX file
        addToFile("</Activity>"
                + "</Activities>"
                + "</TrainingCenterDatabase>");

        //Save TCX to file
        File file = new File(FILE_PATH, TCXData.getTime() + ".tcx");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillLap(Lap lap){
        addToFile("<Lap StartTime=\"" + lap.getStartTime() + "\">"
                + "<TotalTimeSeconds>" + lap.getTimeInSeconds() + "</TotalTimeSeconds>");
        //Kcal
        if(lap.getKcal() != 0)
            addToFile("<Calories>" + lap.getKcal() + "</Calories>");
        //Avg and max hr
        addToFile("<AverageHeartRateBpm xsi:type=\"HeartRateInBeatsPerMinute_t\">"
                + "<Value>" + lap.getAvgHr() + "</Value>"
                + "</AverageHeartRateBpm>");
        addToFile("<MaximumHeartRateBpm xsi:type=\"HeartRateInBeatsPerMinute_t\">"
                + "<Value>" + lap.getMaxHr() + "</Value>"
                + "</MaximumHeartRateBpm>");
        //Intensity (Active/Resting)
        addToFile("<Intensity>" + lap.getIntensity() + "</Intensity>");
        //Trigger method (Time)
        addToFile("<TriggerMethod>" + "Time" + "</TriggerMethod>");
        //Trackpoints
        addToFile("<Track>");
        for (Trackpoint tp : lap.getTrackpoints())
            fillTrackPoint(tp);
        addToFile("</Track>");
        //Lap end
        addToFile("</Lap>");
    }

    private void fillTrackPoint(Trackpoint tp){
        addToFile("<Trackpoint>"
                //Time
                + "<Time>" + tp.getTime() + "</Time>"
                //HR
                + "<HeartRateBpm xsi:type=\"HeartRateInBeatsPerMinute_t\">"
                + "<Value>" + tp.getHr() + "</Value>"
                + "</HeartRateBpm>"
                //Trackpoint end
                + "</Trackpoint>");
    }


}
