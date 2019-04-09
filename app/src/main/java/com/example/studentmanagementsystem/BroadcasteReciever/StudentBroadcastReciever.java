package com.example.studentmanagementsystem.BroadcasteReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.studentmanagementsystem.constants.Constants;

/**
 * this class is getting message and sending call back to the respected classes.
 */
public class StudentBroadcastReciever extends BroadcastReceiver {

    private SendBroadCastMessage sendBroadCastMessageUpdate;
    private SendBroadCastMessage sendBroadCastMessageDelete;

    public void setSendBroadCastMessageUpdate(SendBroadCastMessage sendBroadCastMessageUpdate) {
        this.sendBroadCastMessageUpdate = sendBroadCastMessageUpdate;
    }

    public void setSendBroadCastMessageDelete(SendBroadCastMessage sendBroadCastMessageDelete) {
        this.sendBroadCastMessageDelete = sendBroadCastMessageDelete;
    }

    /**
     * getting message if task is done.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(Constants.VIBRATE_MILI_SECOND);
        Toast.makeText(context, intent.getStringExtra(Constants.ACTION_TYPE), Toast.LENGTH_SHORT).show();
        if(intent.getStringExtra(Constants.ACTION_TYPE).equals(Constants.ACTION_TYPE_DELETE)){
            sendBroadCastMessageDelete.sendCallMessage(Constants.ACTION_TYPE_DELETE);
        }else {
            sendBroadCastMessageUpdate.sendCallMessage(intent.getStringExtra(Constants.ACTION_TYPE));
        }
    }

    /**
     * sending broadcastmessage to respected classes.
     */
    public interface SendBroadCastMessage{
        void sendCallMessage(String str);
    }
}
