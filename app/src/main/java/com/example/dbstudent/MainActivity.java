package com.example.dbstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText Id,Name,delete,Phn;
    Button bt1,bt2;
    myDbAdapter helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Id=(EditText)findViewById(R.id.editTextId);
        Name=(EditText)findViewById(R.id.editTextName);
        Phn=(EditText)findViewById(R.id.editTextPhn);
        bt1=(Button)findViewById(R.id.AddStud);
        bt2=(Button)findViewById(R.id.viewStud);
        delete=(EditText)findViewById(R.id.deleteText);
        helper=new myDbAdapter(this);
    }
    public void addstud(View view)
    {
        String t1=Id.getText().toString();
        String t2=Name.getText().toString();
        String t3=Phn.getText().toString();

        if (t1.isEmpty()||t2.isEmpty()||t3.isEmpty())
        {
            message("Please Enter All Fields...");
        }
        else
        {
            long id=helper.insertData(t1,t2,t3);
            if (id<=0)
            {
                message("Insertion Unsuccessful");
                Id.setText("");
                Name.setText("");
                Phn.setText("");
            }
            else
            {
                message("Insertion Successful");
                Id.setText("");
                Name.setText("");
                Phn.setText("");
            }
        }
    }

    public void viewStud(View view)
    {
        String data=helper.getData();
        message(data);
    }

    public void delete(View view)
    {
        String uname = delete.getText().toString();
        if (uname.isEmpty()) {
            message("Enter data");
        } else {
            int a = helper.delete(uname);
            if (a <= 0) {
                message("Unsuccessful");
                delete.setText("");
            } else {
                message("Deleted");
                delete.setText("");
            }
        }
    }

    public void message(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}

class myDbAdapter {
    myDbHelper myhelper;

    public myDbAdapter(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String compid,String name, String num) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.CID, compid);
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.NUMBER, num);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.CID,myDbHelper.NAME,myDbHelper.NUMBER};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int cd = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            @SuppressLint("Range") String compid = cursor.getString(cursor.getColumnIndex(myDbHelper.CID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(myDbHelper.NUMBER));
            buffer.append(cd + "   " + compid + "    " + name + "   " + number +"  \n ");
        }
        return buffer.toString();
    }

    public int delete(String uname)
    {
        SQLiteDatabase db=myhelper.getWritableDatabase();
        String[] whereArgs={uname};
        int count=db.delete(myDbHelper.TABLE_NAME,myDbHelper.NAME+" =?",whereArgs);
        return count;
    }
}

class myDbHelper extends SQLiteOpenHelper
{
    public static final String UID = "_id";
    static final String DATABASE_NAME="myDatabase";
    static final String TABLE_NAME="myTable";
    static final int DATABASE_Version=1;
    static final String CID="_compid";
    static final String NAME="Name";
    static final String NUMBER="Number";
    static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CID+" VARCHAR(255)," + NAME +" VARCHAR(255) ,"+ NUMBER +" VARCHAR(255));";
    static final String DROP_TABLE="DROP TABLE IF EXISTS "+TABLE_NAME;
    Context context;

    public myDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_Version);
        this.context=context;
    }
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e)
        {

        }
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        try
        {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch (Exception e)
        {

        }
    }
}