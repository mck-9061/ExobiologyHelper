package me.mck9061;

import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EdJournal {
    public String fileLocation;
    public File file;
    public List<String> processedEvents;

    public EdJournal(String fileLocation) {
        processedEvents = new ArrayList<>();
        this.fileLocation = fileLocation;
        file = new File(fileLocation);
    }

    List<Planet> planets = new ArrayList<>();

    public void findNext() throws FileNotFoundException {
        // Search for a FSSBodySignals event
        // Find the Scan event afterwards and set up a Planet object
        // Start comparing specimens
        // Find the SAASignalsFound event
        // Narrow search

        boolean anything = false;

        Scanner scanner = new Scanner(file);
        String bodyName = "";


        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            JSONObject json = new JSONObject(line);
            String event = json.getString("event");

            if (processedEvents.contains(line)) continue;

            if (event.equals("FSSBodySignals") && ((JSONObject) json.getJSONArray("Signals").get(0)).getString("Type_Localised").equals("Biological")) {
                processedEvents.add(line);
                bodyName = json.getString("BodyName");
                System.out.println(((JSONObject) json.getJSONArray("Signals").get(0)).getInt("Count") + " bio sign(s) on " + bodyName);
                anything = true;
            }

            if (event.equals("Scan") && json.getString("BodyName").equals(bodyName)) {
                processedEvents.add(line);
                Planet planet = new Planet();
                planet.name = bodyName;
                planet.distance = (int) json.getFloat("DistanceFromArrivalLS");
                planet.planetClass = json.getString("PlanetClass");
                planet.atmosphere = json.getString("Atmosphere");
                planet.volcanism = json.getString("Volcanism");
                planet.gravity = json.getFloat("SurfaceGravity");
                planet.temperature = (int) json.getFloat("SurfaceTemperature");
                planets.add(planet);
                //System.out.println("Planet added");
                System.out.println(planet.atmosphere);
                anything = true;
            }

            if (event.equals("SAASignalsFound")) {
                processedEvents.add(line);

                for (Planet planet : planets) {
                    List<String> genuses = new ArrayList<>();
                    if (planet.name.equals(json.getString("BodyName"))) {
                        System.out.println(planet.name);
                        anything = true;
                        for (Object object : json.getJSONArray("Genuses")) {
                            JSONObject obj = (JSONObject) object;

                            System.out.println(obj.getString("Genus_Localised"));
                            anything = true;
                            genuses.add(obj.getString("Genus_Localised"));
                        }
                        System.out.println();
                        anything = true;
                    }

                    for (Specimen specimen : Main.specimenList) {
                        for (String genus : genuses) {
                            if (specimen.genus.equals(genus) && specimen.doesMatch(planet)) {
                                System.out.println(specimen.genus + " " + specimen.species + ": " + NumberFormat.getIntegerInstance().format(specimen.value) + " or " + NumberFormat.getIntegerInstance().format(specimen.value * 4));
                            }
                        }
                    }

                    //System.out.println();
                }
            }
        }

        if (!anything) {
            System.out.println("Nothing found");
        }
    }
}
