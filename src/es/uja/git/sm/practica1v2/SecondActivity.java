package es.uja.git.sm.practica1v2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;

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
		/**
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
		*/
	}
	
	public String conectaSocket(String host,int puerto) {

		if (host!= null && puerto!=0) {
			String contentAsString = "";
			Socket s = new Socket();
			InputStream is;
			DataOutputStream dos;

			try {
				String line = null;
				int port=puerto;
				s = new Socket(host, port);

				is = s.getInputStream();
				dos = new DataOutputStream(s.getOutputStream());

				dos.writeUTF("GET / HTTP/1.1\r\n" + "HOST=www10.ujaen.es\r\n" + "Connection: close\r\n"
						+ "Accept: text/*\r\n" + "User-Agent: UJAClient (Windows NT 10.0; WOW64)\r\n"
						+ "Accept-Language: es-ES,es;q=0.8,en;q=0.6");
				dos.flush();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					line = line + "\r\n";

					if (line.contains("Content-Length")) {
						String datalength = line.substring(line.indexOf("Content-Length"));
					}
					contentAsString = contentAsString + line;
				}
				dos.close();
				is.close();
				s.close();
				return contentAsString;
			} catch (IOException e) {
				return e.getMessage();

			} catch (IllegalArgumentException e) {
				return e.getMessage();

			}
		}
		return "Conexión fallida";

	}

}
