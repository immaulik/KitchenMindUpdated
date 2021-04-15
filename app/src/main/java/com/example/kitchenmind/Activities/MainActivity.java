package com.example.kitchenmind.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.kitchenmind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    final String MY_PREFS_NAME = "MyPrefsFile";
    String userid;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String theme;

    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else
        {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusettings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(MainActivity.this, settingsActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor=prefs.edit();
        userid = prefs.getString("userid", "nothing");
        theme = prefs.getString("theme","dark");
        Log.d("theme",theme);
        Toast.makeText(this, theme, Toast.LENGTH_SHORT).show();
        if(theme.contains("dark"))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString("theme","dark");
            editor.apply();
        }
        if(theme.contains("white"))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString("theme","white");
            editor.apply();
        }
//        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();
        if (userid.equals("nothing")) {
//            Toast.makeText(this, "Not Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
//            Toast.makeText(this, "Login!!", Toast.LENGTH_SHORT).show();
        }
    }
}
