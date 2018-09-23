package org.afrikcode.pesmanager;

import android.app.Activity;
import android.preference.PreferenceManager;

public class Utils {

    public void saveBranchID(Activity activity, String branchID){
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(Constants.BRANCH_ID, branchID).apply();
    }

    public String getBranchID(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.BRANCH_ID, "");
    }

    public void saveBranchName(Activity activity, String branchName) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(Constants.BRANCH_NAME, branchName).apply();
    }

    public String getBranchName(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.BRANCH_NAME, "");
    }

    public void saveManagerID(Activity activity, String managerID) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(Constants.MANAGER_ID, managerID).apply();
    }

    public String getManagerID(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.MANAGER_ID, "");
    }

}
