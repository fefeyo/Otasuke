package com.fefeyo.otasuke;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fefeyo.otasuke.activities.AddScheduleActivity;
import com.fefeyo.otasuke.adapters.EventListAdapter;
import com.fefeyo.otasuke.databinding.ActivityMainBinding;
import com.fefeyo.otasuke.items.Event;
import com.fefeyo.otasuke.items.EventDetail;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.EventDetailRealmProxy;
import io.realm.EventRealmProxy;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Realm mRealm;
    private SimpleDateFormat mSimpleDateFormat;
    private List<Event> mEventList;
    private List<EventDetail> mEventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mRealm = Realm.getDefaultInstance();
        mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
        mEventList = mRealm.where(Event.class).findAll();
        mEventDetails = mRealm.where(EventDetail.class).findAll();

        initCalendar();
        initButtons();
    }

    private void initCalendar() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date now = new Date();

        List<Date> checkedList = new ArrayList<>();
        for (EventDetail detail : mEventDetails) {
            try {
                Date checkDay = mSimpleDateFormat.parse(detail.getDate());
                int diff = checkDay.compareTo(now);
                if (diff < 0) {
                    checkedList.add(checkDay);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mBinding.monthCalendar.init(
                now,
                nextYear.getTime()
        ).withSelectedDate(now).withHighlightedDates(checkedList);

        mBinding.monthCalendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String day = mSimpleDateFormat.format(date);
                List<Event> todayEvents = new ArrayList<>();
                boolean exist = false;
                for (Event e : mEventList) {
                    for (EventDetail ed : e.getEventDetails()) {
                        if (day.equals(ed.getDate())) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist) {
                        todayEvents.add(e);
                        exist = false;
                    }
                }

                if (todayEvents.size() > 0) {
                    mBinding.dayEventList.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInUp)
                            .duration(500)
                            .playOn(mBinding.dayEventList);
                    initList(todayEvents);
                } else {
                    if (mBinding.dayEventList.getVisibility() == View.VISIBLE) {
                        YoYo.with(Techniques.SlideInUp)
                                .duration(500)
                                .onEnd((animator -> {
                                    mBinding.dayEventList.setVisibility(View.INVISIBLE);
                                }))
                                .playOn(mBinding.dayEventList);
                    }
                }


            }

            @Override
            public void onDateUnselected(Date date) {
                YoYo.with(Techniques.SlideOutDown)
                        .duration(500)
                        .playOn(mBinding.dayEventList);
            }
        });
    }

    private void initButtons() {
        mBinding.floatingmenu.addButton(makeActionButton(
                "スケジュール追加",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_event_white_24dp,
                (v -> {
                    Intent i = new Intent(getApplicationContext(), AddScheduleActivity.class);
                    startActivity(i);
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "チケット情報追加",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_payment_white_24dp,
                (v -> {
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "週間スケジュール",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    mBinding.floatingmenu.toggle();
                })
        ));
    }

    private void initList(final List<Event> events) {
        EventListAdapter adapter = new EventListAdapter(getApplicationContext());
        adapter.addAll(events);
        mBinding.dayEventList.setAdapter(adapter);
    }

    private FloatingActionButton makeActionButton(String title, int colorNormal, int colorPressed, int iconRes, View.OnClickListener listener) {
        colorNormal = ContextCompat.getColor(getApplicationContext(), colorNormal);
        colorPressed = ContextCompat.getColor(getApplicationContext(), colorPressed);
        FloatingActionButton button = new FloatingActionButton(getApplicationContext());
        button.setTitle(title);
        button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setColorNormal(colorNormal);
        button.setColorPressed(colorPressed);
        button.setIcon(iconRes);
        button.setStrokeVisible(true);
        button.setOnClickListener(listener);

        return button;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Log.d("設定", "設定");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
