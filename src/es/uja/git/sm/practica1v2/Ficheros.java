package es.uja.git.sm.practica1v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





public class Ficheros extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ficheros);
		
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
			/* Escritura sin serialización*/
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(operacion);
			dos.writeInt(value);
			dos.flush();
			dos.close();

			/*Escritura con serialización*/
//			Record data = new Record(tag,value);
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(data);
//			oos.flush();
//			oos.close();
			/*Fin escritura con serialización*/
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
			
			/* Lectura sin serialización */
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
