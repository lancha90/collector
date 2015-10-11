package colector.co.com.collector.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import colector.co.com.collector.settings.AppSettings;

public class DriverSQL{

	protected SQLiteHelper sqLiteHelper;
	protected Context ctx;
	
	public DriverSQL(Context ctx) {
		this.ctx = ctx;
	}

	public SQLiteDatabase getDBRead() throws SQLException {
		sqLiteHelper = new SQLiteHelper(this.ctx, null);
		return sqLiteHelper.getReadableDatabase(); 
	}

	public SQLiteDatabase getDBWrite() throws SQLException {
		sqLiteHelper = new SQLiteHelper(this.ctx, null);
		return sqLiteHelper.getWritableDatabase(); 
	}
	
	public void close() {
		this.sqLiteHelper.close();
	}

	/**
	 * Delete all entity
	 */
	public void deleteAll(String table) {
		SQLiteDatabase db = getDBWrite();
		db.delete(table, null, null);
		close();
	}
	
	
	public class SQLiteHelper extends SQLiteOpenHelper {
		
		public SQLiteHelper(Context context, CursorFactory factory) {
			super(context, AppSettings.DB_NAME, factory,AppSettings.DB_VERSION);
//			super(context, "/storage/sdcard0/DB_DMS_MOBILE.db", factory, AppSettings.DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table if not exists TBL_SURVEY ( ID  INTEGER ,  NAME  TEXT,DESCRIPTION  TEXT, PRIMARY KEY (ID));");
			db.execSQL("create table if not exists TBL_SECTION ( ID  INTEGER ,  NAME  TEXT,DESCRIPTION  TEXT, SURVEY INTEGER, PRIMARY KEY (ID));");
			db.execSQL("create table if not exists TBL_QUESTION ( ID  INTEGER ,TYPE  INTEGER,  NAME  TEXT,DESCRIPTION  TEXT, SECTION  INTEGER,  PRIMARY KEY (ID));");
			db.execSQL("create table if not exists TBL_RESPONSE ( ID  INTEGER ,  VALUE  TEXT, QUESTION  INTEGER , PRIMARY KEY (ID));");
			db.execSQL("create table if not exists TBL_RESPONSE_COMPLEX ( ID  INTEGER ,  VALUE  TEXT, QUESTION  INTEGER , PRIMARY KEY (ID));");
			db.execSQL("create table if not exists TBL_RESPONSE_COMPLEX_OPTION ( ID  INTEGER ,  VALUE  TEXT, COMPLEX  INTEGER , PRIMARY KEY (ID));");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			//db.execSQL("DROP TABLE IF EXISTS TBL_SURVEY");
	        onCreate(db);
		}
	}

}
