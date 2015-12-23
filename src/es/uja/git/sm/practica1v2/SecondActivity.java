package es.uja.git.sm.practica1v2;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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
	
	private String aux=null;
	protected int secuencia=0;
	protected static final byte version=1,tipo=0;
	private boolean autenticado=false;
	private String usuario="",password="";
	private String modifiedSentence = "";
	private TextView panel;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		//Cogemos los datos enviamos por el Fragment y los asignamos a variables tipo String
		Bundle bundle= getIntent().getExtras();
		String ip=null,puerto=null;
		if(bundle!=null){
		usuario=getIntent().getStringExtra("user");
		password=getIntent().getStringExtra("pass");
		ip=getIntent().getStringExtra("ip");
		puerto=getIntent().getStringExtra("port");
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		
		panel = (TextView)findViewById(R.id.textView1);
		aux=ip+":"+puerto;
	
	}
	
	
    @Override
    protected void onSaveInstanceState(Bundle guardaEstado) {
          super.onSaveInstanceState(guardaEstado);
          //guardamos en la variable t el texto del campo TextView
          String t = panel.getText().toString();
          //lo "guardamos" en el Bundle   
          guardaEstado.putString("text", t);   
    }
 
    @Override
    protected void onRestoreInstanceState(Bundle recuperaEstado) {
          super.onRestoreInstanceState(recuperaEstado);
          //recuperamos el String del Bundle
          String t = recuperaEstado.getString("text");
          //Seteamos el valor del EditText con el valor de nuestra cadena   
          panel.setText(t);                    
    }
	
	
	/**
	 * @param ip direccion ip de servidor
	 * @param puerto puerto donde escucha el servidor
	 * @param ope operacion a realizar (seno o coseno)
	 * @return un String con el resultado de la operacion
	 */
	public String conectaSocket(String ip, String puerto, String ope) {
		
		int estado=0;
		int port = Integer.parseInt(puerto);
		if (ip != null) {

			try {
				
				
				Socket s = new Socket(ip, port);

				DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());

				DataInputStream dis = new DataInputStream(s.getInputStream());

				EditText edit_v = (EditText) findViewById(R.id.valor);
				String valor = edit_v.getEditableText().toString();
				
				while (estado==0) {
					
					

					outToServer.write(version);
					outToServer.writeInt(secuencia);
					outToServer.write(MSG_OPERACION);
					outToServer.writeUTF(ope);
					outToServer.writeUTF(usuario + "_" + password+"_"+valor);
					outToServer.flush();
					
					modifiedSentence=dis.readUTF();																			
					
					estado++;
				}
				dis.close();
				outToServer.close();
				s.close();
				return modifiedSentence;
			} catch (UnknownHostException e) {
				return "Host desconocido";

			}  catch (IOException e) {
					return "No se ha podido conectar con el servidor";
				} 
		}
		return "Conexión fallida";

	}
	

	private class SocketConnection extends AsyncTask<String,String, String> {
		ProgressDialog pbar = null;

		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the ip.
			//params[1] is the port and params[2] is the operation string
			String aux=urls[0];
			String [] params=aux.split(":");

			return conectaSocket(params[0],params[1],params[2]);

		}

		@Override
		protected void onPreExecute() {

			pbar = new ProgressDialog(SecondActivity.this);
			pbar.setIndeterminate(true);
			pbar.setMessage(getBaseContext().getString(R.string.conectando));

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
		protected void onPostExecute(String result) {
			//TextView panel = (TextView)findViewById(R.id.textView1);
			//Comprobamos que se recibe +OK
			if(result.substring(3,6).equals(OK)){
				if(panel.getText().equals("")){
					if(result.length()>15){		//Si el resultado contiene muchas cifras, cogemos solo 6
				panel.setText(result.substring(9, 15));
				onSave(result.substring(6,9),result.substring(9, 15)); //Guardamos el resultado
					}
					else{
						panel.setText(result.substring(9, result.length()));
						onSave(result.substring(6,9),result.substring(9, result.length())); //Guardamos el resultado
					}
				} // Si el textview no esta vacio, lo limpiamos primero
				else{
					panel.setText("");
					
					if(result.length()>15){
						panel.setText(result.substring(9, 15)); //Si el resultado contiene muchas cifras, cogemos solo 6
						onSave(result.substring(6,9),result.substring(9, 15)); //Guardamos el resultado
							}
							else{
								panel.setText(result.substring(9, result.length()));
								onSave(result.substring(6,9),result.substring(9, result.length())); //Guardamos el resultado
							}
				}
			} //Si no hemos recibido +OK
			else {
				if(result.substring(7,14).equals("usuario")){ //Comprobamos si es el usuario el que no existe
					Toast.makeText(SecondActivity.this,
							getResources().getString(R.string.nouser),
							Toast.LENGTH_SHORT).show();
				finish(); //Finalizamos la activity
				}
					else if(result.substring(7,15).equals("password")){ //Comprobamos si la contraseña no es correcta para ese usuario
						Toast.makeText(SecondActivity.this,
								getResources().getString(R.string.nopass),
								Toast.LENGTH_SHORT).show();
						finish(); //Finalizamos la activity
					}
					else 
						Toast.makeText(SecondActivity.this,	//En otro caso, mostramos el error recibido
								result,
								Toast.LENGTH_SHORT).show();
			}
				
			
			if (pbar != null)
				pbar.dismiss();

		}

	}
	
	
	/**
	 * @param ip direccion ip del servidor
	 * @param puerto puerto donde escucha el servidor
	 * @return Un String con el resultado de la operacion de comprobar si existe el usuario
	 */
	public String compruebaUsuarioSocket(String ip, String puerto) {
		
		int estado=0;
		int port = Integer.parseInt(puerto);
		if (ip != null) {

			try {
				
				
				Socket s = new Socket(ip, port);

				DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());

				DataInputStream dis = new DataInputStream(s.getInputStream());

				
				while (estado==0) {
					
					

					outToServer.write(version);
					outToServer.writeInt(secuencia);
					outToServer.write(MSG_LOGIN);
					outToServer.writeUTF(OK);
					outToServer.writeUTF(usuario + "_" + password+"_"+"0");
					outToServer.flush();
					
					modifiedSentence=dis.readUTF();																			
					
					estado++;
				}
				dis.close();
				outToServer.close();
				s.close();
				return modifiedSentence;
			} catch (UnknownHostException e) {
				return "Host desconocido";

			}  catch (IOException e) {
					return "No se ha podido conectar con el servidor";
				}
		}
		return "Conexión fallida";

	}
	
	
	private class SocketComprueba extends AsyncTask<String,String, String> {
		ProgressDialog pbar = null;

		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the ip.
			//params[1] is the port
			String aux=urls[0];
			String [] params=aux.split(":");

			return compruebaUsuarioSocket(params[0],params[1]);

		}

		@Override
		protected void onPreExecute() {

			pbar = new ProgressDialog(SecondActivity.this);
			pbar.setIndeterminate(true);
			pbar.setMessage(getBaseContext().getString(R.string.conectando));

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
		protected void onPostExecute(String result) {
			//Si recibimos +OK, ponemos la variable a verdadero
			if(result.substring(3,6).equals(OK)){
				autenticado=true;			
			} //Si no, a falso
			else {
				autenticado=false;
				//Fallo al introducir usuario no existente
				if(result.substring(7,14).equals("usuario"))
					Toast.makeText(SecondActivity.this,
							getResources().getString(R.string.nouser),
							Toast.LENGTH_SHORT).show();
				//Fallo por contraseña errónea
				else if(result.substring(7,15).equals("password")){
					Toast.makeText(SecondActivity.this,
							getResources().getString(R.string.nopass),
							Toast.LENGTH_SHORT).show();
				}
				else //En otro caso, mostramos el error recibido
					Toast.makeText(SecondActivity.this,
							result,
							Toast.LENGTH_SHORT).show();
				//finish(); //Finalizamos la activity
			}
			
			if (pbar != null)
				pbar.dismiss();

		}

	}
	
	
	
	/**
	 * @return String con la fecha actual en formato año-mes-dia
	 */
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
	
	
	/**
	 * @param ope El tipo de operacion que ha realizado el usuario
	 * @param res El resultado de esa operacion
	 */
	public void onSave(String ope, String res) {
		double value;
		
		EditText edit_v = (EditText) findViewById(R.id.valor);
		String valor = edit_v.getEditableText().toString();
		String filename = usuario;
		String operacion=ope, fec=getFecha();
		
		if (valor != null)
			try {
				value = Double.valueOf(valor);
			} catch (NumberFormatException es) {
				value = 0;
			}
		else
			value = 0;
         
		try {
			FileOutputStream os = openFileOutput(filename, MODE_PRIVATE | MODE_APPEND);
			/* Escritura sin serialización*/
			DataOutputStream dos = new DataOutputStream(os);
			//Escribimos los diferentes campos que nos interesan en el fichero
			dos.writeUTF(operacion);
			dos.writeDouble(value);
			dos.writeUTF(res);
			dos.writeUTF(fec);
			dos.flush();
			dos.close();

			
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
		SocketComprueba task = new SocketComprueba();
		task.execute(aux);
		if(autenticado==true){
		String texto = "";
		//TextView panel = (TextView)findViewById(R.id.textView1);
		
		try {
			
			String filename = usuario;

			FileInputStream is = openFileInput(filename);
			
			/* Lectura sin serialización */
			DataInputStream dos = new DataInputStream(is);
			panel.setText("");
			//Mostramos las entradas guardadas para este usuario
			while (dos.available() > 0) {
				texto = texto + " Operacion: " + dos.readUTF() + " Valor:"
						+ dos.readDouble() + " Resultado:"+dos.readUTF()+
						" Fecha:"+dos.readUTF()+"\r\n";
			}
			dos.close();	

					
			is.close();
			panel.setText(texto);

			
			
			Toast.makeText(this,
					getResources().getString(R.string.toast_loaded),
					Toast.LENGTH_SHORT).show();
			
		} catch (IOException ex) {
			
			}
		}
	}
	
	
	public void onSin(View v){
		SocketConnection task = new SocketConnection();
		String aux2=aux+":sin"; //Añadimos a la ip y al puerto la operacion seno
		task.execute(aux2);
		
	}
	
	public void onCos(View v){
		SocketConnection task = new SocketConnection();
		String aux2=aux+":cos"; //Añadimos a la ip y al puerto la operacion coseno
		task.execute(aux2);
		
	}
	

}
