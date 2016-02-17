package com.answerme.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.answerme.model.User;



/**
 * Created by rctoscano on 23/01/2016.
 */
public class UserDAO {

    private SQLiteDatabase db;

    public UserDAO(Context context){
        db = context.openOrCreateDatabase("answerme.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    public static void createTable(SQLiteDatabase dataBase, String tableName){
        try{
            dataBase.beginTransaction();

            String sql =    "CREATE TABLE IF NOT EXISTS " +tableName+ " (" +
                            "  id INTEGER PRIMARY KEY, " +
                            "  email TEXT, "+
                            "  password TEXT " +
                            "); ";
            dataBase.execSQL(sql);

            //Commit
            dataBase.setTransactionSuccessful();
        }
        finally {
            dataBase.endTransaction();
        }
    }

}
