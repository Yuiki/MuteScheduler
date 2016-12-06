package com.yuikibis.mutescheduler.viewController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.yuikibis.mutescheduler.R;
import com.yuikibis.mutescheduler.model.Schedule;

import java.util.List;

public class SchedulerAdapter extends ArrayAdapter<Schedule> {
    private final LayoutInflater layoutInflater;
    private Context context;

    public SchedulerAdapter(Context context, List<Schedule> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        layoutInflater = LayoutInflater.from(context);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_schedule, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Schedule schedule = getItem(position);

        String dayWeek = (schedule.isSun ? context.getString(R.string.sun) + ", " : "") + (schedule.isMon ? context.getString(R.string.mon) + ", " : "") + (schedule.isTue ? context.getString(R.string.tue) + ", " : "") + (schedule.isWed ? context.getString(R.string.wed) + ", " : "") + (schedule.isThu ? context.getString(R.string.thu) + ", " : "") + (schedule.isFri ? context.getString(R.string.fri) + ", " : "") + (schedule.isSat ? context.getString(R.string.sat) + ", " : "");
        dayWeek = dayWeek.substring(0, dayWeek.length() - 2);

        String target = (schedule.isMedia ? context.getString(R.string.media) + ", " : "") + (schedule.isRing ? context.getString(R.string.ring) + ", " : "") + (schedule.isAlarm ? context.getString(R.string.alarm) + ", " : "");
        target = target.substring(0, target.length() - 2);

        String str = String.format("%1$02d", schedule.startHour) + ":" + String.format("%1$02d", schedule.startMinute) + "-" + String.format("%1$02d", schedule.endHour) + ":" + String.format("%1$02d", schedule.endMinute) + "\n" + dayWeek + "\n" + target;

        viewHolder.scheduleText.setText(str);

        if (!schedule.isEnabled) {
            viewHolder.onOff.setText(R.string.disabled);
        } else {
            viewHolder.onOff.setText(R.string.enabled);
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.scheduleText)
        TextView scheduleText;
        @Bind(R.id.onOff)
        TextView onOff;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
