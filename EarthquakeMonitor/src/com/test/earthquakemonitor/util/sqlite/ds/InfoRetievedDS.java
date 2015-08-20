package com.test.earthquakemonitor.util.sqlite.ds;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.test.earthquakemonitor.entity.InfoRetrieved;
import com.test.earthquakemonitor.util.sqlite.MySQLiteHelper;

public class InfoRetievedDS {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_DATA 
	};
	
	public InfoRetievedDS(Context context){
		dbHelper = new MySQLiteHelper(context); 
	}

	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public void close() {
	    dbHelper.close();
	}
	
	public void deleteInfo(InfoRetrieved data) {
	    long id = data.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_SERVERDATA, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public InfoRetrieved saveInfo(InfoRetrieved infoRetrieved){
		System.out.println("savingInfo: " + infoRetrieved.toString().substring(0,70));
		ContentValues values = new ContentValues();
		long insertId;
		values.put(MySQLiteHelper.COLUMN_DATA, infoRetrieved.getInfoFromServer());
		if(infoRetrieved.getId() != null && infoRetrieved.getId() > 0){
			String strFilter = "_id=" + infoRetrieved.getId();
			insertId = infoRetrieved.getId();
			database.update(MySQLiteHelper.TABLE_SERVERDATA, values, strFilter , null);
		}
		else {
			System.out.println("saving unexistent");
			insertId = database.insert(MySQLiteHelper.TABLE_SERVERDATA, null, values);
		}
		System.out.println("inserted: " + insertId + "  String: " + infoRetrieved.getInfoFromServer().substring(0, 70));
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SERVERDATA, allColumns, 
	    		MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    infoRetrieved = cursorToData(cursor);
	    cursor.close();
	    // as I'm using this, there must not exist, but in the case that something else was created It must be implemented a method to keep only one record
		return infoRetrieved;
	}
	
	public List<InfoRetrieved> getInfo() {
	    List<InfoRetrieved> dataFromServer = new ArrayList<InfoRetrieved>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SERVERDATA,
	        allColumns, null, null, null, null, MySQLiteHelper.COLUMN_ID + " desc");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	InfoRetrieved data = cursorToData(cursor);
	    	dataFromServer.add(data);
	    	cursor.moveToNext();
	    }

	    cursor.close();
	    return dataFromServer;
	}

	private InfoRetrieved cursorToData(Cursor cursor) {
		InfoRetrieved data = new InfoRetrieved();
	    data.setId(cursor.getLong(0));
	    data.setInfoFromServer(cursor.getString(1));
	    return data;
	}
}
