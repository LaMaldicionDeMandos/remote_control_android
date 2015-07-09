package org.pasut.android.remotecontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.remotecontrol.R;
import org.pasut.android.remotecontrol.model.Led;
import org.pasut.android.remotecontrol.services.RestService;
import org.pasut.android.remotecontrol.utils.ActivityUtils;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Nullable;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static org.pasut.android.remotecontrol.R.drawable.light_off;
import static org.pasut.android.remotecontrol.R.drawable.light_on;


@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @Inject
    private RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restService.start(this);
        restService.leds(new RequestListener<List<BigDecimal>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                ActivityUtils.configurarionErrorMessage(MainActivity.this, RESULT_OK);
            }

            @Override
            public void onRequestSuccess(List<BigDecimal> leds) {
                Log.d("RESPONSE", leds.toString());
                ListView ledsView = (ListView) findViewById(R.id.leds);
                populate(ledsView, leds);
                ViewSwitcher switcher = (ViewSwitcher)findViewById(R.id.switcher);
                switcher.showNext();
            }
        });
    }

    @Override
    protected void onStop() {
        restService.shouldStop();
        super.onStop();
    }

    private void populate(final ListView ledsView, final List<BigDecimal> intLeds) {
        List<Led> leds = Lists.transform(intLeds, new Function<BigDecimal, Led>() {
            @Override
            public Led apply(@Nullable BigDecimal input) {
                Led led = new Led(input.intValue());
                return led;
            }
        });
        ledsView.setAdapter(new LedListAdapter(leds));
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

    class LedListAdapter extends BaseAdapter {
        private final List<Led> leds;

        private class ViewHolder {
            TextView name;
            Switch state;
            ImageView image;
        }

        public LedListAdapter(final List<Led> leds) {
            super();
            this.leds = leds;
        }
        @Override
        public int getCount() {
            return leds.size();
        }

        @Override
        public Object getItem(int position) {
            return leds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return leds.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.view_item_led, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView)convertView.findViewById(R.id.name);
                holder.state = (Switch)convertView.findViewById(R.id.state);
                holder.image = (ImageView)convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            final Led led = (Led) getItem(position);
            holder.name.setText(String.valueOf(led.getId()));
            holder.state.setChecked(led.isState());
            holder.state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("CHANGE STATE", "TO: " + isChecked);
                    led.setState(isChecked);
                }
            });
            int imageResource = led.isState() ? light_on : light_off;
               holder.image.setBackgroundResource(imageResource);
            return convertView;
        }
    }
}
