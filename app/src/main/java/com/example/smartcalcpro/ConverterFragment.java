package com.example.smartcalcpro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ConverterFragment extends Fragment {
    private TextInputEditText inputValue;
    private AutoCompleteTextView fromUnit;
    private AutoCompleteTextView toUnit;
    private MaterialButton convertButton;
    private ImageView swapButton;
    private TextView resultValue;
    private TextView resultFormula;
    private View resultCard;

    private String selectedCategory = "Length";
    private Map<String, Double> conversionFactors;
    private String[] units;
    private DecimalFormat decimalFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        inputValue = view.findViewById(R.id.inputValue);
        fromUnit = view.findViewById(R.id.fromUnit);
        toUnit = view.findViewById(R.id.toUnit);
        convertButton = view.findViewById(R.id.convertButton);
        swapButton = view.findViewById(R.id.swapButton);
        resultValue = view.findViewById(R.id.resultValue);
        resultFormula = view.findViewById(R.id.resultFormula);
        resultCard = view.findViewById(R.id.resultCard);

        // Initialize number formatter
        decimalFormat = new DecimalFormat("#,##0.########");

        // Set up unit selection
        setupUnitSelection();

        // Set up listeners
        setupListeners();
    }

    private void setupUnitSelection() {
        // Default to length units
        setConversionCategory("Length");
    }

    private void setConversionCategory(String category) {
        selectedCategory = category;
        switch (category) {
            case "Length":
                units = new String[]{"Meter", "Kilometer", "Centimeter", "Millimeter", "Mile", "Yard", "Foot", "Inch"};
                conversionFactors = new HashMap<>();
                conversionFactors.put("Meter", 1.0);
                conversionFactors.put("Kilometer", 1000.0);
                conversionFactors.put("Centimeter", 0.01);
                conversionFactors.put("Millimeter", 0.001);
                conversionFactors.put("Mile", 1609.344);
                conversionFactors.put("Yard", 0.9144);
                conversionFactors.put("Foot", 0.3048);
                conversionFactors.put("Inch", 0.0254);
                break;
            case "Mass":
                units = new String[]{"Kilogram", "Gram", "Milligram", "Pound", "Ounce", "Ton"};
                conversionFactors = new HashMap<>();
                conversionFactors.put("Kilogram", 1.0);
                conversionFactors.put("Gram", 0.001);
                conversionFactors.put("Milligram", 0.000001);
                conversionFactors.put("Pound", 0.453592);
                conversionFactors.put("Ounce", 0.0283495);
                conversionFactors.put("Ton", 907.185);
                break;
            case "Temperature":
                units = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
                break;
            // Add more categories as needed
        }

        // Set up dropdown adapters
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, units);
        fromUnit.setAdapter(adapter);
        toUnit.setAdapter(adapter);

        // Set default selections
        if (units.length > 0) {
            fromUnit.setText(units[0], false);
            if (units.length > 1) {
                toUnit.setText(units[1], false);
            }
        }
    }

    private void setupListeners() {
        // Input value listener
        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    performConversion();
                } else {
                    resultCard.setVisibility(View.GONE);
                }
            }
        });

        // Unit selection listeners
        fromUnit.setOnItemClickListener((parent, view, position, id) -> performConversion());
        toUnit.setOnItemClickListener((parent, view, position, id) -> performConversion());

        // Swap button listener
        swapButton.setOnClickListener(v -> {
            String temp = fromUnit.getText().toString();
            fromUnit.setText(toUnit.getText().toString(), false);
            toUnit.setText(temp, false);
            performConversion();
        });

        // Convert button listener
        convertButton.setOnClickListener(v -> performConversion());
    }

    private void performConversion() {
        try {
            String inputStr = inputValue.getText().toString();
            if (inputStr.isEmpty()) {
                resultCard.setVisibility(View.GONE);
                return;
            }

            double value = Double.parseDouble(inputStr);
            String fromUnitStr = fromUnit.getText().toString();
            String toUnitStr = toUnit.getText().toString();

            if (fromUnitStr.isEmpty() || toUnitStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both units", Toast.LENGTH_SHORT).show();
                return;
            }

            double result;
            String formula;

            if (selectedCategory.equals("Temperature")) {
                result = convertTemperature(value, fromUnitStr, toUnitStr);
                formula = getTemperatureFormula(value, fromUnitStr, toUnitStr);
            } else {
                result = convertStandard(value, fromUnitStr, toUnitStr);
                formula = getStandardFormula(value, fromUnitStr, toUnitStr);
            }

            resultValue.setText(decimalFormat.format(result));
            resultFormula.setText(formula);
            resultCard.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private double convertStandard(double value, String from, String to) {
        double fromFactor = conversionFactors.get(from);
        double toFactor = conversionFactors.get(to);
        return value * fromFactor / toFactor;
    }

    private String getStandardFormula(double value, String from, String to) {
        return String.format("%s %s = %s %s",
                decimalFormat.format(value), from,
                decimalFormat.format(convertStandard(value, from, to)), to);
    }

    private double convertTemperature(double value, String from, String to) {
        double celsius;
        switch (from) {
            case "Celsius":
                celsius = value;
                break;
            case "Fahrenheit":
                celsius = (value - 32) * 5 / 9;
                break;
            case "Kelvin":
                celsius = value - 273.15;
                break;
            default:
                return value;
        }

        switch (to) {
            case "Celsius":
                return celsius;
            case "Fahrenheit":
                return (celsius * 9 / 5) + 32;
            case "Kelvin":
                return celsius + 273.15;
            default:
                return value;
        }
    }

    private String getTemperatureFormula(double value, String from, String to) {
        return String.format("%s %s = %s %s",
                decimalFormat.format(value), from,
                decimalFormat.format(convertTemperature(value, from, to)), to);
    }
} 