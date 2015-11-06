package es.uja.git.sm.practica1v2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		
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
			
			View lanzar = rootView.findViewById(R.id.boton1);
		    final View usuario=rootView.findViewById(R.id.usuario);
		    final View password=rootView.findViewById(R.id.password);
		    final View ip=rootView.findViewById(R.id.dir_ip);
		    final View port=rootView.findViewById(R.id.puerto);

			((ImageButton) lanzar).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					if(((EditText)usuario).getText().toString().equals("")||((EditText)password).getText().toString().equals("")||((EditText)ip).getText().toString().equals("")||((EditText)port).getText().toString().equals(""))
					{
						
					Toast toast1 =
				            Toast.makeText(getActivity(),
				                    "Datos incompletos", Toast.LENGTH_SHORT);
					toast1.setGravity(Gravity.CENTER,0,0);
				        toast1.show();
				        
						
					} else { 
					Intent intent= new Intent();
					intent.setClass(getActivity(), SecondActivity.class);
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
}
