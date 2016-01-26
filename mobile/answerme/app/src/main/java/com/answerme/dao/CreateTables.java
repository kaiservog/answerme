package com.answerme.dao;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import com.answerme.dao.UserDAO;


/**
 * Created by Toscano on 23/01/2016.
 */
public class CreateTables {

    private SQLiteDatabase db;
    private UserDAO userDAO;

    /**
     * Cria banco de dados na memoria do aparelho e
     * as tabelas caso nao existam.
     * @param context
     */
    public void create(Context context) {
        db = context.openOrCreateDatabase("answerme.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        userDAO.createTable(db, "user");
    }

    /**
     * Deleta banco de dados do aparelho.
     * @param context
     */
    private void delete(Context context){
        context.deleteDatabase("answerme.db");
    }

}
