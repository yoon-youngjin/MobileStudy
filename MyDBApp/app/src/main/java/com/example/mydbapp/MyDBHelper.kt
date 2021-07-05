package com.example.mydbapp

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
    // 질의문 던져서 데이터베이스모든 내용 가져옴
    fun getAllRecord() {
        // 질의문 : 데이터베이스모든 내용 가져옴 -> select 모든(*) 테이블로 부터
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase // 읽기모드
        //cursor 반환 -> 타이틀 생성, 데이터 추가 작업(showrecord)
        val cursor = db.rawQuery(strsql,null)

//        if(cursor.count!=0) { // count= 0 인경우는 showRecord수행 안하므로 다지워도 하나가 남음
//            showRecord(cursor)
//        }

        showRecord(cursor)
        cursor.close()
        db.close()

    }

    private fun showRecord(cursor: Cursor) {
        // cursor를 처음 데이터 위치로 옮기기
       cursor.moveToFirst()
        // 속성 개수 return -> colunCount 만큼 타이틀만들기위함
        val attrcount = cursor.columnCount
        // 메인 activity접근
        val activity = context as MainActivity
        // 레이아웃의 모든 뷰 삭제 -> removeAllviewsinlayout
        activity.binding.tableLayout.removeAllViewsInLayout()
        // 타이틀 만들기 -> TableRow객체
        val tablerow = TableRow(activity)
        // 동적 생성시 -> 파라메터 정보 있어야함 -> LayoutParams (폭,높이)
        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT)

        tablerow.layoutParams = rowParam
        // product정보 넘겨줌 -> 뷰를 만들어 넘긴다 -> 뷰의 파라메터 정보
        val viewParam = TableRow.LayoutParams(0,100,1f)
        // 뷰에 속성 넣는 작업 -> 타이틀 만들기
        for(i in 0 until attrcount) {
            val textView = TextView(activity)
            textView.layoutParams = viewParam
            // i번째 콜룸 네임
            textView.text = cursor.getColumnName(i)
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize =  15.0f
            textView.gravity = Gravity.CENTER
            // row에 textview 붙힘
            tablerow.addView(textView)
        }
        // 메인의 tablelayout에 tablerow를 붙힌다
        activity.binding.tableLayout.addView(tablerow)
        if(cursor.count==0) return // 예외처리 찾는데이터 없는경우 타이틀만 출력
        //레코드 추가하기
        do {
            //테이블 한줄 생성
            val row = TableRow(activity)
            row.layoutParams = rowParam

            // row클릭시 이벤트처리 -> 동적생성시 -> id값 x -> tag로 대체(식별가능)
            row.setOnClickListener {
                for(i in 0 until attrcount) {
                    //textView id값 동적으로 생성했으므로 없음

                    val textView = row.getChildAt(i) as TextView//한줄의 child객체 get 뷰반환
                    when(textView.tag) {
                        0 -> activity.binding.pIdEdit.setText(textView.text) // editText에 데이터 set
                        1 -> activity.binding.pNameEdit.setText(textView.text)
                        2 -> activity.binding.pQuantityEdit.setText(textView.text)

                    }

                }
            }
            // textview만듬 -> 한사이클 돌면 하나의 레코드를 읽어서 추가시킨것 -> 레코드 개수만큼 반복
            for(i in 0 until attrcount) {
                val textView = TextView(activity)
                textView.tag = i//식별용
                textView.layoutParams = viewParam
                //커서로 부터 데이터를 읽어오는 작업
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
        val db = writableDatabase // 쓰기모드
        //쓰기 -> 에러 발생시 -1리턴 , 아닌경우 양수 리턴 -> db close
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag
    }

    //db생성시 호출
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 -> sql 구문 -> 테이블 존재x 생성 , 속성: pid:int(primary key -> null, 중복 x), pname:text , pquantity:int

        val create_table = "create table if not exists $TABLE_NAME("+
                "$PID integer primary key autoincrement, "+
                "$PNAME text," +
                "$PQUANTITY integer);"
        //질의문 실행 -> execSQL
        db!!.execSQL(create_table )
    }
    //db버전 변경시 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //table값 삭제 ->존재하면 삭제
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)

    }
    //select *from product where name = 'name';
    //질의문 작성 -> 해당 네임을 가진 row : select *(모든) 테이블 네임의 pName의 name인것 아이템 리턴
    fun findProduct(name:String):Boolean {
        val strsql = "select*from $TABLE_NAME where $PNAME='$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        //데이터 없는경우 타이틀 정보만 출력-> if(cursor.count==0) return , 있는 경우 해당 데이터 출력
        showRecord(cursor) // 찾은 데이터 있는경우 타이틀,레코드 모두 출력 , 찾는 데이터 없는 경우 타이틀만 출력
        cursor.close()
        db.close()
        return flag
        }

    //select *from product where pid = 'pid';
    fun deletProduct(pid: String): Boolean {
        // 질의문 : 삭제 루틴 : pid해당 데이터 찾고 지움
        val strsql = "select*from $TABLE_NAME where $PID='$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        // cursor.count!=0 -> 데이터 찾았다.
        val flag = cursor.count!=0
        if(flag) {
            cursor.moveToFirst()
            //delete(테이블 네임,where구문 -> '?'는 외부에서 값지정(arrayof(pid)),?에 들어가는 값 -> 여러개일수있으므로 array로)
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
            //update(테이블네임,values,조건문,?값) -> 조건 : pid해당하는 값이 ?인 경우 update하겠다.
            db.update(TABLE_NAME, values,"$PID=?", arrayOf(pid.toString( ))) // pid에 해당하는 값이 arrayOf인경우 update하겠다.

        }
        cursor.close()
        db.close()
        return flag
    }
    //select *from product where pname like '김%'; -> 김으로 시작하는 모든 product 나열
    // 해당 name으로 시작하는 아이템 나열
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