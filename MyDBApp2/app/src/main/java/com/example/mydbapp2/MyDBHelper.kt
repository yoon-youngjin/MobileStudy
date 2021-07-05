package com.example.mydbapp2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class MyDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{ // static멤버 ,DB관련 내용 선언
        val DB_NAME = "mydb.db"
        val DB_VERSION = 1
        val TABLE_NAME = "products"
        val PID = "pid"
        val PNAME = "pname"
        val PQUANTITY = "pquantity"
    }
    fun getAllRecord() {
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql,null)
//        if(cursor.count!=0) { // count= 0 인경우는 showRecord수행 안하므로 다지워도 하나가 남음
//            showRecord(cursor)
//        }
        showRecord(cursor)
        cursor.close()
        db.close()
    }

    private fun showRecord(cursor: Cursor) {
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        val activity = context as MainActivity
        activity.binding.tableLayout.removeAllViewsInLayout()
        //타이틀 만들기
        val tablerow = TableRow(activity)
        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT)
         tablerow.layoutParams = rowParam
        val viewParam = TableRow.LayoutParams(0,100,1f)
        for(i in 0 until attrcount) {
            val textView = TextView(activity)
            textView.layoutParams = viewParam
            textView.text = cursor.getColumnName(i)
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize =  15.0f
            textView.gravity = Gravity.CENTER
            tablerow.addView(textView)
        }

        activity.binding.tableLayout.addView(tablerow)
        if(cursor.count==0) return // 예외처리 찾는데이터 없는경우 타이틀만 출력
        //레코드 추가하기
        do {
            val row = TableRow(activity)
            row.layoutParams = rowParam
            row.setOnClickListener {
                for(i in 0 until attrcount) {
                    //textView id값 동적으로 생성했으므로 없음

                    val textView = row.getChildAt(i) as TextView//한줄의 child객체 get 뷰반환
                    when(textView.tag) {
                        0 -> activity.binding.pIdEdit.setText(textView.text)
                        1 -> activity.binding.pNameEdit.setText(textView.text)
                        2 -> activity.binding.pQuantityEdit.setText(textView.text)

                    }

                }
            }
            for(i in 0 until attrcount) {
                val textView = TextView(activity)
                textView.tag = i//식별용
                textView.layoutParams = viewParam
                textView.text = cursor.getString(i)
                textView.textSize =  13.0f
                textView.gravity = Gravity.CENTER
                row.addView(textView)
            }
            activity.binding.tableLayout.addView(row )
        }while(cursor.moveToNext()) //커서 다음으로 넘어가서 읽을 수 있는게 다음에 있으면 반복




    }

    fun insertProduct(product: Product):Boolean {
        val values = ContentValues()
        values.put(PNAME, product.pName)
        values.put(PQUANTITY, product.pQuantity)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$PID integer primary key autoincrement, "+
                "$PNAME text," +
                "$PQUANTITY integer);"
        db!!.execSQL(create_table )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)

    }
    //select *from product where name = 'name';

    fun findProduct(name:String):Boolean {
        val strsql = "select*from $TABLE_NAME where $PNAME='$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        showRecord(cursor) // 찾은 데이터 있는경우 타이틀,레코드 모두 출력 , 찾는 데이터 없는 경우 타이틀만 출력
        cursor.close()
        db.close()
        return flag
        }

    //select *from product where pid = 'pid';
    fun deletProduct(pid: String): Boolean {
        val strsql = "select*from $TABLE_NAME where $PID='$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag) {
            cursor.moveToFirst()
            db.delete(TABLE_NAME,"$PID=?", arrayOf(pid)) // ? -> arrayOF(pid)
        }
        cursor.close()
        db.close()
        return flag
    }

    fun updateProduct(product: Product): Boolean {
        val pid = product.pId
        val strsql = "select*from $TABLE_NAME where $PID='$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag) {
            cursor.moveToFirst()
            val values = ContentValues()

            values.put(PNAME, product.pName)
            values.put(PQUANTITY, product.pQuantity)
            db.update(TABLE_NAME, values,"$PID=?", arrayOf(pid.toString( ))) // pid에 해당하는 값이 arrayOf인경우 update하겠다.

        }
        cursor.close()
        db.close()
        return flag
    }
    //select *from product where pname like '김%'; -> 김으로 시작하는 product
    fun findProduct2(name: String): Boolean {
        val strsql = "select*from $TABLE_NAME where $PNAME like '$name%';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        showRecord(cursor) // 찾은 데이터 있는경우 타이틀,레코드 모두 출력 , 찾는 데이터 없는 경우 타이틀만 출력
        cursor.close()
        db.close()
        return flag
    }

}