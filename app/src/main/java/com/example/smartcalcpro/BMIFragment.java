package com.example.smartcalcpro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DecimalFormat;
import java.util.Stack;

public class BMIFragment extends Fragment {
    private TextView display;
    private String currentNumber = "";
    private String operator = "";
    private double result = 0;
    private boolean isNewOperation = true;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");
    private final Stack<Double> memoryStack = new Stack<>();
    
    private TextInputLayout heightLayout;
    private TextInputLayout weightLayout;
    private TextInputEditText heightInput;
    private TextInputEditText weightInput;
    private MaterialCardView resultCard;
    private TextView bmiResultText;
    private TextView bmiCategoryText;
    private TextView bmiAdviceText;
    private LinearProgressIndicator bmiProgressIndicator;
    private MaterialButton calculateButton;
    private TabLayout unitTabLayout;
    private boolean isMetric = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bmi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupListeners();
        setupCalculatorButtons(view);
    }

    private void initializeViews(View view) {
        display = view.findViewById(R.id.display);
        heightLayout = view.findViewById(R.id.heightLayout);
        weightLayout = view.findViewById(R.id.weightLayout);
        heightInput = view.findViewById(R.id.heightInput);
        weightInput = view.findViewById(R.id.weightInput);
        resultCard = view.findViewById(R.id.resultCard);
        bmiResultText = view.findViewById(R.id.bmiResultText);
        bmiCategoryText = view.findViewById(R.id.bmiCategoryText);
        bmiAdviceText = view.findViewById(R.id.bmiAdviceText);
        bmiProgressIndicator = view.findViewById(R.id.bmiProgressIndicator);
        calculateButton = view.findViewById(R.id.calculateButton);
        unitTabLayout = view.findViewById(R.id.unitTabLayout);
    }

    private void setupListeners() {
        calculateButton.setOnClickListener(v -> calculateBMI());
        
        unitTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isMetric = tab.getPosition() == 0;
                updateHints();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCalculatorButtons(View view) {
        try {
            // Only setup buttons that exist in the layout
            MaterialButton squareButton = view.findViewById(R.id.btnSquare);
            if (squareButton != null) {
                squareButton.setOnClickListener(v -> calculateSquare());
            }

            MaterialButton squareRootButton = view.findViewById(R.id.btnSquareRoot);
            if (squareRootButton != null) {
                squareRootButton.setOnClickListener(v -> calculateSquareRoot());
            }

            // Remove calculator-specific setup since this is a BMI calculator
            display = view.findViewById(R.id.display);
            if (display != null) {
                display.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("BMIFragment", "Error setting up calculator buttons", e);
        }
    }

    private void calculateSquare() {
        try {
            if (heightInput != null && heightInput.getText() != null) {
                String heightStr = heightInput.getText().toString();
                if (!heightStr.isEmpty()) {
                    double height = Double.parseDouble(heightStr);
                    heightInput.setText(String.valueOf(height * height));
                }
            }
        } catch (Exception e) {
            Log.e("BMIFragment", "Error calculating square", e);
        }
    }

    private void calculateSquareRoot() {
        try {
            if (heightInput != null && heightInput.getText() != null) {
                String heightStr = heightInput.getText().toString();
                if (!heightStr.isEmpty()) {
                    double height = Double.parseDouble(heightStr);
                    if (height >= 0) {
                        heightInput.setText(String.valueOf(Math.sqrt(height)));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("BMIFragment", "Error calculating square root", e);
        }
    }

    private void calculateBMI() {
        String heightStr = heightInput.getText().toString();
        String weightStr = weightInput.getText().toString();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            showError("Please enter both height and weight");
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            if (height <= 0 || weight <= 0) {
                showError("Please enter valid height and weight values");
                return;
            }

            double bmi;
            if (isMetric) {
                // Convert cm to meters
                height = height / 100.0;
                bmi = weight / (height * height);
            } else {
                // Convert imperial to metric then calculate
                // Height is in inches, weight in pounds
                double heightInMeters = height * 0.0254;
                double weightInKg = weight * 0.453592;
                bmi = weightInKg / (heightInMeters * heightInMeters);
            }

            displayResult(bmi);

        } catch (NumberFormatException e) {
            showError("Please enter valid numbers");
        }
    }

    private void displayResult(double bmi) {
        DecimalFormat df = new DecimalFormat("#.#");
        String bmiString = df.format(bmi);
        String category = getBMICategory(bmi);
        int color = getBMICategoryColor(category);
        String advice = getAdvice(category);

        bmiResultText.setText(bmiString);
        bmiCategoryText.setText(category);
        bmiCategoryText.setTextColor(getResources().getColor(color, null));
        bmiAdviceText.setText(advice);

        // Set progress indicator
        bmiProgressIndicator.setProgress((int) (bmi * 2));
        bmiProgressIndicator.setIndicatorColor(getResources().getColor(color, null));

        resultCard.setVisibility(View.VISIBLE);
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal Weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private int getBMICategoryColor(String category) {
        switch (category) {
            case "Underweight":
                return R.color.bmi_underweight;
            case "Normal Weight":
                return R.color.bmi_normal;
            case "Overweight":
                return R.color.bmi_overweight;
            case "Obese":
                return R.color.bmi_obese;
            default:
                return R.color.black;
        }
    }

    private String getAdvice(String category) {
        switch (category) {
            case "Underweight":
                return "Consider consulting a healthcare provider about gaining weight safely through a balanced diet and proper exercise.";
            case "Normal Weight":
                return "Great job! Maintain your healthy lifestyle with a balanced diet and regular exercise.";
            case "Overweight":
                return "Consider making lifestyle changes including a balanced diet and regular exercise. Consult a healthcare provider for personalized advice.";
            case "Obese":
                return "It's recommended to consult with a healthcare provider for a personalized weight management plan that includes diet and exercise guidance.";
            default:
                return "";
        }
    }

    private void showError(String message) {
        resultCard.setVisibility(View.GONE);
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void updateHints() {
        if (isMetric) {
            heightLayout.setHint("Height (cm)");
            weightLayout.setHint("Weight (kg)");
        } else {
            heightLayout.setHint("Height (inches)");
            weightLayout.setHint("Weight (lbs)");
        }
    }
} 