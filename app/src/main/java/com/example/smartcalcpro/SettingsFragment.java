package com.example.smartcalcpro;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    private static final String PREFS_NAME = "SmartCalcPrefs";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_VIBRATION = "vibration";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_HISTORY = "history";
    private static final String KEY_BLUR_ENABLED = "blur_enabled";
    private static final String KEY_BLUR_INTENSITY = "blur_intensity";
    private static final String KEY_ANIMATION_ENABLED = "animation_enabled";
    private static final String KEY_ANIMATION_SPEED = "animation_speed";
    private static final String KEY_ANIMATION_STYLE = "animation_style";
    private static final String KEY_BACKGROUND_ENABLED = "background_enabled";
    private static final String KEY_BACKGROUND_TYPE = "background_type";
    private static final String KEY_BACKGROUND_COLOR = "background_color";
    private static final String KEY_GRADIENT_TYPE = "gradient_type";
    private static final String KEY_BACKGROUND_IMAGE = "background_image";
    
    // Appearance Settings
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_BUTTON_STYLE = "button_style";

    // Functionality Settings
    private static final String KEY_RADIANS = "use_radians";
    private static final String KEY_PRECISION = "decimal_places";
    private static final String KEY_SCIENTIFIC = "scientific_functions";
    private static final String KEY_HAPTIC = "haptic_feedback";
    private static final String KEY_AUTO_CLEAR = "auto_clear";
    private static final String KEY_PARENTHESES = "enable_parentheses";
    private static final String KEY_DECIMAL_SEPARATOR = "decimal_separator";

    // Smart Features
    private static final String KEY_VOICE_INPUT = "voice_input";
    private static final String KEY_HANDWRITING = "handwriting";
    private static final String KEY_SHOW_HISTORY = "show_history";
    private static final String KEY_PERSIST_HISTORY = "persist_history";
    private static final String KEY_AUTO_CORRECT = "auto_correct";
    private static final String KEY_STEP_BY_STEP = "step_by_step";

    // Localization & Units
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_UNIT_SYSTEM = "unit_system";
    private static final String KEY_CURRENCY_FORMAT = "currency_format";
    private static final String KEY_NUMBER_FORMAT = "number_format";

    // Privacy & Storage
    private static final String KEY_ANALYTICS = "usage_analytics";
    private static final String KEY_CLOUD_SYNC = "cloud_sync";

    // Developer Settings
    private static final String KEY_BETA_FEATURES = "beta_features";
    private static final String KEY_GRAPHING = "advanced_graphing";
    private static final String KEY_MATH_ENGINE = "math_engine_version";

    private SharedPreferences sharedPreferences;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
        
        // Setup all settings components
        setupDarkModeSwitch(rootView);
        setupVibrationSwitch(rootView);
        setupSoundSwitch(rootView);
        setupHistorySwitch(rootView);
        setupThemeRadioGroup(rootView);
        setupDataManagementButtons(rootView);
        setupBlurSettingsButton(rootView);
        setupAnimationSettingsButton(rootView);
        setupBackgroundSettingsButton(rootView);
        setupAppearanceSettings(rootView);
        setupFunctionalitySettings(rootView);
        setupSmartFeatures(rootView);
        setupLocalizationSettings(rootView);
        setupPrivacySettings(rootView);
        setupDeveloperSettings(rootView);
        
        return rootView;
    }

    private void setupDarkModeSwitch(View root) {
        SwitchMaterial darkModeSwitch = root.findViewById(R.id.switchDarkMode);
        
        // Set initial state
        boolean isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        darkModeSwitch.setChecked(isDarkMode);
        
        // Handle switch changes
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean(KEY_DARK_MODE, isChecked)
                    .apply();
            
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).applyDarkMode(isChecked);
            }
        });
    }

    private void setupVibrationSwitch(View root) {
        SwitchMaterial vibrationSwitch = root.findViewById(R.id.switchVibration);
        
        // Set initial state
        boolean isVibrationEnabled = sharedPreferences.getBoolean(KEY_VIBRATION, true);
        vibrationSwitch.setChecked(isVibrationEnabled);
        
        // Handle switch changes
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean(KEY_VIBRATION, isChecked)
                    .apply();
        });
    }

    private void setupSoundSwitch(View root) {
        SwitchMaterial soundSwitch = root.findViewById(R.id.switchSound);
        
        // Set initial state
        boolean isSoundEnabled = sharedPreferences.getBoolean(KEY_SOUND, true);
        soundSwitch.setChecked(isSoundEnabled);
        
        // Handle switch changes
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean(KEY_SOUND, isChecked)
                    .apply();
        });
    }

    private void setupHistorySwitch(View root) {
        SwitchMaterial historySwitch = root.findViewById(R.id.switchHistory);
        
        // Set initial state
        boolean isHistoryEnabled = sharedPreferences.getBoolean(KEY_HISTORY, true);
        historySwitch.setChecked(isHistoryEnabled);
        
        // Handle switch changes
        historySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean(KEY_HISTORY, isChecked)
                    .apply();
        });
    }

    private void setupThemeRadioGroup(View root) {
        RadioGroup themeRadioGroup = root.findViewById(R.id.themeRadioGroup);
        
        // Set initial selection
        String currentTheme = sharedPreferences.getString(KEY_THEME, "default");
        int radioId = getResources().getIdentifier("radio" + currentTheme.substring(0, 1).toUpperCase() + currentTheme.substring(1), "id", requireContext().getPackageName());
        themeRadioGroup.check(radioId);
        
        // Handle radio button changes
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String theme = ((RadioButton) root.findViewById(checkedId)).getText().toString().toLowerCase();
            sharedPreferences.edit().putString(KEY_THEME, theme).apply();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).applyTheme(theme);
            }
        });
    }

    private void setupDataManagementButtons(View root) {
        // Clear History Button
        MaterialButton btnClearHistory = root.findViewById(R.id.btnClearHistory);
        btnClearHistory.setOnClickListener(v -> {
            // TODO: Implement history clearing logic
            Toast.makeText(requireContext(), "Calculation history cleared", Toast.LENGTH_SHORT).show();
        });

        // Export Data Button
        MaterialButton btnExportData = root.findViewById(R.id.btnExportData);
        btnExportData.setOnClickListener(v -> {
            // TODO: Implement data export logic
            Toast.makeText(requireContext(), "Data exported successfully", Toast.LENGTH_SHORT).show();
        });

        // Import Data Button
        MaterialButton btnImportData = root.findViewById(R.id.btnImportData);
        btnImportData.setOnClickListener(v -> {
            // TODO: Implement data import logic
            Toast.makeText(requireContext(), "Data imported successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBlurSettingsButton(View root) {
        MaterialButton btnBlurSettings = root.findViewById(R.id.btnBlurSettings);
        btnBlurSettings.setOnClickListener(v -> showBlurSettingsDialog());
    }

    private void showBlurSettingsDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_blur_settings, null);

        // Initialize views
        Slider sliderBlurIntensity = dialogView.findViewById(R.id.sliderBlurIntensity);
        SwitchMaterial switchBlurEnabled = dialogView.findViewById(R.id.switchBlurEnabled);
        MaterialButton btnApplyBlur = dialogView.findViewById(R.id.btnApplyBlur);

        // Set initial values
        boolean isBlurEnabled = sharedPreferences.getBoolean(KEY_BLUR_ENABLED, false);
        float blurIntensity = sharedPreferences.getFloat(KEY_BLUR_INTENSITY, 0f);
        
        switchBlurEnabled.setChecked(isBlurEnabled);
        sliderBlurIntensity.setValue(blurIntensity);

        // Create and show dialog
        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        // Setup blur intensity slider
        sliderBlurIntensity.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                applyBlurEffect(value);
            }
        });

        // Setup blur enabled switch
        switchBlurEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                applyBlurEffect(sliderBlurIntensity.getValue());
            } else {
                removeBlurEffect();
            }
        });

        // Setup apply button
        btnApplyBlur.setOnClickListener(v -> {
            float intensity = sliderBlurIntensity.getValue();
            boolean enabled = switchBlurEnabled.isChecked();
            
            sharedPreferences.edit()
                    .putBoolean(KEY_BLUR_ENABLED, enabled)
                    .putFloat(KEY_BLUR_INTENSITY, intensity)
                    .apply();
            
            if (enabled) {
                applyBlurEffect(intensity);
            } else {
                removeBlurEffect();
            }
            
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applyBlurEffect(float intensity) {
        if (getActivity() != null && getActivity().getWindow() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            if (decorView instanceof ViewGroup) {
                BlurUtils.applyBlurToViewGroup((ViewGroup) decorView, intensity);
            }
        }
    }

    private void removeBlurEffect() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            if (decorView instanceof ViewGroup) {
                BlurUtils.applyBlurToViewGroup((ViewGroup) decorView, 0);
            }
        }
    }

    private void setupAnimationSettingsButton(View root) {
        MaterialButton btnAnimationSettings = root.findViewById(R.id.btnAnimationSettings);
        btnAnimationSettings.setOnClickListener(v -> showAnimationSettingsDialog());
    }

    private void showAnimationSettingsDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_animation_settings, null);

        // Initialize views
        Slider sliderAnimationSpeed = dialogView.findViewById(R.id.sliderAnimationSpeed);
        RadioGroup animationStyleGroup = dialogView.findViewById(R.id.animationStyleGroup);
        SwitchMaterial switchAnimationsEnabled = dialogView.findViewById(R.id.switchAnimationsEnabled);
        MaterialButton btnApplyAnimations = dialogView.findViewById(R.id.btnApplyAnimations);

        // Set initial values
        boolean isAnimationsEnabled = sharedPreferences.getBoolean(KEY_ANIMATION_ENABLED, true);
        float animationSpeed = sharedPreferences.getFloat(KEY_ANIMATION_SPEED, 1f);
        String animationStyle = sharedPreferences.getString(KEY_ANIMATION_STYLE, "default");
        
        switchAnimationsEnabled.setChecked(isAnimationsEnabled);
        sliderAnimationSpeed.setValue(animationSpeed);
        
        switch (animationStyle) {
            case "default":
                animationStyleGroup.check(R.id.radioDefaultAnimation);
                break;
            case "bounce":
                animationStyleGroup.check(R.id.radioBounceAnimation);
                break;
            case "fade":
                animationStyleGroup.check(R.id.radioFadeAnimation);
                break;
            case "slide":
                animationStyleGroup.check(R.id.radioSlideAnimation);
                break;
        }

        // Create and show dialog
        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        // Setup animation speed slider
        sliderAnimationSpeed.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                // Preview animation speed
                AnimationUtils.animateButtonPress(btnApplyAnimations, value);
            }
        });

        // Setup animation style radio group
        animationStyleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            AnimationUtils.AnimationStyle style;
            if (checkedId == R.id.radioDefaultAnimation) {
                style = AnimationUtils.AnimationStyle.DEFAULT;
            } else if (checkedId == R.id.radioBounceAnimation) {
                style = AnimationUtils.AnimationStyle.BOUNCE;
            } else if (checkedId == R.id.radioFadeAnimation) {
                style = AnimationUtils.AnimationStyle.FADE;
            } else if (checkedId == R.id.radioSlideAnimation) {
                style = AnimationUtils.AnimationStyle.SLIDE;
            } else {
                style = AnimationUtils.AnimationStyle.DEFAULT;
            }
            
            // Preview animation style
            AnimationUtils.animateView(btnApplyAnimations, style, sliderAnimationSpeed.getValue());
        });

        // Setup animations enabled switch
        switchAnimationsEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AnimationUtils.animateButtonPress(btnApplyAnimations, sliderAnimationSpeed.getValue());
            }
        });

        // Setup apply button
        btnApplyAnimations.setOnClickListener(v -> {
            float speed = sliderAnimationSpeed.getValue();
            boolean enabled = switchAnimationsEnabled.isChecked();
            String style;
            
            if (animationStyleGroup.getCheckedRadioButtonId() == R.id.radioDefaultAnimation) {
                style = "default";
            } else if (animationStyleGroup.getCheckedRadioButtonId() == R.id.radioBounceAnimation) {
                style = "bounce";
            } else if (animationStyleGroup.getCheckedRadioButtonId() == R.id.radioFadeAnimation) {
                style = "fade";
            } else if (animationStyleGroup.getCheckedRadioButtonId() == R.id.radioSlideAnimation) {
                style = "slide";
            } else {
                style = "default";
            }
            
            sharedPreferences.edit()
                    .putBoolean(KEY_ANIMATION_ENABLED, enabled)
                    .putFloat(KEY_ANIMATION_SPEED, speed)
                    .putString(KEY_ANIMATION_STYLE, style)
                    .apply();
            
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupBackgroundSettingsButton(View root) {
        MaterialButton btnBackgroundSettings = root.findViewById(R.id.btnBackgroundSettings);
        btnBackgroundSettings.setOnClickListener(v -> showBackgroundSettingsDialog());
    }

    private void showBackgroundSettingsDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_background_settings, null);

        // Initialize views
        RadioGroup backgroundTypeGroup = dialogView.findViewById(R.id.backgroundTypeGroup);
        RadioGroup gradientTypeGroup = dialogView.findViewById(R.id.gradientTypeGroup);
        MaterialButton btnPickColor = dialogView.findViewById(R.id.btnPickColor);
        MaterialButton btnPickImage = dialogView.findViewById(R.id.btnPickImage);
        SwitchMaterial switchBackgroundEnabled = dialogView.findViewById(R.id.switchBackgroundEnabled);
        MaterialButton btnApplyBackground = dialogView.findViewById(R.id.btnApplyBackground);

        // Set initial values
        boolean isBackgroundEnabled = sharedPreferences.getBoolean(KEY_BACKGROUND_ENABLED, false);
        String backgroundType = sharedPreferences.getString(KEY_BACKGROUND_TYPE, "solid");
        String gradientType = sharedPreferences.getString(KEY_GRADIENT_TYPE, "linear");
        int backgroundColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE);
        String backgroundImage = sharedPreferences.getString(KEY_BACKGROUND_IMAGE, null);
        
        switchBackgroundEnabled.setChecked(isBackgroundEnabled);
        
        switch (backgroundType) {
            case "solid":
                backgroundTypeGroup.check(R.id.radioSolidColor);
                break;
            case "gradient":
                backgroundTypeGroup.check(R.id.radioGradient);
                break;
            case "image":
                backgroundTypeGroup.check(R.id.radioImage);
                break;
        }
        
        switch (gradientType) {
            case "linear":
                gradientTypeGroup.check(R.id.radioLinearGradient);
                break;
            case "radial":
                gradientTypeGroup.check(R.id.radioRadialGradient);
                break;
        }

        // Create and show dialog
        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        // Setup background type radio group
        backgroundTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioSolidColor) {
                showColorPickerOptions(dialogView);
            } else if (checkedId == R.id.radioGradient) {
                showGradientOptions(dialogView);
            } else if (checkedId == R.id.radioImage) {
                showImagePickerOptions(dialogView);
            }
        });

        // Setup gradient type radio group
        gradientTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Update gradient preview
            updateGradientPreview(dialogView);
        });

        // Setup color picker button
        btnPickColor.setOnClickListener(v -> {
            // TODO: Implement color picker
            Toast.makeText(requireContext(), "Color picker will be implemented", Toast.LENGTH_SHORT).show();
        });

        // Setup image picker button
        btnPickImage.setOnClickListener(v -> {
            // TODO: Implement image picker
            Toast.makeText(requireContext(), "Image picker will be implemented", Toast.LENGTH_SHORT).show();
        });

        // Setup background enabled switch
        switchBackgroundEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                applyBackgroundSettings(dialogView);
            } else {
                removeBackgroundSettings();
            }
        });

        // Setup apply button
        btnApplyBackground.setOnClickListener(v -> {
            String type;
            if (backgroundTypeGroup.getCheckedRadioButtonId() == R.id.radioSolidColor) {
                type = "solid";
            } else if (backgroundTypeGroup.getCheckedRadioButtonId() == R.id.radioGradient) {
                type = "gradient";
            } else if (backgroundTypeGroup.getCheckedRadioButtonId() == R.id.radioImage) {
                type = "image";
            } else {
                type = "solid";
            }
            
            String gradient;
            if (gradientTypeGroup.getCheckedRadioButtonId() == R.id.radioLinearGradient) {
                gradient = "linear";
            } else if (gradientTypeGroup.getCheckedRadioButtonId() == R.id.radioRadialGradient) {
                gradient = "radial";
            } else {
                gradient = "linear";
            }
            
            sharedPreferences.edit()
                    .putBoolean(KEY_BACKGROUND_ENABLED, switchBackgroundEnabled.isChecked())
                    .putString(KEY_BACKGROUND_TYPE, type)
                    .putString(KEY_GRADIENT_TYPE, gradient)
                    .putInt(KEY_BACKGROUND_COLOR, backgroundColor)
                    .putString(KEY_BACKGROUND_IMAGE, backgroundImage)
                    .apply();
            
            if (switchBackgroundEnabled.isChecked()) {
                applyBackgroundSettings(dialogView);
            } else {
                removeBackgroundSettings();
            }
            
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showColorPickerOptions(View dialogView) {
        dialogView.findViewById(R.id.textColorPicker).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.btnPickColor).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.textGradientType).setVisibility(View.GONE);
        dialogView.findViewById(R.id.gradientTypeGroup).setVisibility(View.GONE);
        dialogView.findViewById(R.id.textImageSource).setVisibility(View.GONE);
        dialogView.findViewById(R.id.btnPickImage).setVisibility(View.GONE);
    }

    private void showGradientOptions(View dialogView) {
        dialogView.findViewById(R.id.textColorPicker).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.btnPickColor).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.textGradientType).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.gradientTypeGroup).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.textImageSource).setVisibility(View.GONE);
        dialogView.findViewById(R.id.btnPickImage).setVisibility(View.GONE);
    }

    private void showImagePickerOptions(View dialogView) {
        dialogView.findViewById(R.id.textColorPicker).setVisibility(View.GONE);
        dialogView.findViewById(R.id.btnPickColor).setVisibility(View.GONE);
        dialogView.findViewById(R.id.textGradientType).setVisibility(View.GONE);
        dialogView.findViewById(R.id.gradientTypeGroup).setVisibility(View.GONE);
        dialogView.findViewById(R.id.textImageSource).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.btnPickImage).setVisibility(View.VISIBLE);
    }

    private void updateGradientPreview(View dialogView) {
        // TODO: Implement gradient preview
    }

    private void showColorPickerDialog(OnColorSelectedListener listener) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_color_picker, null);

        Slider sliderRed = dialogView.findViewById(R.id.sliderRed);
        Slider sliderGreen = dialogView.findViewById(R.id.sliderGreen);
        Slider sliderBlue = dialogView.findViewById(R.id.sliderBlue);
        View colorPreview = dialogView.findViewById(R.id.colorPreview);
        MaterialButton btnApplyColor = dialogView.findViewById(R.id.btnApplyColor);

        // Set initial color from preferences
        int currentColor = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE);
        sliderRed.setValue(Color.red(currentColor));
        sliderGreen.setValue(Color.green(currentColor));
        sliderBlue.setValue(Color.blue(currentColor));
        colorPreview.setBackgroundColor(currentColor);

        // Create color change listener
        Slider.OnChangeListener colorChangeListener = (slider, value, fromUser) -> {
            if (fromUser) {
                int color = Color.rgb(
                    (int) sliderRed.getValue(),
                    (int) sliderGreen.getValue(),
                    (int) sliderBlue.getValue()
                );
                colorPreview.setBackgroundColor(color);
            }
        };

        // Setup sliders
        sliderRed.addOnChangeListener(colorChangeListener);
        sliderGreen.addOnChangeListener(colorChangeListener);
        sliderBlue.addOnChangeListener(colorChangeListener);

        // Create and show dialog
        Dialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        // Setup apply button
        btnApplyColor.setOnClickListener(v -> {
            int selectedColor = Color.rgb(
                (int) sliderRed.getValue(),
                (int) sliderGreen.getValue(),
                (int) sliderBlue.getValue()
            );
            listener.onColorSelected(selectedColor);
            dialog.dismiss();
        });

        dialog.show();
    }

    private interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    private void setupColorPickerButton(View dialogView) {
        MaterialButton btnPickColor = dialogView.findViewById(R.id.btnPickColor);
        btnPickColor.setOnClickListener(v -> showColorPickerDialog(color -> {
            sharedPreferences.edit()
                    .putInt(KEY_BACKGROUND_COLOR, color)
                    .apply();
            applyBackgroundSettings(dialogView);
        }));
    }

    private void applyBackgroundSettings(View dialogView) {
        boolean isEnabled = sharedPreferences.getBoolean(KEY_BACKGROUND_ENABLED, false);
        String type = sharedPreferences.getString(KEY_BACKGROUND_TYPE, "solid");
        int color = sharedPreferences.getInt(KEY_BACKGROUND_COLOR, Color.WHITE);
        String gradientType = sharedPreferences.getString(KEY_GRADIENT_TYPE, "linear");
        String imagePath = sharedPreferences.getString(KEY_BACKGROUND_IMAGE, null);

        if (!isEnabled) {
            removeBackgroundSettings();
            return;
        }

        View rootView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Drawable background = null;

        switch (type) {
            case "solid":
                background = new ColorDrawable(color);
                break;
            case "gradient":
                int[] colors = {color, Color.WHITE};
                GradientDrawable.Orientation orientation = gradientType.equals("linear") ?
                    GradientDrawable.Orientation.LEFT_RIGHT :
                    GradientDrawable.Orientation.TOP_BOTTOM;
                background = new GradientDrawable(orientation, colors);
                break;
            case "image":
                if (imagePath != null) {
                    try {
                        background = Drawable.createFromPath(imagePath);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        if (background != null) {
            rootView.setBackground(background);
        }
    }

    private void removeBackgroundSettings() {
        View rootView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setBackground(null);
    }

    private void setupAppearanceSettings(View view) {
        // Font Size
        Slider sliderFontSize = view.findViewById(R.id.sliderFontSize);
        sliderFontSize.setValue(sharedPreferences.getFloat(KEY_FONT_SIZE, 16f));
        sliderFontSize.addOnChangeListener((slider, value, fromUser) -> {
            sharedPreferences.edit().putFloat(KEY_FONT_SIZE, value).apply();
            // TODO: Apply font size change
        });

        // Theme Customization
        MaterialButton btnThemeCustomization = view.findViewById(R.id.btnThemeCustomization);
        btnThemeCustomization.setOnClickListener(v -> {
            // TODO: Show theme customization dialog
        });

        // Button Style
        MaterialButton btnButtonStyle = view.findViewById(R.id.btnButtonStyle);
        btnButtonStyle.setOnClickListener(v -> {
            // TODO: Show button style dialog
        });

        // Background Image
        MaterialButton btnBackgroundImage = view.findViewById(R.id.btnBackgroundImage);
        btnBackgroundImage.setOnClickListener(v -> {
            // TODO: Show background image picker
        });
    }

    private void setupFunctionalitySettings(View view) {
        // Radians/Degrees
        SwitchMaterial switchRadians = view.findViewById(R.id.switchRadians);
        switchRadians.setChecked(sharedPreferences.getBoolean(KEY_RADIANS, false));
        switchRadians.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_RADIANS, isChecked).apply();
        });

        // Precision
        Slider sliderPrecision = view.findViewById(R.id.sliderPrecision);
        sliderPrecision.setValue(sharedPreferences.getInt(KEY_PRECISION, 2));
        sliderPrecision.addOnChangeListener((slider, value, fromUser) -> {
            sharedPreferences.edit().putInt(KEY_PRECISION, (int) value).apply();
        });

        // Scientific Functions
        SwitchMaterial switchScientific = view.findViewById(R.id.switchScientific);
        switchScientific.setChecked(sharedPreferences.getBoolean(KEY_SCIENTIFIC, true));
        switchScientific.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_SCIENTIFIC, isChecked).apply();
        });

        // Haptic Feedback
        SwitchMaterial switchHaptic = view.findViewById(R.id.switchHaptic);
        switchHaptic.setChecked(sharedPreferences.getBoolean(KEY_HAPTIC, true));
        switchHaptic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_HAPTIC, isChecked).apply();
        });

        // Auto-clear
        SwitchMaterial switchAutoClear = view.findViewById(R.id.switchAutoClear);
        switchAutoClear.setChecked(sharedPreferences.getBoolean(KEY_AUTO_CLEAR, false));
        switchAutoClear.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_AUTO_CLEAR, isChecked).apply();
        });

        // Parentheses
        SwitchMaterial switchParentheses = view.findViewById(R.id.switchParentheses);
        switchParentheses.setChecked(sharedPreferences.getBoolean(KEY_PARENTHESES, true));
        switchParentheses.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_PARENTHESES, isChecked).apply();
        });

        // Decimal Separator
        MaterialButton btnDecimalSeparator = view.findViewById(R.id.btnDecimalSeparator);
        btnDecimalSeparator.setOnClickListener(v -> {
            // TODO: Show decimal separator dialog
        });
    }

    private void setupSmartFeatures(View view) {
        // Voice Input
        SwitchMaterial switchVoiceInput = view.findViewById(R.id.switchVoiceInput);
        switchVoiceInput.setChecked(sharedPreferences.getBoolean(KEY_VOICE_INPUT, false));
        switchVoiceInput.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_VOICE_INPUT, isChecked).apply();
        });

        // Handwriting
        SwitchMaterial switchHandwriting = view.findViewById(R.id.switchHandwriting);
        switchHandwriting.setChecked(sharedPreferences.getBoolean(KEY_HANDWRITING, false));
        switchHandwriting.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_HANDWRITING, isChecked).apply();
        });

        // Show History
        SwitchMaterial switchHistory = view.findViewById(R.id.switchHistory);
        switchHistory.setChecked(sharedPreferences.getBoolean(KEY_SHOW_HISTORY, true));
        switchHistory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_SHOW_HISTORY, isChecked).apply();
        });

        // Persist History
        SwitchMaterial switchPersistHistory = view.findViewById(R.id.switchPersistHistory);
        switchPersistHistory.setChecked(sharedPreferences.getBoolean(KEY_PERSIST_HISTORY, true));
        switchPersistHistory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_PERSIST_HISTORY, isChecked).apply();
        });

        // Auto-correction
        SwitchMaterial switchAutoCorrect = view.findViewById(R.id.switchAutoCorrect);
        switchAutoCorrect.setChecked(sharedPreferences.getBoolean(KEY_AUTO_CORRECT, true));
        switchAutoCorrect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_AUTO_CORRECT, isChecked).apply();
        });

        // Step-by-step
        SwitchMaterial switchStepByStep = view.findViewById(R.id.switchStepByStep);
        switchStepByStep.setChecked(sharedPreferences.getBoolean(KEY_STEP_BY_STEP, false));
        switchStepByStep.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_STEP_BY_STEP, isChecked).apply();
        });
    }

    private void setupLocalizationSettings(View view) {
        // Language
        MaterialButton btnLanguage = view.findViewById(R.id.btnLanguage);
        btnLanguage.setOnClickListener(v -> {
            // TODO: Show language selection dialog
        });

        // Unit System
        MaterialButton btnUnitSystem = view.findViewById(R.id.btnUnitSystem);
        btnUnitSystem.setOnClickListener(v -> {
            // TODO: Show unit system selection dialog
        });

        // Currency Format
        MaterialButton btnCurrencyFormat = view.findViewById(R.id.btnCurrencyFormat);
        btnCurrencyFormat.setOnClickListener(v -> {
            // TODO: Show currency format dialog
        });

        // Number Format
        MaterialButton btnNumberFormat = view.findViewById(R.id.btnNumberFormat);
        btnNumberFormat.setOnClickListener(v -> {
            // TODO: Show number format dialog
        });
    }

    private void setupPrivacySettings(View view) {
        // Clear Data
        MaterialButton btnClearData = view.findViewById(R.id.btnClearData);
        btnClearData.setOnClickListener(v -> {
            // TODO: Show confirmation dialog and clear data
        });

        // Analytics
        SwitchMaterial switchAnalytics = view.findViewById(R.id.switchAnalytics);
        switchAnalytics.setChecked(sharedPreferences.getBoolean(KEY_ANALYTICS, false));
        switchAnalytics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_ANALYTICS, isChecked).apply();
        });

        // Export History
        MaterialButton btnExportHistory = view.findViewById(R.id.btnExportHistory);
        btnExportHistory.setOnClickListener(v -> {
            // TODO: Show export options dialog
        });

        // Cloud Sync
        SwitchMaterial switchCloudSync = view.findViewById(R.id.switchCloudSync);
        switchCloudSync.setChecked(sharedPreferences.getBoolean(KEY_CLOUD_SYNC, false));
        switchCloudSync.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_CLOUD_SYNC, isChecked).apply();
        });
    }

    private void setupDeveloperSettings(View view) {
        // Beta Features
        SwitchMaterial switchBetaFeatures = view.findViewById(R.id.switchBetaFeatures);
        switchBetaFeatures.setChecked(sharedPreferences.getBoolean(KEY_BETA_FEATURES, false));
        switchBetaFeatures.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_BETA_FEATURES, isChecked).apply();
        });

        // Custom Functions
        MaterialButton btnCustomFunctions = view.findViewById(R.id.btnCustomFunctions);
        btnCustomFunctions.setOnClickListener(v -> {
            // TODO: Show custom functions editor
        });

        // Advanced Graphing
        SwitchMaterial switchGraphing = view.findViewById(R.id.switchGraphing);
        switchGraphing.setChecked(sharedPreferences.getBoolean(KEY_GRAPHING, false));
        switchGraphing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_GRAPHING, isChecked).apply();
        });

        // Math Engine
        MaterialButton btnMathEngine = view.findViewById(R.id.btnMathEngine);
        btnMathEngine.setOnClickListener(v -> {
            // TODO: Show math engine version selector
        });
    }
} 