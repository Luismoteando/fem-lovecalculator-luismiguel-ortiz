package es.upm.miw.lovereporter;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import es.upm.miw.lovereporter.fcube.commands.FCColor;
import es.upm.miw.lovereporter.fcube.commands.FCOff;
import es.upm.miw.lovereporter.fcube.commands.FCOn;
import es.upm.miw.lovereporter.fcube.config.FeedbackCubeConfig;
import es.upm.miw.lovereporter.fcube.config.FeedbackCubeManager;
import es.upm.miw.lovereporter.feeback.FeedbackColor;

public class LoveAlertActivity extends IntentService {

    private String sIp;

    public LoveAlertActivity() {
        super("LoveAlertActivity");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.sIp = FeedbackCubeConfig.getSingleInstance().getIp();
        this.lightOn();
        if (intent != null) {
            int percentage = intent.getExtras().getInt("percentage");
            if (percentage >= 0 && percentage < 25) {
                lightUpdate(new FeedbackColor(255, 255, 255));
            } else if (percentage >= 25 && percentage < 50) {
                lightUpdate(new FeedbackColor(255, 170, 170));
            } else if (percentage >= 50 && percentage < 75) {
                lightUpdate(new FeedbackColor(255, 85, 85));
            } else if (percentage >= 75 && percentage < 100) {
                lightUpdate(new FeedbackColor(255, 0, 0));
            }
        }
    }

    public void lightOn() {
        // Boot cube on
        Log.i("FC", "Starting feedback cube ..." + this.sIp);
        FCOn f = new FCOn(this.sIp);
        new FeedbackCubeManager().execute(f);
    }

    public void lightOff() {
        // Switch cube off
        Log.i("FC", "Stoping feedback cube ..." + this.sIp);
        FCOff f = new FCOff(this.sIp);
        new FeedbackCubeManager().execute(f);
    }

    public void lightUpdate(FeedbackColor color) {
        FCColor fcc = new FCColor(this.sIp, "" + color.getR(), ""
                + color.getG(), "" + color.getB());
        new FeedbackCubeManager().execute(fcc);
    }
}
