package com.yuikibis.mutescheduler.viewController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yuikibis.mutescheduler.R;
import de.greenrobot.event.EventBus;
import org.androidannotations.annotations.EFragment;

@EFragment
public class CustomAlertDialogFragment extends DialogFragment {
    public static DialogFragment newInstance(String title, String message, String positiveText, String negativeText) {
        return newInstance(title, message, positiveText, negativeText, 0, 0);
    }

    // カスタムViewありコンストラクタ
    public static DialogFragment newInstance(String title, String message, String positiveText, String negativeText, int layout, int view) {
        DialogFragment instance = new CustomAlertDialogFragment();

        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putString("message", message);
        arguments.putString("positiveText", positiveText);
        arguments.putString("negativeText", negativeText);
        if (layout == 0 && view == 0) {
            arguments.putBoolean("isUseCustomView", false);
        } else {
            arguments.putBoolean("isUseCustomView", true);
            arguments.putInt("layout", layout);
            arguments.putInt("view", view);
        }

        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String positiveText = getArguments().getString("positiveText");
        String negativeText = getArguments().getString("negativeText");

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new DialogClickEvent(getTag()));
                        dismiss();
                    }
                }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        if (getArguments().getBoolean("isUseCustomView")) {
            int layout = getArguments().getInt("layout");
            int view = getArguments().getInt("view");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View customView = inflater.inflate(layout, (ViewGroup) getActivity().findViewById(view));
            alert.setView(customView).setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EventBus.getDefault().post(new DialogClickEvent(customView, getTag()));
                    dismiss();
                }
            });
        }
        setCancelable(false);

        return alert.create();
    }

    public class DialogClickEvent {
        private final View view;
        private final String tag;

        private DialogClickEvent(String tag) {
            this(null, tag);
        }

        private DialogClickEvent(View view, String tag) {
            this.view = view;
            this.tag = tag;
        }

        public View getView() {
            return view;
        }

        public String getTag() {
            return tag;
        }
    }
}
