package com.x_force.unimar.profile;

import java.util.Map;

public interface IProfileCallback {
    void onSuccess(String message);
    void onFailure(String message);
    void onProfileLoaded(Map<String, Object> profileData);
}
