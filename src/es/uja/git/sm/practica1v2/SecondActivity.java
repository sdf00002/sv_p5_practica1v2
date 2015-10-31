package es.uja.git.sm.practica1v2;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends Activity {
	

	protected void onCreate(Bundle savedInstanceState) {
		
		//Cogemos los datos enviamos por el Fragment y los asignamos a variables tipo String
		Bundle bundle= getIntent().getExtras();
		String usuario = null,password=null,ip=null,puerto=null;
		if(bundle!=null){
		usuario=getIntent().getStringExtra("user");
		password=getIntent().getStringExtra("pass");
		ip=getIntent().getStringExtra("ip");
		puerto=getIntent().getStringExtra("port");
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		
		//Asociamos las variables tipo TextView a los campos de la interfaz
		TextView user = (TextView)findViewById(R.id.textView1);
		TextView clave = (TextView)findViewById(R.id.textView2);
		TextView dirip = (TextView)findViewById(R.id.textView3);
		TextView port = (TextView)findViewById(R.id.textView4);
		
		//Escribimos en los TextView los datos recibidos
		user.setText(usuario);
		clave.setText(password);
		dirip.setText(ip);
		port.setText(puerto);

	}

}
