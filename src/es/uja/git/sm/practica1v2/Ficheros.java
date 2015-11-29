package es.uja.git.sm.practica1v2;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import es.uja.git.sv.examples.R;
import es.uja.git.sv.examples.SQLiteTableRecords;
import es.uja.git.sv.utils.Record;


public class Ficheros extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ficheros);
		
		database = new SQLiteTableRecords(this);
		database.open();
		
	}
	
	public void onSaveDatabase(View view) {

		if(database!=null)
		{	
			database.addRecord(getData());
		}
	}

	public void onLoadDatabase(View view) {

		List<Record> comentarios =database.getAllRecords();
		TextView resultado = (TextView) findViewById(R.id.ficheros_textView_result);
		String texto="";
		for(Record c : comentarios)
		{
			texto=texto+"\r\n"+c.getTag()+" valor="+c.getValue();

			

		}
		
		resultado.setText(texto);

	}

}
