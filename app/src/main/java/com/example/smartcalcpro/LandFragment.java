package com.example.smartcalcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class LandFragment extends Fragment {
    private TextView display;
    private String currentNumber = "";
    private String operator = "";
    private double result = 0;
    private boolean isNewOperation = true;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");
    
    private AutoCompleteTextView stateSpinner;
    private AutoCompleteTextView unitSpinner;
    private TextInputEditText lengthInput;
    private TextInputEditText widthInput;
    private MaterialButton calculateButton;
    private MaterialCardView resultCard;
    private TextView areaResultText;
    private TextView conversionText;

    private Map<String, String[]> stateUnits;
    private Map<String, Double> conversionFactors;
    private String currentUnit = "";
    private String currentState = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_land, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupStateUnits();
        setupConversionFactors();
        setupSpinners();
        setupCalculatorButtons(view);
        setupCalculateButton();
    }

    private void initializeViews(View view) {
        display = view.findViewById(R.id.display);
        stateSpinner = view.findViewById(R.id.stateSpinner);
        unitSpinner = view.findViewById(R.id.unitSpinner);
        lengthInput = view.findViewById(R.id.lengthInput);
        widthInput = view.findViewById(R.id.widthInput);
        calculateButton = view.findViewById(R.id.calculateButton);
        resultCard = view.findViewById(R.id.resultCard);
        areaResultText = view.findViewById(R.id.areaResultText);
        conversionText = view.findViewById(R.id.conversionText);
    }

    private void setupStateUnits() {
        stateUnits = new HashMap<>();
        // Northern States
        stateUnits.put("Jammu and Kashmir", new String[]{"Kanal", "Marla", "Biswa", "Bigha"});
        stateUnits.put("Himachal Pradesh", new String[]{"Biswa", "Bigha", "Kanal", "Marla"});
        stateUnits.put("Punjab", new String[]{"Marla", "Kanal", "Bigha", "Acre"});
        stateUnits.put("Haryana", new String[]{"Biswa", "Bigha", "Kanal", "Marla"});
        stateUnits.put("Uttarakhand", new String[]{"Biswa", "Bigha", "Nali", "Acre"});
        stateUnits.put("Uttar Pradesh", new String[]{"Bigha", "Biswa", "Katha", "Acre"});
        stateUnits.put("Delhi", new String[]{"Bigha", "Biswa", "Kanal", "Marla"});

        // Western States
        stateUnits.put("Rajasthan", new String[]{"Bigha", "Biswa", "Biswansi", "Acre"});
        stateUnits.put("Gujarat", new String[]{"Vigha", "Guntha", "Acre", "Square Feet"});
        stateUnits.put("Maharashtra", new String[]{"Guntha", "Acre", "Bigha", "Square Feet"});
        stateUnits.put("Goa", new String[]{"Guntha", "Acre", "Square Feet", "Square Meter"});

        // Southern States
        stateUnits.put("Karnataka", new String[]{"Guntha", "Acre", "Ground", "Square Feet"});
        stateUnits.put("Kerala", new String[]{"Cent", "Acre", "Square Feet", "Square Meter"});
        stateUnits.put("Tamil Nadu", new String[]{"Cent", "Acre", "Ground", "Square Feet"});
        stateUnits.put("Andhra Pradesh", new String[]{"Guntha", "Acre", "Ground", "Square Feet"});
        stateUnits.put("Telangana", new String[]{"Guntha", "Acre", "Ground", "Square Feet"});

        // Eastern States
        stateUnits.put("West Bengal", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Bihar", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Jharkhand", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Odisha", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Assam", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Sikkim", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Arunachal Pradesh", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Nagaland", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Manipur", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Mizoram", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Tripura", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
        stateUnits.put("Meghalaya", new String[]{"Katha", "Bigha", "Acre", "Square Feet"});
    }

    private void setupConversionFactors() {
        conversionFactors = new HashMap<>();
        // Common units (in square meters)
        conversionFactors.put("Square Meter", 1.0);
        conversionFactors.put("Square Feet", 0.0929);
        conversionFactors.put("Acre", 4046.86);

        // Northern States
        conversionFactors.put("Kanal", 505.857); // 1 Kanal = 505.857 sq meters
        conversionFactors.put("Marla", 25.2929); // 1 Marla = 25.2929 sq meters
        conversionFactors.put("Biswa", 125.419); // 1 Biswa = 125.419 sq meters
        conversionFactors.put("Bigha", 2508.38); // 1 Bigha = 2508.38 sq meters
        conversionFactors.put("Nali", 200.671); // 1 Nali = 200.671 sq meters

        // Western States
        conversionFactors.put("Vigha", 2508.38); // 1 Vigha = 2508.38 sq meters
        conversionFactors.put("Guntha", 101.171); // 1 Guntha = 101.171 sq meters
        conversionFactors.put("Biswansi", 12.5419); // 1 Biswansi = 12.5419 sq meters

        // Southern States
        conversionFactors.put("Cent", 40.4686); // 1 Cent = 40.4686 sq meters
        conversionFactors.put("Ground", 222.967); // 1 Ground = 222.967 sq meters

        // Eastern States
        conversionFactors.put("Katha", 66.8903); // 1 Katha = 66.8903 sq meters
    }

    private void setupSpinners() {
        String[] states = stateUnits.keySet().toArray(new String[0]);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, states);
        stateSpinner.setAdapter(stateAdapter);

        stateSpinner.setOnItemClickListener((parent, view, position, id) -> {
            currentState = parent.getItemAtPosition(position).toString();
            String[] units = stateUnits.get(currentState);
            ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_dropdown_item_1line, units);
            unitSpinner.setAdapter(unitAdapter);
            unitSpinner.setText(units[0], false);
            currentUnit = units[0];
        });

        stateSpinner.setText(states[0], false);
        currentState = states[0];
        currentUnit = stateUnits.get(currentState)[0];
    }

    private void setupCalculatorButtons(View view) {
        try {
            // Only setup buttons that exist in the layout
            MaterialButton convertButton = view.findViewById(R.id.btnConvert);
            if (convertButton != null) {
                convertButton.setOnClickListener(v -> convertUnit());
            }

            // Remove calculator-specific setup since this is a land area calculator
            display = view.findViewById(R.id.display);
            if (display != null) {
                display.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("LandFragment", "Error setting up calculator buttons", e);
        }
    }

    private void setupCalculateButton() {
        calculateButton.setOnClickListener(v -> calculateArea());
    }

    private void appendNumber(String number) {
        if (isNewOperation) {
            currentNumber = number;
            isNewOperation = false;
        } else {
            currentNumber += number;
        }
        display.setText(currentNumber);
    }

    private void appendDecimal() {
        if (isNewOperation) {
            currentNumber = "0.";
            isNewOperation = false;
        } else if (!currentNumber.contains(".")) {
            currentNumber += ".";
        }
        display.setText(currentNumber);
    }

    private void setOperator(String op) {
        if (!currentNumber.isEmpty()) {
            if (!operator.isEmpty()) {
                calculate();
            }
            operator = op;
            result = Double.parseDouble(currentNumber);
            currentNumber = "";
            display.setText(decimalFormat.format(result));
        }
    }

    private void calculate() {
        if (!currentNumber.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            switch (operator) {
                case "+":
                    result += secondNumber;
                    break;
                case "-":
                    result -= secondNumber;
                    break;
                case "*":
                    result *= secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result /= secondNumber;
                    } else {
                        display.setText("Error");
                        return;
                    }
                    break;
            }
            display.setText(decimalFormat.format(result));
            currentNumber = "";
            operator = "";
            isNewOperation = true;
        }
    }

    private void convertUnit() {
        try {
            if (currentUnit.isEmpty() || currentState.isEmpty()) {
                return;
            }

            String[] availableUnits = stateUnits.get(currentState);
            if (availableUnits == null || availableUnits.length == 0) {
                return;
            }

            // Find the next unit in the array
            int currentIndex = -1;
            for (int i = 0; i < availableUnits.length; i++) {
                if (availableUnits[i].equals(currentUnit)) {
                    currentIndex = i;
                    break;
                }
            }

            if (currentIndex == -1) {
                return;
            }

            // Get the next unit (wrap around if at the end)
            int nextIndex = (currentIndex + 1) % availableUnits.length;
            String nextUnit = availableUnits[nextIndex];

            // Update the unit spinner
            unitSpinner.setText(nextUnit, false);
            currentUnit = nextUnit;

            // Recalculate and display results
            calculateArea();
        } catch (Exception e) {
            Log.e("LandFragment", "Error converting unit", e);
        }
    }

    private void clear() {
        currentNumber = "";
        operator = "";
        result = 0;
        isNewOperation = true;
        display.setText("0");
    }

    private void calculateArea() {
        try {
            if (lengthInput.getText().toString().isEmpty() || widthInput.getText().toString().isEmpty()) {
                return;
            }

            double length = Double.parseDouble(lengthInput.getText().toString());
            double width = Double.parseDouble(widthInput.getText().toString());
            double area = length * width;

            // Convert to square meters first
            double squareMeters = area * conversionFactors.get(currentUnit);

            // Display results
            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append(String.format("Area: %.2f %s\n", area, currentUnit));
            resultBuilder.append(String.format("Square Meters: %.2f\n", squareMeters));
            resultBuilder.append(String.format("Square Feet: %.2f\n", squareMeters / conversionFactors.get("Square Feet")));
            resultBuilder.append(String.format("Acre: %.2f\n", squareMeters / conversionFactors.get("Acre")));

            // Add state-specific conversions
            String[] availableUnits = stateUnits.get(currentState);
            if (availableUnits != null) {
                for (String unit : availableUnits) {
                    if (!unit.equals(currentUnit)) {
                        resultBuilder.append(String.format("%s: %.2f\n", unit, 
                            squareMeters / conversionFactors.get(unit)));
                    }
                }
            }

            areaResultText.setText(resultBuilder.toString());
            resultCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("LandFragment", "Error calculating area", e);
            areaResultText.setText("Error calculating area. Please check your input.");
            resultCard.setVisibility(View.VISIBLE);
        }
    }
} 