package com.example.censusmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.censusmap.fragments.DataFragment;
import com.example.censusmap.fragments.MainFragment;
import com.example.censusmap.fragments.OnQuerySubmitListener;
import com.example.censusmap.fragments.FragmentInterface;
import com.example.censusmap.fragments.OnQueryTextChangeListener;
import com.example.censusmap.fragments.SplashFragment;

public class MainActivity extends AppCompatActivity implements FragmentInterface {

    android.support.v7.widget.Toolbar toolBar;
    OnQuerySubmitListener barQueryListener;
    OnQueryTextChangeListener barTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getSupportActionBar().hide();
        // befor enew fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, SplashFragment.newInstance())
                .commit();
    }

    @Override
    public void moveToMainScreen() {
        MainFragment mainFragment = MainFragment.newInstance();
        barQueryListener = mainFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, mainFragment)
                .commit();

        setToolBar();
    }

    private void setToolBar() {
        toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_developer_info);
        setSearchView();
    }

    @Override
    public void moveToDetailsScreen(String zipCode) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, DataFragment.newInstance(zipCode))
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.developer_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer_contact:
                developerInfo().show();
                break;
        }
        return true;
    }

    public AlertDialog developerInfo() {
        AlertDialog.Builder devInfo = new AlertDialog.Builder(MainActivity.this);
        devInfo.setTitle(R.string.developer_info_text)
                .setItems(R.array.developer_contact_text, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent myEmail = new Intent(Intent.ACTION_SEND);
                            myEmail.setType("text/email");
                            myEmail.putExtra(Intent.EXTRA_EMAIL,
                                    new String[]{"talharahman@pursuit.org"});
                            myEmail.putExtra(Intent.EXTRA_SUBJECT,
                                    "Hello Talha");
                            myEmail.putExtra(Intent.EXTRA_TEXT, "Dear Talha," + "");
                            startActivity(Intent.createChooser(myEmail, "Send Message:"));
                            break;
                        case 1:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/talharahman/Census-Map")));
                            break;
                        case 2:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/talha-rahman-799516174/")));
                            break;
                    }
                });
        return devInfo.create();
    }

    private void setSearchView() {
        SearchView searchView = findViewById(R.id.map_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                barQueryListener.onQuerySubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                //  barTextListener.onQueryChange(filter);
                return false;
            }
        });

    }

}
