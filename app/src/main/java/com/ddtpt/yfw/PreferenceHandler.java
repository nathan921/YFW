package com.ddtpt.yfw;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by e228596 on 7/13/2016.
 */
public class PreferenceHandler {
    Context context;

    public PreferenceHandler(Context c) {
        context = c;
    }

    public boolean doesValidTokenExist() {
        boolean exists = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if ((prefs.getString(Strings.TOKEN, "").equals("")) && (prefs.getString(Strings.SECRET, "").equals(""))) {
            Calendar c = Calendar.getInstance();
            int minutes = c.get(Calendar.MINUTE);
        }
        return true;
    }
}
