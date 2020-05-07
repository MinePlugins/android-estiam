package fr.mineplugins.android_estiam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private Session session;
    private DatabaseHelper productDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.main_drawer);
        session = new Session(this);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_product);
        }
        reloadData();
        final Menu menu = navigationView.getMenu();
        if(session.getEmail() != ""){
            final View headerLayout = navigationView.getHeaderView(0);
            ImageView profile_pic = headerLayout.findViewById(R.id.nav_profile_pic);
            TextView profile_email = headerLayout.findViewById(R.id.nav_profile_email);
            MenuItem button_disconnect = menu.findItem(R.id.nav_connexion);
            button_disconnect.setTitle("Deconexion");
            button_disconnect.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            profile_email.setText(session.getEmail());
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(80)  // width in px
                    .height(80) // height in px
                    .endConfig()
                    .buildRound(session.getEmail().substring(0,2).toUpperCase(), Color.BLUE);
            profile_pic.setImageDrawable(drawable);
        }

    }
    public void reloadData(){
        NavigationView navigationView = findViewById(R.id.navigation);
        final Menu menu = navigationView.getMenu();
        MenuItem button_favorie = menu.findItem(R.id.nav_favori);
        View favorieView = button_favorie.getActionView();
        TextView favorieCount = favorieView.findViewById(R.id.fav_badge);
        productDB = new DatabaseHelper(this);
        favorieCount.setText(String.valueOf(productDB.getAllFav().getCount()));
        favorieCount.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            reloadData();
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_connexion:
                if(session.getEmail() != "") {
                    session.clear();
                    Toast.makeText(this, "Deconnecte", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
                }
                break;
            case R.id.nav_product:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_favori:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
