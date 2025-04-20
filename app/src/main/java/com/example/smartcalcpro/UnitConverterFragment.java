package com.example.smartcalcpro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class UnitConverterFragment extends Fragment {
    private EditText inputValue;
    private TextView resultText;
    private AutoCompleteTextView fromUnitSpinner;
    private AutoCompleteTextView toUnitSpinner;
    private TextInputLayout fromUnitLayout;
    private TextInputLayout toUnitLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_converter, container, false);

        // Initialize views
        inputValue = view.findViewById(R.id.inputValue);
        resultText = view.findViewById(R.id.resultText);
        fromUnitSpinner = view.findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = view.findViewById(R.id.toUnitSpinner);
        fromUnitLayout = view.findViewById(R.id.fromUnitLayout);
        toUnitLayout = view.findViewById(R.id.toUnitLayout);

        setupUnitSpinners();
        setupListeners();

        return view;
    }

    private void setupUnitSpinners() {
        // Define available units
        String[] units = {
            "Meter", "Kilometer", "Centimeter", "Millimeter",
            "Inch", "Foot", "Yard", "Mile",
            "Gram", "Kilogram", "Pound", "Ounce",
            "Liter", "Milliliter", "Gallon", "Fluid Ounce",
            "Celsius", "Fahrenheit", "Kelvin"
        };

        // Create adapter for spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            R.layout.dropdown_item,
            units
        );

        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        // Set default values
        fromUnitSpinner.setText(units[0], false);
        toUnitSpinner.setText(units[1], false);
    }

    private void setupListeners() {
        // Add text change listener to input
        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convert();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Add unit change listeners
        fromUnitSpinner.setOnItemClickListener((parent, view, position, id) -> convert());
        toUnitSpinner.setOnItemClickListener((parent, view, position, id) -> convert());
    }

    private void convert() {
        String input = inputValue.getText().toString();
        if (input.isEmpty()) {
            resultText.setText("");
            return;
        }

        try {
            double value = Double.parseDouble(input);
            String fromUnit = fromUnitSpinner.getText().toString();
            String toUnit = toUnitSpinner.getText().toString();

            // Convert to base unit first (meters for length, grams for mass, etc.)
            double baseValue = convertToBase(value, fromUnit);
            
            // Convert from base unit to target unit
            double result = convertFromBase(baseValue, toUnit);

            // Format the result
            resultText.setText(String.format("%.6f", result));
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input");
        }
    }

    private double convertToBase(double value, String unit) {
        // Conversion to base units
        switch (unit) {
            // Length conversions (base: meter)
            case "Meter": return value;
            case "Kilometer": return value * 1000;
            case "Centimeter": return value / 100;
            case "Millimeter": return value / 1000;
            case "Inch": return value * 0.0254;
            case "Foot": return value * 0.3048;
            case "Yard": return value * 0.9144;
            case "Mile": return value * 1609.34;

            // Mass conversions (base: gram)
            case "Gram": return value;
            case "Kilogram": return value * 1000;
            case "Pound": return value * 453.592;
            case "Ounce": return value * 28.3495;

            // Volume conversions (base: liter)
            case "Liter": return value;
            case "Milliliter": return value / 1000;
            case "Gallon": return value * 3.78541;
            case "Fluid Ounce": return value * 0.0295735;

            // Temperature conversions (base: celsius)
            case "Celsius": return value;
            case "Fahrenheit": return (value - 32) * 5/9;
            case "Kelvin": return value - 273.15;

            default: return value;
        }
    }

    private double convertFromBase(double baseValue, String unit) {
        // Conversion from base units
        switch (unit) {
            // Length conversions (base: meter)
            case "Meter": return baseValue;
            case "Kilometer": return baseValue / 1000;
            case "Centimeter": return baseValue * 100;
            case "Millimeter": return baseValue * 1000;
            case "Inch": return baseValue / 0.0254;
            case "Foot": return baseValue / 0.3048;
            case "Yard": return baseValue / 0.9144;
            case "Mile": return baseValue / 1609.34;

            // Mass conversions (base: gram)
            case "Gram": return baseValue;
            case "Kilogram": return baseValue / 1000;
            case "Pound": return baseValue / 453.592;
            case "Ounce": return baseValue / 28.3495;

            // Volume conversions (base: liter)
            case "Liter": return baseValue;
            case "Milliliter": return baseValue * 1000;
            case "Gallon": return baseValue / 3.78541;
            case "Fluid Ounce": return baseValue / 0.0295735;

            // Temperature conversions (base: celsius)
            case "Celsius": return baseValue;
            case "Fahrenheit": return (baseValue * 9/5) + 32;
            case "Kelvin": return baseValue + 273.15;

            default: return baseValue;
        }
    }
} 