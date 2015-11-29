package es.uja.git.sm.practica1v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordsSQLiteHelper extends SQLiteOpenHelper implements RecordTableColumns {

	

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_RECORDS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_USER
			+ " text not null, " + COLUMN_OPER + " text not null, "+ COLUMN_VALUE + " real not null, "+
			COLUMN_RESULT + "real not null," + COLUMN_FECHA + "text not null);";

	public RecordsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
		onCreate(db);
	}

}