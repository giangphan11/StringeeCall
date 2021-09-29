package vn.com.phanbagiang.myapplication;

/**
 * Created by giangphanba on 9/24/2021.
 */
public class AppConstants {

    public static boolean isInCall = false;

    public interface Action{
        String MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main";
        String PLAY_ACTION = "com.marothiatechs.foregroundservice.action.play";
        String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.stopforeground";
    }

    interface NOTIFICATION_ID{
        int FOREGROUND_SERVICE = 101;
    }
}
