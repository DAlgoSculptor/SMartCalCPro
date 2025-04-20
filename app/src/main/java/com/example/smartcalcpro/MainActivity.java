package com.example.smartcalcpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_THEME = "selected_theme";
    private static final String KEY_DARK_MODE = "dark_mode";
    private SharedPreferences sharedPreferences;
    private boolean isThemeChanging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize SharedPreferences before super.onCreate
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        // Apply theme before super.onCreate
        applyTheme(sharedPreferences.getString(KEY_THEME, "default"));
        applyDarkMode(sharedPreferences.getBoolean(KEY_DARK_MODE, false));
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            
            // Setup the bottom navigation
            BottomNavigationView navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_calculator,
                    R.id.navigation_converter,
                    R.id.navigation_settings)
                    .build();
            
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    public void applyTheme(String theme) {
        if (isThemeChanging) return;
        isThemeChanging = true;
        
        try {
            switch (theme) {
                case "green":
                    setTheme(R.style.Theme_SmartCalcPro_Green);
                    break;
                case "yellow":
                    setTheme(R.style.Theme_SmartCalcPro_Yellow);
                    break;
                case "blue":
                    setTheme(R.style.Theme_SmartCalcPro_Blue);
                    break;
                case "purple":
                    setTheme(R.style.Theme_SmartCalcPro_Purple);
                    break;
                case "default":
                default:
                    setTheme(R.style.Theme_SmartCalcPro);
                    break;
            }
            
            // Save the theme preference
            sharedPreferences.edit()
                    .putString(KEY_THEME, theme)
                    .apply();
            
            // Recreate the activity to apply the new theme
            recreate();
        } finally {
            isThemeChanging = false;
        }
    }

    public void applyDarkMode(boolean isDarkMode) {
        if (isThemeChanging) return;
        isThemeChanging = true;
        
        try {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            
            // Save the dark mode preference
            sharedPreferences.edit()
                    .putBoolean(KEY_DARK_MODE, isDarkMode)
                    .apply();
            
            // Recreate the activity to apply the new theme
            recreate();
        } finally {
            isThemeChanging = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}