package es.uja.git.sm.practica1v2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.uja.git.sv.examples.Network;
import es.uja.git.sv.examples.R;
import es.uja.git.sv.examples.Network.SocketConnection;

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
		//panel.setText(conectaSocket(ip, puerto));
		//SocketConnection task = new SocketConnection();
		//task.execute(url);
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
		return "Conexi�n fallida";

	}
	
	private class SocketConnection extends AsyncTask<URL, String, String> {
		ProgressDialog pbar = null;

		@Override
		protected String doInBackground(URL... urls) {

			// params comes from the execute() call: params[0] is the url.

			return conectaSocket(urls[0]);

		}

		@Override
		protected void onPreExecute() {

			pbar = new ProgressDialog(Network.this);
			pbar.setIndeterminate(true);
			pbar.setMessage(getBaseContext().getString(R.string.socket_downloading));

			pbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pbar.setCancelable(false);
			if (!pbar.isShowing()) {
				pbar.show();
			}
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		// onPostExecute displays the results of the AsyncTask.
		protected void onPostExecute(final String result) {
			web.setText(result);
			if (pbar != null)
				pbar.dismiss();

		}

	}
	
	public String getFecha(){
		String dia,mes,annio;
		Calendar c = new GregorianCalendar();
		 dia = Integer.toString(c.get(Calendar.DATE));
		   if(dia.length()==1)
			   dia="0"+dia;
		   mes = Integer.toString(c.get(Calendar.MONTH)+1);
		   if(mes.length()==1)
			   mes="0"+mes;
		   annio = Integer.toString(c.get(Calendar.YEAR));
		   return annio+"-"+mes+"-"+dia;
		
	}
	
	public void onSave(View v) {
		int value;
		
		EditText edit_u = (EditText) findViewById(R.id.ficheros_usuario);
		EditText edit_o = (EditText) findViewById(R.id.ficheros_operacion);
		EditText edit_v = (EditText) findViewById(R.id.ficheros_value);

		String usuario = edit_u.getEditableText().toString();
		String operacion = edit_o.getEditableText().toString();
		String valor = edit_v.getEditableText().toString();
		
		String filename = usuario;

		if (valor != null)
			try {
				value = Integer.valueOf(valor);
			} catch (NumberFormatException es) {
				value = 0;
			}
		else
			value = 0;

         
		try {
			FileOutputStream os = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
			/* Escritura sin serializaci�n*/
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(operacion);
			dos.writeInt(value);
			dos.flush();
			dos.close();

			/*Escritura con serializaci�n*/
//			Record data = new Record(tag,value);
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(data);
//			oos.flush();
//			oos.close();
			/*Fin escritura con serializaci�n*/
			os.close();
			Toast.makeText(this,
					getResources().getString(R.string.toast_saved),
					Toast.LENGTH_SHORT).show();


		} catch (IOException ex) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_error),
					Toast.LENGTH_SHORT).show();
		    
		}
		
     
	}
	
	public void onLoad(View v) {

		String texto = "";

		try {

			EditText edit_u = (EditText) findViewById(R.id.ficheros_usuario);
			
			TextView resultado = (TextView) findViewById(R.id.textView1);
			
			String filename = edit_u.getEditableText().toString();

			FileInputStream is = openFileInput(filename);
			
			/* Lectura sin serializaci�n */
			DataInputStream dos = new DataInputStream(is);
			
			int n = dos.available();
			texto = "Leidos (" + n + " bytes)\r\n";
			while (dos.available() > 0) {
				texto = texto + " clave: " + dos.readUTF() + " valor:"
						+ dos.readInt() + "\r\n";
			}
			dos.close();	

					
			is.close();
			resultado.setText(texto);

			

			Toast.makeText(this,
					getResources().getString(R.string.toast_loaded),
					Toast.LENGTH_SHORT).show();

		} catch (IOException ex) {
			Toast.makeText(this,
					getResources().getString(R.string.toast_error),
					Toast.LENGTH_SHORT).show();

		}

	}
	

}
