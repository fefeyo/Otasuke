package com.fefeyo.otasuke.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.databinding.ActivityAddScheduleBinding;
import com.fefeyo.otasuke.fragments.EventDetailDialogFragment;
import com.fefeyo.otasuke.items.EventDetail;
import com.fefeyo.otasuke.items.EventType;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements EventDetailDialogFragment.OnCommitListener{

    private ActivityAddScheduleBinding mBinding;
    private List<EventDetail> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_schedule);
        setSupportActionBar(mBinding.toolbar);
        initButtons();
        detailList = new ArrayList<>();
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

    private void showDetailDialog(String title, int type) {
        EventDetailDialogFragment fragment = new EventDetailDialogFragment();
        fragment.setOnCommitListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "detaildialog");
    }

    @Override
    public void onCommit(EventDetail eventDetail) {
        Log.d("場所", eventDetail.getPlace());
        Log.d("備考", eventDetail.getDescription());
        Log.d("日付", eventDetail.getDate());
        Log.d("時間", eventDetail.getTime());
        detailList.add(eventDetail);
    }
}
