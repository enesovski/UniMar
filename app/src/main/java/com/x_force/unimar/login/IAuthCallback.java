package com.x_force.unimar.login;

public interface IAuthCallback {
    void onSuccess(String message);
    void onFailure(String message);
}
