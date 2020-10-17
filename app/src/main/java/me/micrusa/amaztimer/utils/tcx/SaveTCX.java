/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.utils.tcx;

import android.os.Environment;

import org.tinylog.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import me.micrusa.amaztimer.utils.tcx.data.Lap;
import me.micrusa.amaztimer.utils.tcx.data.TCXData;
import me.micrusa.amaztimer.utils.tcx.data.Trackpoint;
import me.micrusa.amaztimer.utils.SystemProperties;

public class SaveTCX {

    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/AmazTimer/";

    private static Element Activity;
    private static Document tcx;


    public static boolean saveToFile(TCXData tcxData){
        if(!performChecks(tcxData))
            return false;
        File tcxFile = new File(FILE_PATH + "AmazTimer" + tcxData.getTime().replaceAll(":", "-").replace("T", "_").replace("Z", "") + ".tcx");

        try {
            Element root = createRoot();

            //Folders
            Element Folders = createElement(root, "Folders", null);
            Element History = createElement(Folders, "History", null);

            createElement(History, "Running", "Name", "Running");
            createElement(History, "Biking", "Name", "Biking");

            Element Other = createElement(History, "Other", "Name", "Other");
            Element ActivityRef = createElement(Other, "ActivityRef", null);
            createElement(ActivityRef, "Id", tcxData.getTime());

            createElement(History, "MultiSport", "Name", "MultiSport");

            Element Activities = createElement(root, "Activities", null);
            Activity = createElement(Activities, "Activity", "Sport", "Other");
            createElement(Activity, "Id", tcxData.getTime());

            //Fill all laps
            for (Lap lap : tcxData.getLaps()) {
                fillLap(lap);
            }

            fillCreator(Activity);
            fillAuthor(root);

            saveResults(tcxFile);
        } catch (ParserConfigurationException | TransformerException | IOException ex) {
            Logger.error(ex);
            return false;
        }
        return tcxFile.exists();
    }

    private static boolean performChecks(TCXData tcxData){
        if (tcxData.isEmpty()){
            Logger.debug("TCX Data empty, returning...");
            return false;
        }
        if (!new File(FILE_PATH).exists())
            new File(FILE_PATH).mkdirs();

        return true;
    }

    private static void saveResults(File tcxFile) throws TransformerException, IOException{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(tcx);
        StreamResult streamResult = new StreamResult(tcxFile);

        transformer.transform(domSource, streamResult);

        if(streamResult.getOutputStream() != null)
            streamResult.getOutputStream().close();
    }

    private static void fillLap(Lap lap){
        Element Lap = createElement(Activity, "Lap", "StartTime", lap.getStartTime());

        createElement(Lap, "TotalTimeSeconds", String.valueOf(lap.getTimeInSeconds()));
        createElement(Lap, "Calories", String.valueOf(lap.getKcal()));

        Element AverageHeartRateBpm = createElement(Lap, "AverageHeartRateBpm", "xsi:type", "HeartRateInBeatsPerMinute_t");
        createElement(AverageHeartRateBpm, "Value", String.valueOf(lap.getAvgHr()));

        Element MaxHeartRateBpm = createElement(Lap, "MaximumHeartRateBpm", "xsi:type", "HeartRateInBeatsPerMinute_t");
        createElement(MaxHeartRateBpm, "Value", String.valueOf(lap.getMaxHr()));

        createElement(Lap, "Intensity", lap.getIntensity());
        createElement(Lap, "TriggerMethod", "Manual");

        Element Track = createElement(Lap, "Track", null);
        for (Trackpoint tp : lap.getTrackpoints())
            fillTrackPoint(tp, Track);
    }

    private static void fillTrackPoint(Trackpoint tp, Element Track){
        Element Trackpoint = createElement(Track, "Trackpoint", null);
        createElement(Trackpoint, "Time", tp.getTime());

        Element HeartRateBpm = createElement(Trackpoint, "HeartRateBpm", "xsi:type", "HeartRateInBeatsPerMinute_t");
        createElement(HeartRateBpm, "Value", String.valueOf(tp.getHr()));
        createElement(Trackpoint, "SensorState", "Absent");
    }

    private static void fillCreator(Element Activity){
        Element Creator = createElement(Activity, "Creator", "xsi:type", "Device_t");
        createElement(Creator, "Name", SystemProperties.getDeviceName());
        createElement(Creator, "UnitId", "1111111111");
        createElement(Creator, "ProductID", "450");
        Element Version = createElement(Creator, "Version", null);
        createElement(Version, "VersionMajor", "2");
        createElement(Version, "VersionMinor", "90");
        createElement(Version, "BuildMajor", "0");
        createElement(Version, "BuildMinor", "0");
    }

    private static void fillAuthor(Element root){
        Element Author = createElement(root, "Author", "xsi:type", "Application_t");
        createElement(Author, "Name", "Garmin Training Center(TM)");
        Element Build = createElement(Author, "Build", null);
        Element aVersion = createElement(Build, "Version", null);
        createElement(aVersion, "VersionMajor", "3");
        createElement(aVersion, "VersionMinor", "2");
        createElement(aVersion, "BuildMajor", "3");
        createElement(aVersion, "BuildMinor", "0");
        createElement(Build, "Type", "Release");
        createElement(Build, "Time", "Mar 15 2007, 12:31:45");
        createElement(Build, "Builder", "SQA");
        createElement(Author, "LangID", "EN");
        createElement(Author, "PartNumber", "006-A0119-00");
    }

    private static Element createRoot() throws ParserConfigurationException{
        DocumentBuilderFactory tcxFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder tcxBuilder = tcxFactory.newDocumentBuilder();
        tcx = tcxBuilder.newDocument();
        tcx.setXmlStandalone(false);

        Element root = tcx.createElement("TrainingCenterDatabase");
        tcx.appendChild(root);
        //Main attributes
        root.setAttribute("xmlns", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd");
        return root;
    }

    private static Element createElement(Element root, String elementName, String value){
        Element e = tcx.createElement(elementName);
        if(value != null) e.appendChild(tcx.createTextNode(value));
        root.appendChild(e);
        return e;
    }

    private static Element createElement(Element root, String elementName, String attrName, String attrValue){
        Element e = tcx.createElement(elementName);
        if(attrName != null && attrValue != null) e.setAttribute(attrName, attrValue);
        root.appendChild(e);
        return e;
    }


}
