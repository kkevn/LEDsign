package com.kkevn.ledsign.ui.configurators;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.kkevn.ledsign.HelpInfoDialogFragment;
import com.kkevn.ledsign.SelectEffectDialogFragment;

public class ConfiguratorListeners {

    private Context context;

    private FragmentManager fragmentManager;

    public ConfiguratorListeners(Context c, FragmentManager fm) {
        context = c;
        fragmentManager = fm;
    }

    public void onHelpClick(String msg) {
        //new SelectEffectDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
        //new HelpInfoDialogFragment(msg).setShowsDialog(true);

        HelpInfoDialogFragment hidf = new HelpInfoDialogFragment();

        Bundle args = new Bundle();
        args.putString("msg", msg);
        hidf.setArguments(args);

        hidf.show(fragmentManager, this.getClass().getSimpleName());
    }
}
