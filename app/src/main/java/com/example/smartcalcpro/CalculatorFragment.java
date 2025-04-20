package com.example.smartcalcpro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CalculatorFragment extends Fragment {
    private TextInputEditText inputValue;
    private boolean isNewOperation = true;
    private double currentValue = 0;
    private String currentOperation = "";
    private boolean isScientificMode = false;
    private View scientificTopRow;
    private View scientificLeftColumn;
    private GridLayout basicGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        inputValue = view.findViewById(R.id.inputValue);
        scientificTopRow = view.findViewById(R.id.scientificTopRow);
        scientificLeftColumn = view.findViewById(R.id.scientificLeftColumn);
        basicGrid = view.findViewById(R.id.basicGrid);

        setupNumberButtons(view);
        setupOperationButtons(view);
        setupScientificButtons(view);

        return view;
    }

    private void setupNumberButtons(View view) {
        int[] numberIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberIds) {
            MaterialButton button = view.findViewById(id);
            button.setOnClickListener(v -> appendNumber(button.getText().toString()));
        }

        view.findViewById(R.id.btnDot).setOnClickListener(v -> appendDecimal());
        view.findViewById(R.id.btnRotate).setOnClickListener(v -> toggleScientificMode());
    }

    private void setupOperationButtons(View view) {
        view.findViewById(R.id.btnPlus).setOnClickListener(v -> setOperation("+"));
        view.findViewById(R.id.btnMinus).setOnClickListener(v -> setOperation("-"));
        view.findViewById(R.id.btnMultiply).setOnClickListener(v -> setOperation("×"));
        view.findViewById(R.id.btnDivide).setOnClickListener(v -> setOperation("÷"));
        view.findViewById(R.id.btnPercent).setOnClickListener(v -> setOperation("%"));
        view.findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
        view.findViewById(R.id.btnClear).setOnClickListener(v -> clear());
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> deleteLastChar());
    }

    private void setupScientificButtons(View view) {
        view.findViewById(R.id.btnFactorial).setOnClickListener(v -> calculateFactorial());
        view.findViewById(R.id.btnSquare).setOnClickListener(v -> calculateSquare());
        view.findViewById(R.id.btnSquareRoot).setOnClickListener(v -> calculateSquareRoot());
        view.findViewById(R.id.btnPi).setOnClickListener(v -> setPi());
        view.findViewById(R.id.btnSin).setOnClickListener(v -> calculateSin());
        view.findViewById(R.id.btnCos).setOnClickListener(v -> calculateCos());
        view.findViewById(R.id.btnTan).setOnClickListener(v -> calculateTan());
        view.findViewById(R.id.btnLog).setOnClickListener(v -> calculateLog());
    }

    private void toggleScientificMode() {
        isScientificMode = !isScientificMode;
        scientificTopRow.setVisibility(isScientificMode ? View.VISIBLE : View.GONE);
        scientificLeftColumn.setVisibility(isScientificMode ? View.VISIBLE : View.GONE);

        // Adjust the basic calculator grid width
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) basicGrid.getLayoutParams();
        if (isScientificMode) {
            params.weight = 1;
            params.setMargins(48, 0, 0, 0); // Add left margin to make space for scientific buttons
        } else {
            params.weight = 1;
            params.setMargins(0, 0, 0, 0);
        }
        basicGrid.setLayoutParams(params);
    }

    private void appendNumber(String number) {
        if (isNewOperation) {
            inputValue.setText(number);
            isNewOperation = false;
        } else {
            inputValue.append(number);
        }
    }

    private void appendDecimal() {
        String currentText = inputValue.getText().toString();
        if (!currentText.contains(".")) {
            if (isNewOperation) {
                inputValue.setText("0.");
                isNewOperation = false;
            } else {
                inputValue.append(".");
            }
        }
    }

    private void setOperation(String operation) {
        if (!inputValue.getText().toString().isEmpty()) {
            currentValue = Double.parseDouble(inputValue.getText().toString());
            currentOperation = operation;
            isNewOperation = true;
        }
    }

    private void calculate() {
        if (!inputValue.getText().toString().isEmpty() && !currentOperation.isEmpty()) {
            double secondValue = Double.parseDouble(inputValue.getText().toString());
            double result = 0;

            switch (currentOperation) {
                case "+":
                    result = currentValue + secondValue;
                    break;
                case "-":
                    result = currentValue - secondValue;
                    break;
                case "×":
                    result = currentValue * secondValue;
                    break;
                case "÷":
                    if (secondValue != 0) {
                        result = currentValue / secondValue;
                    } else {
                        inputValue.setError("Cannot divide by zero");
                        return;
                    }
                    break;
                case "%":
                    result = currentValue % secondValue;
                    break;
            }

            inputValue.setText(String.format("%.2f", result));
            currentOperation = "";
            isNewOperation = true;
        }
    }

    private void clear() {
        inputValue.setText("");
        currentValue = 0;
        currentOperation = "";
        isNewOperation = true;
    }

    private void deleteLastChar() {
        String currentText = inputValue.getText().toString();
        if (!currentText.isEmpty()) {
            inputValue.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void calculateFactorial() {
        if (!inputValue.getText().toString().isEmpty()) {
            try {
                int n = Integer.parseInt(inputValue.getText().toString());
                if (n < 0) {
                    inputValue.setError("Cannot calculate factorial of negative number");
                    return;
                }
                long result = 1;
                for (int i = 2; i <= n; i++) {
                    result *= i;
                }
                inputValue.setText(String.valueOf(result));
                isNewOperation = true;
            } catch (NumberFormatException e) {
                inputValue.setError("Invalid input for factorial");
            }
        }
    }

    private void calculateSquare() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            double result = value * value;
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }

    private void calculateSquareRoot() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            if (value < 0) {
                inputValue.setError("Cannot calculate square root of negative number");
                return;
            }
            double result = Math.sqrt(value);
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }

    private void setPi() {
        inputValue.setText(String.format("%.10f", Math.PI));
        isNewOperation = true;
    }

    private void calculateSin() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            double result = Math.sin(Math.toRadians(value));
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }

    private void calculateCos() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            double result = Math.cos(Math.toRadians(value));
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }

    private void calculateTan() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            double result = Math.tan(Math.toRadians(value));
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }

    private void calculateLog() {
        if (!inputValue.getText().toString().isEmpty()) {
            double value = Double.parseDouble(inputValue.getText().toString());
            if (value <= 0) {
                inputValue.setError("Cannot calculate log of non-positive number");
                return;
            }
            double result = Math.log10(value);
            inputValue.setText(String.format("%.2f", result));
            isNewOperation = true;
        }
    }
} 