package me.mck9061;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Specimen {
    public String genus;
    public String species;
    public int value;
    public List<SpawnParameters> spawnParameters;

    public Specimen(File file) throws FileNotFoundException {
        spawnParameters = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        JSONObject json;
        String s = scanner.nextLine();
        // 10/10 way of dealing with the scunthorpe problem
        s = s.replace("ice", "icy").replace("Pumicy", "Pumice");
        json = new JSONObject(s);



        genus = json.getString("genus");
        species = json.getString("species");

        String atmosphere = json.getString("atmosphere");
        String bodyType = json.getString("bodyType");
        String temperature = json.getString("temperatureRange");

        List<String> atmospheres = new ArrayList<>();
        atmospheres.add(atmosphere);
        List<String> bodyTypes = new ArrayList<>();
        bodyTypes.add(bodyType);
        List<String> temperatures = new ArrayList<>();
        temperatures.add(temperature);

        if (atmosphere.contains(",")) {
            atmospheres = List.of(atmosphere.split(","));
        }
        if (bodyType.contains(",")) {
            bodyTypes = List.of(bodyType.split(","));
        }
        if (temperature.contains(",")) {
            temperatures = List.of(temperature.split(","));
        }

        // edge cases
        if (genus.equals("Fungoida") && species.equals("Setisis")) {
            atmospheres = new ArrayList<>();
            atmospheres.add("ammonia");
            bodyTypes = new ArrayList<>();
            bodyTypes.add("rocky");
            bodyTypes.add("hmc");

            SpawnParameters parameters = new SpawnParameters();
            parameters.atmosphere = "methane";
           parameters.bodyType = "any";
            if (!(json.getString("gravityLessThan").equals(""))) parameters.maxGravity = Float.parseFloat(json.getString("gravityLessThan"));
            if (!(json.getString("distanceHigherThan").equals(""))) parameters.minDistance = Integer.parseInt(json.getString("distanceHigherThan"));
            parameters.volcanism = json.getString("volcanism");
            if (parameters.volcanism.equals("")) parameters.volcanism = "any";

            spawnParameters.add(parameters);
        }


        int i = 0;
        for (String atmType : atmospheres) {
            int j = 0;
            for (String bdyType : bodyTypes) {
                SpawnParameters parameters = new SpawnParameters();

                parameters.atmosphere = atmType;
                if (atmType.equals("")) parameters.atmosphere = "any";
                parameters.bodyType = bdyType;
                if (bdyType.equals("")) parameters.bodyType = "any";
                if (!(json.getString("gravityLessThan").equals(""))) parameters.maxGravity = Float.parseFloat(json.getString("gravityLessThan"));
                if (!(json.getString("distanceHigherThan").equals(""))) parameters.minDistance = Integer.parseInt(json.getString("distanceHigherThan"));
                parameters.volcanism = json.getString("volcanism");
                if (parameters.volcanism.equals("")) parameters.volcanism = "any";

                String usedTemp = "";
                if (temperatures.size() < atmospheres.size()) {
                    usedTemp = temperatures.get(0);
                } else usedTemp = temperatures.get(i);

                if (usedTemp.equals("-")) parameters.minTemperature = 0;
                else if (usedTemp.contains(">")) parameters.minTemperature = Integer.parseInt(usedTemp.replace(">", ""));
                else if (usedTemp.contains("<")) parameters.maxTemperature = Integer.parseInt(usedTemp.replace("<", ""));
                else if (usedTemp.contains("-")) {
                    parameters.minTemperature = Integer.parseInt(usedTemp.split("-")[0]);
                    parameters.maxTemperature = Integer.parseInt(usedTemp.split("-")[1]);
                } else if (usedTemp.contains(":")) {
                    // only occurs once, so we can hardcode this absolutely shit solution
                    if (j == 1) parameters.maxTemperature = 190;
                }

                spawnParameters.add(parameters);

                j++;
            }
            i++;
        }
    }

    public void print() {
        System.out.print(genus + " ");
        System.out.print(species + " ");

        for (SpawnParameters parameters : spawnParameters) {
            parameters.print();
        }

        System.out.println();
    }

    public boolean doesMatch(Planet planet) {
        for (SpawnParameters parameters : spawnParameters) {
            if (parameters.doesMatch(planet)) return true;
        }
        return false;
    }
}
