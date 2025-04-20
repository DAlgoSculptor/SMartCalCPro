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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BMICalculatorFragment extends Fragment {
    private TextInputEditText heightInput;
    private TextInputEditText weightInput;
    private AutoCompleteTextView unitSpinner;
    private TextView bmiResult;
    private TextView bmiCategory;
    private MaterialButton calculateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi_calculator, container, false);

        // Initialize views
        heightInput = view.findViewById(R.id.heightInput);
        weightInput = view.findViewById(R.id.weightInput);
        unitSpinner = view.findViewById(R.id.unitSpinner);
        bmiResult = view.findViewById(R.id.bmiResult);
        bmiCategory = view.findViewById(R.id.bmiCategory);
        calculateButton = view.findViewById(R.id.calculateButton);

        setupUnitSpinner();
        setupListeners();

        return view;
    }

    private void setupUnitSpinner() {
        String[] units = {"Metric (kg, cm)", "Imperial (lb, in)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            R.layout.dropdown_item,
            units
        );
        unitSpinner.setAdapter(adapter);
        unitSpinner.setText(units[0], false);
    }

    private void setupListeners() {
        // Add text change listeners to input fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateBMI();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        heightInput.addTextChangedListener(textWatcher);
        weightInput.addTextChangedListener(textWatcher);
        unitSpinner.setOnItemClickListener((parent, view, position, id) -> calculateBMI());
    }

    private void calculateBMI() {
        String heightStr = heightInput.getText().toString();
        String weightStr = weightInput.getText().toString();
        String unitSystem = unitSpinner.getText().toString();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            bmiResult.setText("");
            bmiCategory.setText("");
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            double bmi;

            if (unitSystem.equals("Metric (kg, cm)")) {
                // Convert height from cm to m
                height = height / 100;
                bmi = weight / (height * height);
            } else {
                // Imperial system
                bmi = (weight * 703) / (height * height);
            }

            // Format BMI to 2 decimal places
            String bmiFormatted = String.format("%.2f", bmi);
            bmiResult.setText("BMI: " + bmiFormatted);
            bmiCategory.setText(getBMICategory(bmi));
        } catch (NumberFormatException e) {
            bmiResult.setText("Invalid input");
            bmiCategory.setText("");
        }
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
} 