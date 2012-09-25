package com.chalmers.feedlr.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

public class TwitterService extends Service {

	static final int MSG_TEST = 1;

	private final Messenger messenger = new Messenger(new MessageHandler());
	
    class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TEST:
                    Toast.makeText(getApplicationContext(), "Messenging working!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
