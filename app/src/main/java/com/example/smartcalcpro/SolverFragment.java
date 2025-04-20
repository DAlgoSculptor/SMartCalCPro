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
import java.text.DecimalFormat;
import java.util.Stack;

public class SolverFragment extends Fragment {
    private TextView display;
    private String currentNumber = "";
    private String operator = "";
    private double result = 0;
    private boolean isNewOperation = true;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");
    private final Stack<Double> memoryStack = new Stack<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_solver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            initializeViews(view);
            setupCalculatorButtons(view);
        } catch (Exception e) {
            Log.e("SolverFragment", "Error in onViewCreated", e);
        }
    }

    private void initializeViews(View view) {
        try {
            display = view.findViewById(R.id.display);
            if (display != null) {
                display.setText("0");
            }
        } catch (Exception e) {
            Log.e("SolverFragment", "Error initializing views", e);
        }
    }

    private void setupCalculatorButtons(View view) {
        try {
            // Number buttons
            int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
            };

            for (int id : numberIds) {
                MaterialButton button = view.findViewById(id);
                if (button != null) {
                    button.setOnClickListener(v -> appendNumber(button.getText().toString()));
                }
            }

            // Operator buttons
            setupButton(view, R.id.btnAdd, v -> setOperator("+"));
            setupButton(view, R.id.btnSubtract, v -> setOperator("-"));
            setupButton(view, R.id.btnMultiply, v -> setOperator("×"));
            setupButton(view, R.id.btnDivide, v -> setOperator("÷"));

            // Function buttons
            setupButton(view, R.id.btnAC, v -> clear());
            setupButton(view, R.id.btnEquals, v -> calculate());
            setupButton(view, R.id.btnDecimal, v -> appendDecimal());
            setupButton(view, R.id.btnDelete, v -> deleteLastDigit());
            setupButton(view, R.id.btnPercent, v -> calculatePercent());
            setupButton(view, R.id.btnMemory, v -> handleMemory());

        } catch (Exception e) {
            Log.e("SolverFragment", "Error setting up calculator buttons", e);
        }
    }

    private void setupButton(View view, int buttonId, View.OnClickListener listener) {
        MaterialButton button = view.findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(listener);
        }
    }

    private void appendNumber(String number) {
        if (isNewOperation) {
            currentNumber = number;
            isNewOperation = false;
        } else {
            currentNumber += number;
        }
        updateDisplay(currentNumber);
    }

    private void appendDecimal() {
        if (isNewOperation) {
            currentNumber = "0.";
            isNewOperation = false;
        } else if (!currentNumber.contains(".")) {
            currentNumber += ".";
        }
        updateDisplay(currentNumber);
    }

    private void setOperator(String op) {
        try {
            if (!currentNumber.isEmpty()) {
                if (!operator.isEmpty()) {
                    calculate();
                }
                operator = op;
                result = Double.parseDouble(currentNumber);
                currentNumber = "";
                updateDisplay(decimalFormat.format(result));
            } else if (operator.isEmpty() && display != null) {
                operator = op;
                result = Double.parseDouble(display.getText().toString());
                currentNumber = "";
            }
        } catch (Exception e) {
            Log.e("SolverFragment", "Error setting operator", e);
            clear();
        }
    }

    private void calculate() {
        try {
            if (!currentNumber.isEmpty() && !operator.isEmpty()) {
                double secondNumber = Double.parseDouble(currentNumber);
                switch (operator) {
                    case "+":
                        result += secondNumber;
                        break;
                    case "-":
                        result -= secondNumber;
                        break;
                    case "×":
                        result *= secondNumber;
                        break;
                    case "÷":
                        if (secondNumber != 0) {
                            result /= secondNumber;
                        } else {
                            updateDisplay("Error");
                            return;
                        }
                        break;
                }
                updateDisplay(decimalFormat.format(result));
                currentNumber = "";
                operator = "";
                isNewOperation = true;
            }
        } catch (Exception e) {
            Log.e("SolverFragment", "Error calculating", e);
            clear();
        }
    }

    private void calculatePercent() {
        try {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                if (operator.isEmpty()) {
                    result = number / 100;
                } else {
                    result = result * (number / 100);
                }
                updateDisplay(decimalFormat.format(result));
                currentNumber = "";
                operator = "";
                isNewOperation = true;
            }
        } catch (Exception e) {
            Log.e("SolverFragment", "Error calculating percent", e);
            clear();
        }
    }

    private void handleMemory() {
        try {
            if (display == null) return;
            
            String currentValue = display.getText().toString();
            if (currentValue.equals("Error") || currentValue.isEmpty()) {
                return;
            }

            if (memoryStack.isEmpty()) {
                // Store value in memory
                double value = Double.parseDouble(currentValue);
                memoryStack.push(value);
                isNewOperation = true;
            } else {
                // Recall value from memory
                double value = memoryStack.pop();
                currentNumber = String.valueOf(value);
                updateDisplay(decimalFormat.format(value));
                isNewOperation = false;
            }
        } catch (Exception e) {
            Log.e("SolverFragment", "Error handling memory", e);
            clear();
        }
    }

    private void clear() {
        currentNumber = "";
        operator = "";
        result = 0;
        isNewOperation = true;
        updateDisplay("0");
    }

    private void deleteLastDigit() {
        if (!currentNumber.isEmpty()) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            if (currentNumber.isEmpty()) {
                updateDisplay("0");
                isNewOperation = true;
            } else {
                updateDisplay(currentNumber);
            }
        }
    }

    private void updateDisplay(String value) {
        if (display != null) {
            display.setText(value);
        }
    }
} 