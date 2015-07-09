package org.pasut.android.remotecontrol.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.pasut.android.remotecontrol.R;

import java.util.List;

import javax.annotation.Nullable;

import static android.R.layout.simple_list_item_1;


public class MainActivity extends Activity {
    public final static String LEDS_KEY = "leds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView ledsView = (ListView)findViewById(R.id.leds);
        if(getIntent().hasExtra(LEDS_KEY)) {
            List<Integer> intLeds = getIntent().getIntegerArrayListExtra(LEDS_KEY);
            List<String> leds = Lists.transform(intLeds, new Function<Integer, String>() {
                @Override
                public String apply(@Nullable Integer input) {
                    return input.toString();
                }
            });
            ledsView.setAdapter(new ArrayAdapter<>(this, simple_list_item_1, leds));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
