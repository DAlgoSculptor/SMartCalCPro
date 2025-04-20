package com.example.smartcalcpro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class LandConverterFragment extends Fragment {
    private TextInputEditText inputValue;
    private AutoCompleteTextView fromUnitSpinner;
    private AutoCompleteTextView toUnitSpinner;
    private TextView resultText;
    private TextView stateInfo;
    private TextView unitInfo;

    private final List<String> units = Arrays.asList(
        "Dhurty", "Dhur", "Kattha", "Bigha",  // Terai units
        "Ropani", "Aana", "Paisa", "Dam"  // Hill units
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_land_converter, container, false);

        // Initialize views
        inputValue = view.findViewById(R.id.inputValue);
        fromUnitSpinner = view.findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = view.findViewById(R.id.toUnitSpinner);
        resultText = view.findViewById(R.id.resultText);
        stateInfo = view.findViewById(R.id.stateInfo);
        unitInfo = view.findViewById(R.id.unitInfo);

        // Set up spinners with custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            R.layout.dropdown_item,
            units
        );

        fromUnitSpinner.setAdapter(adapter);
        toUnitSpinner.setAdapter(adapter);

        // Set default values
        fromUnitSpinner.setText(units.get(0), false);
        toUnitSpinner.setText(units.get(1), false);

        // Force initial unit info display
        updateUnitInfo(units.get(0));
        updateStateCompatibility();

        // Set up listeners
        setupListeners();

        return view;
    }

    private void setupListeners() {
        fromUnitSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUnit = units.get(position);
            updateUnitInfo(selectedUnit);
            updateStateCompatibility();
            convert();
        });

        toUnitSpinner.setOnItemClickListener((parent, view, position, id) -> {
            updateStateCompatibility();
            convert();
        });

        inputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                convert();
            }
        });
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

            // Convert to square meters first (base unit)
            double squareMeters = convertToSquareMeters(value, fromUnit);
            
            // Convert from square meters to target unit
            double result = convertFromSquareMeters(squareMeters, toUnit);

            // Format the result
            DecimalFormat df = new DecimalFormat("#,##0.####");
            resultText.setText(df.format(result));
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input");
        }
    }

    private double convertToSquareMeters(double value, String unit) {
        // Conversion factors to square meters
        switch (unit) {
            case "Dhurty":
                return value * 0.5; // 1 Dhurty = 0.5 sq.m
            case "Dhur":
                return value * 2; // 1 Dhur = 2 sq.m
            case "Kattha":
                return value * 40; // 1 Kattha = 40 sq.m
            case "Bigha":
                return value * 800; // 1 Bigha = 800 sq.m
            case "Ropani":
                return value * 508.74; // 1 Ropani = 508.74 sq.m
            case "Aana":
                return value * 31.796; // 1 Aana = 31.796 sq.m
            case "Paisa":
                return value * 7.949; // 1 Paisa = 7.949 sq.m
            case "Dam":
                return value * 1.987; // 1 Dam = 1.987 sq.m
            default:
                return value;
        }
    }

    private double convertFromSquareMeters(double squareMeters, String unit) {
        // Conversion factors from square meters
        switch (unit) {
            case "Dhurty":
                return squareMeters / 0.5;
            case "Dhur":
                return squareMeters / 2;
            case "Kattha":
                return squareMeters / 40;
            case "Bigha":
                return squareMeters / 800;
            case "Ropani":
                return squareMeters / 508.74;
            case "Aana":
                return squareMeters / 31.796;
            case "Paisa":
                return squareMeters / 7.949;
            case "Dam":
                return squareMeters / 1.987;
            default:
                return squareMeters;
        }
    }

    private void updateConversion() {
        String input = inputValue.getText().toString();
        if (input.isEmpty()) {
            resultText.setText("0");
            return;
        }

        try {
            double value = Double.parseDouble(input);
            String fromUnit = fromUnitSpinner.getText().toString();
            String toUnit = toUnitSpinner.getText().toString();

            double result = LandUnitConverter.convert(value, fromUnit, toUnit);
            DecimalFormat df = new DecimalFormat("#,##0.####");
            resultText.setText(df.format(result));

            updateStateCompatibility();
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input");
        }
    }

    private void updateStateCompatibility() {
        String fromUnit = fromUnitSpinner.getText().toString();
        String toUnit = toUnitSpinner.getText().toString();

        if (isHillUnit(fromUnit) != isHillUnit(toUnit)) {
            stateInfo.setText("Warning: Converting between Hill and Terai units may not be accurate");
            stateInfo.setVisibility(View.VISIBLE);
        } else {
            stateInfo.setVisibility(View.GONE);
        }
    }

    private boolean isHillUnit(String unit) {
        return unit.equals("Ropani") || unit.equals("Aana") || 
               unit.equals("Paisa") || unit.equals("Dam");
    }

    private void updateUnitInfo(String selectedUnit) {
        if (isHillUnit(selectedUnit)) {
            unitInfo.setText("Hill Units:\n1 Ropani = 16 Aana\n1 Aana = 4 Paisa\n1 Paisa = 4 Dam");
        } else {
            unitInfo.setText("Terai Units:\n1 Bigha = 20 Kattha\n1 Kattha = 20 Dhur\n1 Dhur = 4 Dhurty");
        }
    }
} 