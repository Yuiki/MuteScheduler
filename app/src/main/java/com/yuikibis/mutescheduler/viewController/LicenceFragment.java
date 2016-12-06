package com.yuikibis.mutescheduler.viewController;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yuikibis.mutescheduler.R;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_licence)
public class LicenceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_licence, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.license));
        toolbar.getMenu().clear();
        return view;
    }
}
