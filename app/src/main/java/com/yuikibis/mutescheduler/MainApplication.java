package com.yuikibis.mutescheduler;

import com.yuikibis.mutescheduler.common.FragmentRouter;
import com.yuikibis.mutescheduler.viewController.LicenceFragment;
import com.yuikibis.mutescheduler.viewController.MainFragment;

public class MainApplication extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FragmentRouter.register(FragmentRouter.Tag.MAIN, MainFragment.class);
        FragmentRouter.register(FragmentRouter.Tag.LICENCE, LicenceFragment.class);
    }
}
