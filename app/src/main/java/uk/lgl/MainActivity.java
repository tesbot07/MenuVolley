package uk.lgl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


import uk.lgl.modmenu.Preferences;


public class MainActivity extends Activity {

    public String GameActivity = "com.epicgames.ue4.SplashActivity";

    //Load lib
    static {
        // When you change the lib name, change also on Android.mk file
        // Both must have same name
        System.loadLibrary("MyLibName");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences.context = this;
        //To launch mod menu
        StaticAct.Start(this);

        //To load lib only
        //LoadLib(this);

        //To launch game activity
        try {
            //Start service
            MainActivity.this.startActivity(new Intent(MainActivity.this, Class.forName(MainActivity.this.GameActivity)));
        } catch (ClassNotFoundException e) {
            //Uncomment this if you are following METHOD 2 of CHANGING FILES
            //Toast.makeText(MainActivity.this, "Error. Game's main activity does not exist", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }
    }

    //Load mod menu
    
    //Call toast only without mod menu
    public static void LoadLibOnly(final Context context) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                NativeToast.makeText(context.getApplicationContext(), 0);
            }
        }, 500);
    }
}
