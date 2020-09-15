package com.kkevn.ledsign.ui.configurators;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.kkevn.ledsign.HelpInfoDialogFragment;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.SelectEffectDialogFragment;
import com.kkevn.ledsign.ui.create.CreateFragment;

public class ConfiguratorListeners {

    private Context context;

    private FragmentManager fragmentManager;

    public ConfiguratorListeners(Context c, FragmentManager fm) {
        context = c;
        fragmentManager = fm;
    }

    public void onCancelClick() {
        MainActivity.navigateToFragment("");
    }

    public void onSubmitClick(String effect, String param) {

        if (!param.contains("-")) {
            CreateFragment.addEffect(effect, param);
            MainActivity.navigateToFragment("");
        } else {
            Toast.makeText(context, R.string.configure_missing_param, Toast.LENGTH_SHORT).show();
        }
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
