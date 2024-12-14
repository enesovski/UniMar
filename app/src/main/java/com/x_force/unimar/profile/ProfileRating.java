package com.x_force.unimar.profile;

public class ProfileRating {

    int maxPoints;
    int totalPoints;

    public int getRatingPercentage()
    {
        double percentage = (double)maxPoints / totalPoints;
        return (int)(percentage * 100);
    }
}
