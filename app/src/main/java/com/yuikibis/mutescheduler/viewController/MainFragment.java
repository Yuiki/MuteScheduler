package com.yuikibis.mutescheduler.viewController;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.activeandroid.query.Select;
import com.yuikibis.mutescheduler.R;
import com.yuikibis.mutescheduler.common.Const;
import com.yuikibis.mutescheduler.common.FragmentRouter;
import com.yuikibis.mutescheduler.model.Schedule;
import de.greenrobot.event.EventBus;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ArrayAdapter<Schedule> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // どうにかFragmentRouterをPreferenceFragmentに対応させたい
                SettingFragment settingFragment = new SettingFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.fragmentContainer, settingFragment, String.valueOf(FragmentRouter.Tag.SETTING));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
        toolbar.setTitle(getString(R.string.app_name));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment alertDialogFragment = CustomAlertDialogFragment.newInstance(getString(R.string.addSchedule), null, getString(R.string.add), getString(R.string.cancel), R.layout.dialog_add, R.id.layoutAdd);
                alertDialogFragment.show(getFragmentManager(), Const.AlertDialogTag.ADD.toString());
            }
        });

        List<Schedule> schedules = new Select().from(Schedule.class).execute();

        adapter = new SchedulerAdapter(getActivity(), schedules);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        return view;
    }

    @OptionsItem
    void actionSettings() {
        FragmentRouter.replace(getFragmentManager(), R.id.fragmentContainer, FragmentRouter.Tag.SETTING, null, true);
    }

    private boolean addItem(Schedule schedule) {
        // 重複確認
        for (int i = 0; i < adapter.getCount(); i++) {
            if (schedule.equals(adapter.getItem(i))) {
                return false;
            }
        }
        adapter.add(schedule);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, Const.ContextMenuItem.ON_OFF.toInt(), 0, R.string.onOff);
        menu.add(0, Const.ContextMenuItem.DELETE.toInt(), 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Schedule schedule = adapter.getItem(info.position);
        switch (Const.ContextMenuItem.valueOf(item.getItemId())) {
            case ON_OFF:
                schedule.isEnabled = !schedule.isEnabled;
                schedule.save();

                List<Schedule> schedules = new ArrayList<>();
                for(int i = 0; i < adapter.getCount(); i++){
                    schedules.add(adapter.getItem(i));
                }
                adapter.clear();
                adapter.addAll(schedules);

                if (schedule.isEnabled) {
                    Calendar calendar = Calendar.getInstance();
                    int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int nowMinute = calendar.get(Calendar.MINUTE);
                    if (nowHour > schedule.startHour || nowHour == schedule.startHour && nowMinute > schedule.startMinute) {
                        calendar.add(Calendar.DATE, 1);
                    }
                    schedule.year = calendar.get(Calendar.YEAR);
                    schedule.month = calendar.get(Calendar.MONTH);
                    schedule.date = calendar.get(Calendar.DATE);
                    schedule.save();

                    MuteService.registerAlarm(getActivity(), schedule);
                    Toast.makeText(getActivity(), R.string.enabledSchedule, Toast.LENGTH_SHORT).show();
                } else {
                    MuteService.unregisterAlarm(getActivity(), schedule);
                    Toast.makeText(getActivity(), R.string.disabledSchedule, Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE:
                deleteSchedule(schedule);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void deleteSchedule(Schedule schedule) {
        MuteService.unregisterAlarm(getActivity(), schedule);
        schedule.delete();
        adapter.remove(schedule);

        Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(CustomAlertDialogFragment.DialogClickEvent event) {
        View view = event.getView();
        if (view == null) {
            return;
        }

        registerSchedule(view);
    }

    @SuppressWarnings("deprecation")
    private void registerSchedule(View view) {
        Schedule schedule = new Schedule();

        TimePicker start = (TimePicker) view.findViewById(R.id.timePickerStart);
        TimePicker end = (TimePicker) view.findViewById(R.id.timePickerEnd);
        if (Build.VERSION.SDK_INT >= 23) {
            schedule.startHour = start.getHour();
            schedule.startMinute = start.getMinute();
            schedule.endHour = end.getHour();
            schedule.endMinute = end.getMinute();
        } else {
            schedule.startHour = start.getCurrentHour();
            schedule.startMinute = start.getCurrentMinute();
            schedule.endHour = end.getCurrentHour();
            schedule.endMinute = end.getCurrentMinute();
        }

        schedule.isSun = ((CheckBox) view.findViewById(R.id.checkBoxSun)).isChecked();
        schedule.isMon = ((CheckBox) view.findViewById(R.id.checkBoxMon)).isChecked();
        schedule.isTue = ((CheckBox) view.findViewById(R.id.checkBoxTue)).isChecked();
        schedule.isWed = ((CheckBox) view.findViewById(R.id.checkBoxWed)).isChecked();
        schedule.isThu = ((CheckBox) view.findViewById(R.id.checkBoxThu)).isChecked();
        schedule.isFri = ((CheckBox) view.findViewById(R.id.checkBoxFri)).isChecked();
        schedule.isSat = ((CheckBox) view.findViewById(R.id.checkBoxSat)).isChecked();

        schedule.isMedia = ((CheckBox) view.findViewById(R.id.checkBoxMedia)).isChecked();
        schedule.isRing = ((CheckBox) view.findViewById(R.id.checkBoxRing)).isChecked();
        schedule.isAlarm = ((CheckBox) view.findViewById(R.id.checkBoxAlarm)).isChecked();

        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        if (nowHour > schedule.startHour || nowHour == schedule.startHour && nowMinute > schedule.startMinute) {
            calendar.add(Calendar.DATE, 1);
        }

        schedule.year = calendar.get(Calendar.YEAR);
        schedule.month = calendar.get(Calendar.MONTH);
        schedule.date = calendar.get(Calendar.DATE);

        // 一意のidを作成
        schedule.idx = ((int) (Math.random() * 1000000000)) + 1;

        if (!(schedule.isSun || schedule.isMon || schedule.isTue || schedule.isWed || schedule.isThu || schedule.isFri || schedule.isSat) ||
                !(schedule.isMedia || schedule.isRing || schedule.isAlarm) ||
                schedule.startHour == schedule.endHour && schedule.startMinute == schedule.endMinute ||
                !addItem(schedule)) {
            Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            return;
        }

        schedule.isEnabled = true;
        schedule.save();
        MuteService.registerAlarm(getActivity(), schedule);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
