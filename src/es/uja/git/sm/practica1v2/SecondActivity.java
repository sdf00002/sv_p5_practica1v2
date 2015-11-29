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
	
	//PDU formada por: Version+secuencia+tipo+comando+payload
	
	//Comandos utilizados en el protocolo
	public static final String CRLF="\r\n";
	public static final String OK="+OK";
	public static final String ERR="-ERR";
	public static final String QUIT="QUIT";
	//Tipos de mensajes
	public static final byte MSG_LOGIN=0x01; 
	public static final byte MSG_OPERACION=0x02;
	public static final byte MSG_FIN=0X04;
	
	protected int secuencia=0;
	protected static final byte version=1,tipo=0;
	private boolean salida=false;
	private String mensaje = "",cmd="";
	private byte v,type;

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
		TextView panel = (TextView)findViewById(R.id.textView1);
		panel.setText(conectaSocket(ip, puerto));
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
	
	public String conectaSocket(String host,String port) {
		int puerto=Integer.parseInt(port);
		if (host!= null && puerto!=0) {
			String contentAsString = "";
			Socket s = new Socket();
			InputStream is;
			DataOutputStream dos;

			try {
				String line = null;
				s = new Socket(host, puerto);

				is = s.getInputStream();
				dos = new DataOutputStream(s.getOutputStream());

				
				dos.writeUTF(OK);
				dos.flush();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					line = line + "\r\n";

					
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
