package org.pasut.android.remotecontrol.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.pasut.android.remotecontrol.R;
import org.pasut.android.remotecontrol.model.Led;
import org.pasut.android.remotecontrol.services.PreferencesService;
import org.pasut.android.remotecontrol.services.RestService;
import org.pasut.android.remotecontrol.utils.ActivityUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static com.google.common.collect.Lists.newArrayList;
import static org.pasut.android.remotecontrol.R.drawable.light_off;
import static org.pasut.android.remotecontrol.R.drawable.light_on;
import static org.pasut.android.remotecontrol.R.string.led_name_dialog_title;
import static org.pasut.android.remotecontrol.R.string.ok;
import static org.pasut.android.remotecontrol.utils.ActivityUtils.configurarionErrorMessage;


@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String LED_PREFIX = "led";
    private LedListAdapter adapter;
    @Inject
    private RestService restService;

    @Inject
    private PreferencesService preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView loadingImage = (ImageView) findViewById(R.id.loading);

        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImage.getBackground();
        loadingAnimation.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        restService.start(this);
        restService.leds(new RequestListener<List<BigDecimal>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                configurarionErrorMessage(MainActivity.this, RESULT_OK);
            }

            @Override
            public void onRequestSuccess(List<BigDecimal> leds) {
                Log.d(TAG, "RESPONSE" + leds.toString());
                findLedStates(leds);
                adapter = populate();
                ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.switcher);
                switcher.showNext();
            }
        });
    }

    @Override
    protected void onStop() {
        restService.shouldStop();
        super.onStop();
    }

    private void findLedStates(List<BigDecimal> ledIds) {
        List<Led> leds = Lists.transform(ledIds, new Function<BigDecimal, Led>() {
            @Override
            public Led apply(@Nullable BigDecimal input) {
                Led led = new Led(input.intValue());
                if( preferences.contain(LED_PREFIX + led.getId())) {
                    led.setName(preferences.get(LED_PREFIX + led.getId(), String.class));
                }
                return led;
            }
        });
        for (final Led led : leds) {
            Log.d(TAG, "Finding Led State: " + led.getId());
            restService.ledState(led, new RequestListener<Boolean>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    configurarionErrorMessage(MainActivity.this, RESULT_OK);
                }

                @Override
                public void onRequestSuccess(Boolean state) {
                    Log.d(TAG, "Found Led State: " + led.getId() + " " + state);
                    led.setState(state);
                    adapter.addLed(led);
                }
            });
        }
    }

    private LedListAdapter populate() {
        ListView ledsView = (ListView) findViewById(R.id.leds);
        List<Led> list = newArrayList();
        final LedListAdapter adapter = new LedListAdapter(list);
        ledsView.setAdapter(adapter);
        ledsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Led led = (Led) MainActivity.this.adapter.getItem(position);
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setText(led.getName());
                Log.d(TAG, "Led selected: " + led.getId());
                new AlertDialog.Builder(MainActivity.this).setTitle(led_name_dialog_title)
                        .setNeutralButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = input.getText().toString();
                            if (name != null && !name.isEmpty()) {
                                led.setName(name);
                                preferences.put(LED_PREFIX + led.getId(), name);
                                adapter.notifyDataSetChanged();
                            }
                        }
                }).setView(input).show();
                return true;
            }
        });
        return adapter;
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

        public void addLed(Led led) {
            leds.add(led);
            notifyDataSetChanged();
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
            holder.name.setText(led.hasName()? led.getName() : String.valueOf(led.getId()));
            holder.state.setChecked(led.isState());
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean isChecked = holder.state.isChecked();
                    Log.d(TAG,"CHANGE STATE TO: " + isChecked);
                    restService.changeStatus(led, isChecked, new RequestListener<Boolean>() {
                        @Override
                        public void onRequestFailure(SpiceException spiceException) {
                            Log.d(TAG, "Faile to change led " + led.getId() + " to state " + isChecked);
                            holder.state.setChecked(led.isState());
                        }

                        @Override
                        public void onRequestSuccess(Boolean state) {
                            Log.d(TAG, "change led " + led.getId() + " to state " + isChecked);
                            led.setState(isChecked);
                            notifyDataSetChanged();
                        }
                    });
                }
            });
            int imageResource = led.isState() ? light_on : light_off;
               holder.image.setBackgroundResource(imageResource);
            return convertView;
        }
    }
}
