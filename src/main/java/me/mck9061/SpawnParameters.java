package me.mck9061;

public class SpawnParameters {
    public String atmosphere = "any";
    public String bodyType = "any";
    public float maxGravity = 9999;
    public int maxTemperature = 9999;
    public int minTemperature = 0;
    public int minDistance = 0;
    public String volcanism = "any";
    public String starType;

    public void print() {
        System.out.print(atmosphere + " ");
        System.out.print(bodyType + " ");
        System.out.print(maxGravity + " ");
        System.out.print(maxTemperature + " ");
        System.out.print(minTemperature + " ");
        System.out.print(minDistance + " ");
        System.out.print(volcanism + " ");
    }


    public boolean doesMatch(Planet planet) {
        // Compare planet types
        if (bodyType.equals("icy") & !(planet.planetClass.equals("Icy body"))) return false;
        if (bodyType.equals("rocky") & !(planet.planetClass.equals("Rocky body"))) return false;
        if (bodyType.equals("rocky icy") & !(planet.planetClass.equals("Rocky icy body"))) return false;
        if (bodyType.equals("hmc") & !(planet.planetClass.equals("High metal content body"))) return false;

        // Compare atmospheres
        String atmCompare = atmosphere;
        if (atmCompare.equals("co2")) atmCompare = "carbon dioxide";
        if (atmCompare.equals("so2")) atmCompare = "sulfur dioxide";
        if (!planet.atmosphere.contains(atmCompare)) return false;

        // Compare gravity
        //if (planet.gravity > maxGravity) return false;

        // Compare temperature
        if (planet.temperature > maxTemperature || planet.temperature < minTemperature) return false;

        // Compare distance
        if (planet.distance < minDistance) return false;

        // Compare volcanism
        if (!volcanism.equals("") && !volcanism.equals("any") && !planet.volcanism.contains(volcanism)) return false;

        return true;
    }
}
