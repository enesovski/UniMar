package com.x_force.unimar;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.x_force.unimar.login.LoginActivity;

public class ActivityManager {

    //#region Singleton
    private static ActivityManager instance;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }

        return instance;
    }
    //#endregion


}
