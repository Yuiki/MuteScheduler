package com.yuikibis.mutescheduler.viewController;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yuikibis.mutescheduler.R;
import com.yuikibis.mutescheduler.common.FragmentRouter;
import org.androidannotations.annotations.EFragment;

@EFragment
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting);
        toolbar.getMenu().clear();

        Preference licence = findPreference(getString(R.string.license));
        licence.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FragmentRouter.replace(getFragmentManager(), R.id.fragmentContainer, FragmentRouter.Tag.LICENCE, null, true);
                return true;
            }
        });

        Preference review = findPreference(getString(R.string.review));
        review.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.yuikibis.mutescheduler"));
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
