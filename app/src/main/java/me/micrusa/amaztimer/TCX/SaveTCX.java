package me.micrusa.amaztimer.TCX;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.micrusa.amaztimer.TCX.data.Lap;
import me.micrusa.amaztimer.TCX.data.TCXData;
import me.micrusa.amaztimer.TCX.data.Trackpoint;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.file;

public class SaveTCX {

    private String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/AmazTimer/";

    private Element Activity;
    private Document tcx;


    public boolean saveToFile(Context paramContext, TCXData TCXData){
        if (TCXData.isEmpty()){
            Log.i("AmazTimer: TCX: ", "TCXData is empty, returning!");
            return false;
        }
        if (!new File(FILE_PATH).exists())
            new File(FILE_PATH).mkdirs();

        File tcxFile = new File(FILE_PATH + "AmazTimer" + TCXData.getTime().replaceAll(":", "-").replace("T", "_").replace("Z", "") + ".tcx");

        ArrayList<Lap> laps = TCXData.getLaps();
        try {

            DocumentBuilderFactory tcxFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder tcxBuilder = tcxFactory.newDocumentBuilder();
            tcx = tcxBuilder.newDocument();
            tcx.setXmlStandalone(false);

            Element root = tcx.createElement("TrainingCenterDatabase");
            tcx.appendChild(root);
            //Attributes
            root.setAttribute("xmlns", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd");

            //Folders
            Element Folders = tcx.createElement("Folders");
            root.appendChild(Folders);
            Element History = tcx.createElement("History");
            Folders.appendChild(History);

            Element Running = tcx.createElement("Running");
            Running.setAttribute("Name", "Running");
            History.appendChild(Running);

            Element Biking = tcx.createElement("Biking");
            Biking.setAttribute("Name", "Biking");
            History.appendChild(Biking);

            Element Other = tcx.createElement("Other");
            Other.setAttribute("Name", "Other");
            History.appendChild(Other);
            Element ActivityRef = tcx.createElement("ActivityRef");
            Other.appendChild(ActivityRef);
            Element IdActivityRef = tcx.createElement("Id");
            IdActivityRef.appendChild(tcx.createTextNode(TCXData.getTime()));
            ActivityRef.appendChild(IdActivityRef);

            Element MultiSport = tcx.createElement("MultiSport");
            MultiSport.setAttribute("Name", "MultiSport");
            History.appendChild(MultiSport);

            Element Activities = tcx.createElement("Activities");
            root.appendChild(Activities);
            Activity = tcx.createElement("Activity");
            Activity.setAttribute("Sport", "Other");
            Activities.appendChild(Activity);

            Element Id = tcx.createElement("Id");
            Id.appendChild(tcx.createTextNode(TCXData.getTime()));
            Activity.appendChild(Id);

            //Fill all laps
            for (Lap lap : laps) {
                if (!lap.isLapEmpty())
                    fillLap(lap);
            }

            Element Creator = tcx.createElement("Creator");
            Creator.setAttribute("xsi:type", "Device_t");
            Activity.appendChild(Creator);
            Element Name = tcx.createElement("Name");
            Name.appendChild(tcx.createTextNode(SystemProperties.getDeviceName()));
            Creator.appendChild(Name);
            Element UnitId = tcx.createElement("UnitId");
            UnitId.appendChild(tcx.createTextNode("1111111111"));
            Creator.appendChild(UnitId);
            Element ProductID = tcx.createElement("ProductID");
            ProductID.appendChild(tcx.createTextNode("450"));
            Creator.appendChild(ProductID);
            Element Version = tcx.createElement("Version");
            Creator.appendChild(Version);
            Element VersionMajor = tcx.createElement("VersionMajor");
            VersionMajor.appendChild(tcx.createTextNode("2"));
            Version.appendChild(VersionMajor);
            Element VersionMinor = tcx.createElement("VersionMinor");
            VersionMinor.appendChild(tcx.createTextNode("90"));
            Version.appendChild(VersionMinor);
            Element BuildMajor = tcx.createElement("BuildMajor");
            BuildMajor.appendChild(tcx.createTextNode("0"));
            Version.appendChild(BuildMajor);
            Element BuildMinor = tcx.createElement("BuildMinor");
            BuildMinor.appendChild(tcx.createTextNode("0"));
            Version.appendChild(BuildMinor);

            Element Author = tcx.createElement("Author");
            Author.setAttribute("xsi:type", "Application_t");
            root.appendChild(Author);
            Element sName = tcx.createElement("Name");
            sName.appendChild(tcx.createTextNode("Garmin Training Center(TM)"));
            Author.appendChild(sName);
            Element Build = tcx.createElement("Build");
            Author.appendChild(Build);
            Element aVersion = tcx.createElement("Version");
            Build.appendChild(aVersion);
            Element aVersionMajor = tcx.createElement("VersionMajor");
            aVersionMajor.appendChild(tcx.createTextNode("3"));
            aVersion.appendChild(aVersionMajor);
            Element aVersionMinor = tcx.createElement("VersionMinor");
            aVersionMinor.appendChild(tcx.createTextNode("2"));
            aVersion.appendChild(aVersionMinor);
            Element aBuildMajor = tcx.createElement("BuildMajor");
            aBuildMajor.appendChild(tcx.createTextNode("3"));
            aVersion.appendChild(aBuildMajor);
            Element aBuildMinor = tcx.createElement("BuildMinor");
            aBuildMinor.appendChild(tcx.createTextNode("0"));
            aVersion.appendChild(aBuildMinor);
            Element Type = tcx.createElement("Type");
            Type.appendChild(tcx.createTextNode("Release"));
            Build.appendChild(Type);
            Element Time = tcx.createElement("Time");
            Time.appendChild(tcx.createTextNode("Mar 15 2007, 12:31:45"));
            Build.appendChild(Time);
            Element Builder = tcx.createElement("Builder");
            Builder.appendChild(tcx.createTextNode("SQA"));
            Build.appendChild(Builder);
            Element LangID = tcx.createElement("LangID");
            LangID.appendChild(tcx.createTextNode("EN"));
            Author.appendChild(LangID);
            Element PartNumber = tcx.createElement("PartNumber");
            PartNumber.appendChild(tcx.createTextNode("006-A0119-00"));
            Author.appendChild(PartNumber);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(tcx);
            StreamResult streamResult = new StreamResult(tcxFile);

            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
            return false;
        }
        return tcxFile.exists();
    }

    private void fillLap(Lap lap){
        Element Lap = tcx.createElement("Lap");
        Lap.setAttribute("StartTime", lap.getStartTime());
        Activity.appendChild(Lap);

        Element TotalTimeSeconds = tcx.createElement("TotalTimeSeconds");
        TotalTimeSeconds.appendChild(tcx.createTextNode(String.valueOf(lap.getTimeInSeconds())));
        Lap.appendChild(TotalTimeSeconds);

        Element Calories = tcx.createElement("Calories");
        Calories.appendChild(tcx.createTextNode(String.valueOf(lap.getKcal())));
        Lap.appendChild(Calories);

        Element AverageHeartRateBpm = tcx.createElement("AverageHeartRateBpm");
        AverageHeartRateBpm.setAttribute("xsi:type", "HeartRateInBeatsPerMinute_t");
        Lap.appendChild(AverageHeartRateBpm);
        Element AvgHrValue = tcx.createElement("Value");
        AvgHrValue.appendChild(tcx.createTextNode(String.valueOf(lap.getAvgHr())));
        AverageHeartRateBpm.appendChild(AvgHrValue);

        Element MaxHeartRateBpm = tcx.createElement("MaximumHeartRateBpm");
        MaxHeartRateBpm.setAttribute("xsi:type", "HeartRateInBeatsPerMinute_t");
        Lap.appendChild(MaxHeartRateBpm);
        Element MaxHrValue = tcx.createElement("Value");
        MaxHrValue.appendChild(tcx.createTextNode(String.valueOf(lap.getMaxHr())));
        MaxHeartRateBpm.appendChild(MaxHrValue);

        Element Intensity = tcx.createElement("Intensity");
        Intensity.appendChild(tcx.createTextNode(lap.getIntensity()));
        Lap.appendChild(Intensity);

        Element TriggerMethod = tcx.createElement("TriggerMethod");
        TriggerMethod.appendChild(tcx.createTextNode("Manual"));
        Lap.appendChild(TriggerMethod);

        Element Track = tcx.createElement("Track");
        Lap.appendChild(Track);
        for (Trackpoint tp : lap.getTrackpoints())
            fillTrackPoint(tp, Track);

    }

    private void fillTrackPoint(Trackpoint tp, Element Track){
        Element Trackpoint = tcx.createElement("Trackpoint");
        Track.appendChild(Trackpoint);

        Element Time = tcx.createElement("Time");
        Time.appendChild(tcx.createTextNode(tp.getTime()));
        Trackpoint.appendChild(Time);

        Element HeartRateBpm = tcx.createElement("HeartRateBpm");
        HeartRateBpm.setAttribute("xsi:type", "HeartRateInBeatsPerMinute_t");
        Trackpoint.appendChild(HeartRateBpm);
        Element hrValue = tcx.createElement("Value");
        hrValue.appendChild(tcx.createTextNode(String.valueOf(tp.getHr())));
        HeartRateBpm.appendChild(hrValue);

        Element SensorState = tcx.createElement("SensorState");
        SensorState.appendChild(tcx.createTextNode("Absent"));
        Trackpoint.appendChild(SensorState);

    }


}
