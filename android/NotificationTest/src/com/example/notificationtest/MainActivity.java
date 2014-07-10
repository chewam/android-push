package com.example.notificationtest;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	      //Register receivers for push notifications
	      registerReceivers();
	      TelephonyManager tManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	      String uid = tManager.getDeviceId();
	      Toast.makeText(this, ""+uid, Toast.LENGTH_LONG).show();
	      //Create and start push manager
	      PushManager pushManager = PushManager.getInstance(this);
	       
	      //Start push manager, this will count app open for Pushwoosh stats as well
	      try {
	             pushManager.onStartup(this);
	      }
	      catch(Exception e)
	      {
	             //push notifications are not available or AndroidManifest.xml is not configured properly
	      }
	       
	      //Register for push!
	      pushManager.registerForPushNotifications();
	 
	      checkMessage(getIntent());
	 
	      // other code
	}
	@Override
	public void onResume()
	{
	    super.onResume();
	     
	    //Re-register receivers on resume
	    registerReceivers();
	}
	 
	@Override
	public void onPause()
	{
	    super.onPause();
	 
	    //Unregister receivers on pause
	    unregisterReceivers();
	}
	@Override
	protected void onNewIntent(Intent intent)
	{
	    super.onNewIntent(intent);
	    setIntent(intent);
	 
	    checkMessage(intent);
	 
	    setIntent(new Intent());
	}
	//Registration receiver
	BroadcastReceiver mBroadcastReceiver = new RegisterBroadcastReceiver()
	{
	    @Override
	    public void onRegisterActionReceive(Context context, Intent intent)
	    {
	        checkMessage(intent);
	    }
	};
	 
	//Push message receiver
	private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
	{
	    @Override
	    protected void onMessageReceive(Intent intent)
	    {
	        //JSON_DATA_KEY contains JSON payload of push notification.
	        showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
	    }
	};
	 
	//Registration of the receivers
	public void registerReceivers()
	{
	    IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
	 
	    registerReceiver(mReceiver, intentFilter);
	     
	    registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));       
	}
	 
	public void unregisterReceivers()
	{
	    //Unregister receivers on pause
	    try
	    {
	        unregisterReceiver(mReceiver);
	    }
	    catch (Exception e)
	    {
	        // pass.
	    }
	     
	    try
	    {
	        unregisterReceiver(mBroadcastReceiver);
	    }
	    catch (Exception e)
	    {
	        //pass through
	    }
	}	
	private void checkMessage(Intent intent)
	{
	    if (null != intent)
	    {
	        if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
	        {
	            showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
	        }
	        else if (intent.hasExtra(PushManager.REGISTER_EVENT))
	        {
	            showMessage("register");
	        }
	        else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
	        {
	            showMessage("unregister");
	        }
	        else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
	        {
	            showMessage("register error");
	        }
	        else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
	        {
	            showMessage("unregister error");
	        }
	 
	        resetIntentValues();
	    }
	}
	 
	/**
	 * Will check main Activity intent and if it contains any PushWoosh data, will clear it
	 */
	private void resetIntentValues()
	{
	    Intent mainAppIntent = getIntent();
	 
	    if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
	    }
	 
	    setIntent(mainAppIntent);
	}

	private void showMessage(String message)
	{
	    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
