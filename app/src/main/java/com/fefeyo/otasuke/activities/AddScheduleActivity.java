package com.fefeyo.otasuke.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.adapters.CommitEventAdapter;
import com.fefeyo.otasuke.adapters.EventListAdapter;
import com.fefeyo.otasuke.databinding.ActivityAddScheduleBinding;
import com.fefeyo.otasuke.fragments.EventDetailDialogFragment;
import com.fefeyo.otasuke.items.Event;
import com.fefeyo.otasuke.items.EventDetail;
import com.fefeyo.otasuke.items.EventType;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

public class AddScheduleActivity extends AppCompatActivity implements EventDetailDialogFragment.OnCommitListener {

    private ActivityAddScheduleBinding mBinding;
    private List<EventDetail> detailList;
    private CommitEventAdapter mAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_schedule);
        setSupportActionBar(mBinding.toolbar);
        detailList = new ArrayList<>();
        mAdapter = new CommitEventAdapter(getApplicationContext());
        mBinding.addListview.setAdapter(mAdapter);
        realm = Realm.getDefaultInstance();

        initButtons();
        initListView();

        TextView emptyMessage = new TextView(getApplicationContext());
        emptyMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.accent_text));
        emptyMessage.setText("右下のボタンからイベントを入力してください");
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setTextSize(20);
        mBinding.addListview.setEmptyView(emptyMessage);
    }

    private void initButtons() {
        mBinding.floatingmenu.addButton(makeActionButton(
                "先行上映回",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("先行上映回", EventType.EARLY.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "発売日",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("発売日", EventType.DATE.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "ライブ",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("ライブ", EventType.LIVE.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "リリイベ",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("リリイベ", EventType.RELEASE.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "ファンミ",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("ファンミ", EventType.FANMEETING.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
        mBinding.floatingmenu.addButton(makeActionButton(
                "その他",
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.drawable.ic_date_range_white_24dp,
                (v -> {
                    showDetailDialog("その他", EventType.OTHERS.getId());
                    mBinding.floatingmenu.toggle();
                })
        ));
    }

    private FloatingActionButton makeActionButton(String title, int colorNormal, int colorPressed, int iconRes, View.OnClickListener listener) {
        colorNormal = ContextCompat.getColor(getApplicationContext(), colorNormal);
        colorPressed = ContextCompat.getColor(getApplicationContext(), colorPressed);
        FloatingActionButton button = new FloatingActionButton(getApplicationContext());
        button.setTitle(title);
        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setColorNormal(colorNormal);
        button.setColorPressed(colorPressed);
        button.setIcon(iconRes);
        button.setStrokeVisible(true);
        button.setOnClickListener(listener);

        return button;
    }

    private void initListView() {
        SwipeMenuCreator creator = menu -> {
            SwipeMenuItem delete = new SwipeMenuItem(getApplicationContext());
            delete.setBackground(new ColorDrawable(Color.parseColor("#e74c3c")));
            delete.setWidth(200);
            delete.setTitle("削除");
            delete.setTitleSize(18);
            delete.setTitleColor(Color.WHITE);
            menu.addMenuItem(delete);
        };
        mBinding.addListview.setMenuCreator(creator);

        mBinding.addListview.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    EventDetail event = mAdapter.getItem(position);
                    detailList.remove(event);
                    mAdapter.remove(event);
                    mAdapter.notifyDataSetChanged();
                    break;
            }

//            true is keeping open menu
            return false;
        });

        mBinding.addListview.setOnItemClickListener((adapterView, view, i, l) -> {
            EventDetail eventDetail = (EventDetail) adapterView.getItemAtPosition(i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
            try {
                Date date = sdf.parse(eventDetail.getDate());
                Calendar calenar = Calendar.getInstance();
                calenar.setTime(date);
                showDetailDialog(
                        EventType.getType(eventDetail.getEventType()).getName(),
                        eventDetail.getEventType(),
                        eventDetail.getPlace(),
                        eventDetail.getDescription(),
                        calenar.get(Calendar.YEAR),
                        calenar.get(Calendar.MONTH),
                        calenar.get(Calendar.DAY_OF_MONTH)
                );
                mAdapter.remove(eventDetail);
                detailList.remove(eventDetail);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void showDetailDialog(String title, int type) {
        EventDetailDialogFragment fragment = new EventDetailDialogFragment();
        fragment.setOnCommitListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "detaildialog");
    }

    private void showDetailDialog(String title, int type, String place, String description, int year, int month, int day) {
        EventDetailDialogFragment fragment = new EventDetailDialogFragment();
        fragment.setOnCommitListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
        bundle.putString("place", place);
        bundle.putString("description", description);
        bundle.putInt("year", year);
        bundle.putInt("month", month + 1);
        bundle.putInt("day", day);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "detaildialog");
    }

    @Override
    public void onCommit(EventDetail eventDetail) {
        Log.d("種類", EventType.getType(eventDetail.getEventType()).getName());
        Log.d("場所", eventDetail.getPlace());
        Log.d("備考", eventDetail.getDescription());
        Log.d("日付", eventDetail.getDate());
        Log.d("時間", eventDetail.getTime());
//        realm.beginTransaction();
//
//        EventDetail saveItem = realm.createObject(EventDetail.class);
//        saveItem.setEventType(eventDetail.getEventType());
//        saveItem.setDate(eventDetail.getDate());
//        saveItem.setTime(eventDetail.getTime());
//        saveItem.setPlace(eventDetail.getPlace());
//        saveItem.setDescription(eventDetail.getDescription());
//
//        realm.commitTransaction();
        detailList.add(eventDetail);
        mAdapter.add(eventDetail);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_schedule_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_complete:
                if (detailList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "少なくとも1つ以上のイベントを入力してください", Toast.LENGTH_LONG).show();
                    break;
                } else {
                    realm.beginTransaction();
                    String eventName = mBinding.name.getText().toString();
                    if (eventName.equals("")) {
                        mBinding.name.setError("コンテンツ名を入力してください");
                        break;
                    } else {
                        Event event = realm.where(Event.class).equalTo("eventName", eventName).findFirst();
                        if (event == null) {
                            event = realm.createObject(Event.class);
                            event.setEventName(eventName);
                        }
                        RealmList<EventDetail> realmEventDetails = new RealmList<>();
//                        realmEventDetails.addAll(detailList);
                        for (EventDetail e : detailList) {
                            EventDetail saveItem = realm.createObject(EventDetail.class);
                            saveItem.setEventType(e.getEventType());
                            saveItem.setDate(e.getDate());
                            saveItem.setTime(e.getTime());
                            saveItem.setPlace(e.getPlace());
                            saveItem.setDescription(e.getDescription());
                            realmEventDetails.add(saveItem);
                        }
                        event.setEventDetails(realmEventDetails);
                        realm.commitTransaction();
                        finish();
                        Log.d("保存完了", "完了");
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
