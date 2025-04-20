package com.example.smartcalcpro;

public class LandUnitConverter {
    // Nepalese Land Unit Conversion Constants
    public static final double DHURTY_TO_DHUR = 1.0 / 4.0;  // 1 Dhur = 4 Dhurty
    public static final double DHUR_TO_KATTHA = 1.0 / 20.0;
    public static final double KATTHA_TO_BIGHA = 1.0 / 20.0;
    public static final double ROPANI_TO_AANA = 1.0 / 16.0;
    public static final double AANA_TO_PAISA = 1.0 / 4.0;
    public static final double PAISA_TO_DAM = 1.0 / 4.0;

    // Square Meter Conversion Constants
    public static final double DHURTY_TO_SQM = 4.23;  // 1 Dhurty = 4.23 sq meters
    public static final double DHUR_TO_SQM = DHURTY_TO_SQM * 4;  // 1 Dhur = 16.93 sq meters
    public static final double KATTHA_TO_SQM = DHUR_TO_SQM * 20;
    public static final double BIGHA_TO_SQM = KATTHA_TO_SQM * 20;
    public static final double ROPANI_TO_SQM = 508.74;
    public static final double AANA_TO_SQM = ROPANI_TO_SQM / 16;
    public static final double PAISA_TO_SQM = AANA_TO_SQM / 4;
    public static final double DAM_TO_SQM = PAISA_TO_SQM / 4;

    // Unit Definitions with Examples
    public static String getUnitDefinition(String unit) {
        switch (unit.toLowerCase()) {
            case "dhurty":
                return "धुर्ती (Dhurty):\n" +
                       "• Smallest unit in Terai system\n" +
                       "• 1 Dhurty = 4.23 square meters\n" +
                       "• 4 Dhurty = 1 Dhur\n" +
                       "Example: A small garden plot might be 2 Dhurty";
            case "dhur":
                return "धुर (Dhur):\n" +
                       "• Basic unit in Terai system\n" +
                       "• 1 Dhur = 16.93 square meters\n" +
                       "• 1 Dhur = 4 Dhurty\n" +
                       "• 20 Dhur = 1 Kattha\n" +
                       "Example: A typical house plot might be 2-3 Dhur";
            case "kattha":
                return "कट्ठा (Kattha):\n" +
                       "• Common unit in Terai\n" +
                       "• 1 Kattha = 338.63 square meters\n" +
                       "• 1 Kattha = 20 Dhur\n" +
                       "• 20 Kattha = 1 Bigha\n" +
                       "Example: A residential plot is often 1-2 Kattha";
            case "bigha":
                return "बिघा (Bigha):\n" +
                       "• Largest Terai unit\n" +
                       "• 1 Bigha = 6,772.63 square meters\n" +
                       "• 1 Bigha = 20 Kattha = 400 Dhur\n" +
                       "Example: A large farm might be 2-3 Bigha";
            case "ropani":
                return "रोपनी (Ropani):\n" +
                       "• Primary hill unit\n" +
                       "• 1 Ropani = 508.74 square meters\n" +
                       "• 1 Ropani = 16 Aana\n" +
                       "Example: A hillside terrace farm might be 4-5 Ropani";
            case "aana":
                return "आना (Aana):\n" +
                       "• Common hill unit\n" +
                       "• 1 Aana = 31.80 square meters\n" +
                       "• 1 Aana = 4 Paisa\n" +
                       "• 16 Aana = 1 Ropani\n" +
                       "Example: A small hill plot might be 3-4 Aana";
            case "paisa":
                return "पैसा (Paisa):\n" +
                       "• Smaller hill unit\n" +
                       "• 1 Paisa = 7.95 square meters\n" +
                       "• 1 Paisa = 4 Dam\n" +
                       "• 4 Paisa = 1 Aana\n" +
                       "Example: A small terrace might be 2 Paisa";
            case "dam":
                return "दाम (Dam):\n" +
                       "• Smallest hill unit\n" +
                       "• 1 Dam = 1.99 square meters\n" +
                       "• 4 Dam = 1 Paisa\n" +
                       "Example: A tiny garden plot might be 3 Dam";
            default:
                return "Unknown unit";
        }
    }

    // Convert any unit to square meters
    public static double toSquareMeters(double value, String fromUnit) {
        switch (fromUnit.toLowerCase()) {
            case "dhurty":
                return value * DHURTY_TO_SQM;
            case "dhur":
                return value * DHUR_TO_SQM;
            case "kattha":
                return value * KATTHA_TO_SQM;
            case "bigha":
                return value * BIGHA_TO_SQM;
            case "ropani":
                return value * ROPANI_TO_SQM;
            case "aana":
                return value * AANA_TO_SQM;
            case "paisa":
                return value * PAISA_TO_SQM;
            case "dam":
                return value * DAM_TO_SQM;
            default:
                return value;
        }
    }

    // Convert square meters to any unit
    public static double fromSquareMeters(double sqMeters, String toUnit) {
        switch (toUnit.toLowerCase()) {
            case "dhurty":
                return sqMeters / DHURTY_TO_SQM;
            case "dhur":
                return sqMeters / DHUR_TO_SQM;
            case "kattha":
                return sqMeters / KATTHA_TO_SQM;
            case "bigha":
                return sqMeters / BIGHA_TO_SQM;
            case "ropani":
                return sqMeters / ROPANI_TO_SQM;
            case "aana":
                return sqMeters / AANA_TO_SQM;
            case "paisa":
                return sqMeters / PAISA_TO_SQM;
            case "dam":
                return sqMeters / DAM_TO_SQM;
            default:
                return sqMeters;
        }
    }

    // Convert between any two units
    public static double convert(double value, String fromUnit, String toUnit) {
        double sqMeters = toSquareMeters(value, fromUnit);
        return fromSquareMeters(sqMeters, toUnit);
    }

    // Get state for a given unit
    public static String getState(String unit) {
        switch (unit.toLowerCase()) {
            case "dhurty":
            case "dhur":
            case "kattha":
            case "bigha":
                return "Terai";
            case "ropani":
            case "aana":
            case "paisa":
            case "dam":
                return "Hill";
            default:
                return "Unknown";
        }
    }

    // Check if units are from the same state system
    public static boolean areSameStateSystem(String unit1, String unit2) {
        return getState(unit1).equals(getState(unit2));
    }
} 