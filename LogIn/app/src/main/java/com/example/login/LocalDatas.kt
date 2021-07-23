package com.example.login

import android.provider.BaseColumns

object LocalDatas { //  로컬 데이터 베이스의 자료형태 정의된 object
    object userData : BaseColumns {  //  users 라는 DB 테이블의 데이터 컬럼 내용 정리
        const val TABLE_NAME = "users"
        const val COLUMN_NAME_ID = "id"                                 //  임의의 컬럼명 작성
        const val COLUMN_NAME_PASSWORD = "password"  // 임의의 컬럼명 작성
        const val COLUMN_NAME_ORGAN = "organization"

    }
    object Groups :BaseColumns{ // 만약 그룹에 관련한 DB 형식을 지정하고 싶다면 동일한 방식으로 추가합니다.

    }
}
