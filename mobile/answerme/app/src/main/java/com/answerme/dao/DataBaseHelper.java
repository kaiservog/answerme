package com.answerme.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.answerme.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by rctoscano on 29/01/2016.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String dataBaseName = "answerme.db";
    private static final int dataBaseVersion = 1;

    public DataBaseHelper(Context context){
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, User.class);
        }
        catch (SQLException e) {
            Log.e("DataBase", "Error create table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sb, ConnectionSource cs, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(cs, User.class, true);
            onCreate(sb, cs);
        }
        catch (SQLException e) {
            Log.e("DataBase", "Error create table", e);
        }
    }
}
