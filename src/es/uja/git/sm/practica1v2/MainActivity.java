package es.uja.git.sm.practica1v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private BroadcastReceiver r=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		final WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		android.net.wifi.SupplicantState s = wifiManager.getConnectionInfo().getSupplicantState();
		//NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);
		
		//Compruebo si el wifi no esta habilitado
		if(wifiManager.isWifiEnabled()==false){
			
			
				wifiManager.setWifiEnabled(true);
				Toast toast1 =
	    	            Toast.makeText(this,
	    	                    "Wifi Habilitado", Toast.LENGTH_SHORT);
	    		toast1.setGravity(Gravity.CENTER,0,0);
	    	        toast1.show();
				r=new BroadcastReceiver(){
	
						
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							 WifiInfo wi = arg1.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
							 NetworkInfo ni= arg1.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
							   
							//caso en que tengamos datos del ssid y el estado del wifi sea CONNECTED
				        	if(wi!=null && ni!=null){
					        		if(ni.getDetailedState()==NetworkInfo.DetailedState.CONNECTED){
					        		
					        		
					        	Toast toast2 =
					    	            Toast.makeText(arg0,
					    	                    "Conectado a "+wi.getSSID(), Toast.LENGTH_SHORT);
					    		toast2.setGravity(Gravity.CENTER,0,0);
					    	        toast2.show();
				        		
				        	}}
							
						}
					
				};
				
				//Registro del receiver que detecta cambios de estado en wifi
				registerReceiver(r, new IntentFilter(
						WifiManager.NETWORK_STATE_CHANGED_ACTION));
		
		}
		
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container_main, new PlaceholderFragment()).commit();
			
		}
			
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.menu_help:
			// Creamos una nueva Activity
			Intent newactivity_help = new Intent(this, Help.class);
			startActivity(newactivity_help);
			return true;
		case R.id.action_settings:
			// Creamos una nueva Activity
			Intent newactivity_settings = new Intent(this, Settings.class);
			startActivity(newactivity_settings);
			return true;	
		}

		return false;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
		
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			//Almacenamos en variables los elementos de la actividad principal
			View lanzar = rootView.findViewById(R.id.boton1);
		    final View usuario=rootView.findViewById(R.id.usuario);
		    final View password=rootView.findViewById(R.id.password);
		    final View ip=rootView.findViewById(R.id.dir_ip);
		    final View port=rootView.findViewById(R.id.puerto);

			((ImageButton) lanzar).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Comprobamos que ninguno de los campos este vacío y hay alguno mandamos un toast indicando que los datos están incompletos
					if(((EditText)usuario).getText().toString().equals("")||((EditText)password).getText().toString().equals("")||((EditText)ip).getText().toString().equals("")||((EditText)port).getText().toString().equals(""))
					{
						
					Toast toast1 =
				            Toast.makeText(getActivity(),
				                    "Datos incompletos", Toast.LENGTH_SHORT);
					toast1.setGravity(Gravity.CENTER,0,0);
				        toast1.show();
				        
						
					} else if (((EditText)usuario).getText().toString().length()<6||((EditText)usuario).getText().toString().length()>12){ 
						Toast toast1 =
					            Toast.makeText(getActivity(),
					            		"El usuario debe tener entre 6 y 12 caracteres", Toast.LENGTH_SHORT);
						toast1.setGravity(Gravity.CENTER,0,0);
					        toast1.show();

							}else{
					Intent intent= new Intent();
					intent.setClass(getActivity(), SecondActivity.class);
					//Pasamos los valores de los elementos a la siguiente actividad
					intent.putExtra("user", ((EditText)usuario).getText().toString());
					intent.putExtra("pass", ((EditText)password).getText().toString());
					intent.putExtra("ip", ((EditText)ip).getText().toString());
					intent.putExtra("port", ((EditText)port).getText().toString());
					startActivity(intent);
					}
				}
			});
		
			return rootView;
		}		 

	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   
	  if (keyCode == KeyEvent.KEYCODE_BACK) {
	   
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    // Get the layout inflater
		  LayoutInflater inflater = this.getLayoutInflater();
		  
		// Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.dialog_exit, null))
		    // Add action buttons
		    	  .setNegativeButton(android.R.string.cancel, null)
		           .setPositiveButton(R.string.salir, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   //Salir, finalizamos Mainactivity y el Receiver
		     	          MainActivity.this.finish();
		     	        // unregisterReceiver(r);
		               }
		           }).show();
		                 		  
	  }
	//para las demas cosas, se reenvia el evento al listener habitual
	  return super.onKeyDown(keyCode, event);
	} 
	

}
