package com.example.mybr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//background -> 앱실행중 아니여도 호출
//manifest에 추가 -> 수신할 액션 정보 manifest에 intentfilter로 작성
//<receiver
//android:name=".MyReceiver"
//android:enabled="true"
//android:exported="true"></receiver>
//<receiver
//android:name=".MyReceiver"
//android:enabled="true"
//android:exported="true">
//<intent-filter>
//<!-- 문자메세지 액션  권한 필요-->
//<action android:name="android.provider.Telephony.SNS_RECEIVED"/>
//</intent-filter>
//</receiver>
class MyReceiver : BroadcastReceiver() {
    //사용 패턴 정의
//    1) 숫자만 : ^[0-9]*$
//
//    2) 영문자만 : ^[a-zA-Z]*$
//
//    3) 한글만 : ^[가-힣]*$
//
//    4) 영어 & 숫자만 : ^[a-zA-Z0-9]*$
//
//    5) E-Mail : ^[a-zA-Z0-9]+@[a-zA-Z0-9]+$
//
//    6) 휴대폰 : ^01(?:0|1|[6-9]) - (?:\d{3}|\d{4}) - \d{4}$
//
//    7) 일반전화 : ^\d{2,3} - \d{3,4} - \d{4}$
//
//    8) 주민등록번호 : \d{6} \- [1-4]\d{6}
//
//    9) IP 주소 : ([0-9]{1,3}) \. ([0-9]{1,3}) \. ([0-9]{1,3}) \. ([0-9]{1,3})
//
//
//
//    출처: https://highcode.tistory.com/6 [HighCode]

    // . -> 하나의 문자 , ? -> 하나와도되고 안와도됨 , + -> 하나이상 와야함 , | -> 패턴 여러개인경우
    // [abc] -> a or b or c 인경우==[a-c],[^abc] -> abc빼고 , \s -> spacebar , \w -> word
    // * -> 하나 없어도되고, 하나이상 , ^ -> 이패턴으로 시작 , $ 이패턴으로 끝나야함 ,
    val usagePattern = Regex("""^[가~힣a~zA~Z0~9]+$""") // 한글 , 영어대소문자,숫자로 꼭 하나로 시작하고 끝나야함 문자열 : 개미식당
    val paymentPattern = Regex("""^*\d{3}원$""") // 최소 정수 3자리이고 원으로 끝(원$) -> 가격
    val dayPattern = Regex("""^\d{2}/\d{2}""") // 00/00 -> 날짜
    val scope = CoroutineScope(Dispatchers.IO)
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync() // onReceive완료 후에도 finish호출 전까지 myReceiver지우지 않는다
        scope.launch {
            if(intent.action.equals("android.provider.Telephony.SNS_RECEIVED")) {
                // 문자메세지는 bundle로 수신
                val  bundle = intent.extras
                // bundle타입의 pdu포멧  , object(any)타입의배열로 전달
                val objects = bundle?.get("pdus") as Array<Any>
                // 첫 문자 메세지
                val sms = objects[0] as ByteArray
                // 포멧정보 가져오기
                val format = bundle.getString("format")
                val message = SmsMessage.createFromPdu(sms,format)
                val msg = message.messageBody
                if(msg.contains("건국카드")) { // 특정 문자열 포함된 경우만 intent
                    val tempstr = msg.split("\n")
                    var resultstr = ""
                    //첫줄은 건국카드이므로 빼고
                    for(str in tempstr.subList(1, tempstr.size)) {
                        //사용패턴이랑 매치되는 경우 resultstr에+
                        // regex.containMatchIn(text) : text에 매치되는게 하나라도 있는경우
                        // regex.MatchEntire(text) : text전체가 매치되는 경우
                       // regex.find(text,idx) : idx번호부터 text찾기
                           // regex.findAll(text,idx) : idx번호부터 text전체 찾기 -> 여러개인경우
                        if(usagePattern.containsMatchIn(str)) {
                            resultstr += "$str : "
                        }
                        if(paymentPattern.containsMatchIn(str)) {
                            resultstr += "$str : "
                        }
                        if(dayPattern.containsMatchIn(str)) {
                            val temp = str.split(" ") // 05/05 12:00 공백으로 구분
                            val daytemp = temp[0].split("/")
                            val month = daytemp[0] // 월
                            val day = daytemp[1] // 일
                            resultstr += "${month}월 ${day}일 카드 승인됨"
                        }
                    }
                    //다른 패턴의 문자일 경우 intent x
//                    if(!resultstr.isEmpty()) return
//                    val newIntent = Intent(context,MainActivity::class.java)
//                    newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    newIntent.putExtra("msg",resultstr)
//                    context.startActivity(newIntent)
                    if(!resultstr.isEmpty()) {
                        val newIntent = Intent(context, MainActivity::class.java)
                        newIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        newIntent.putExtra("msg", resultstr)
                        //api 29이상 기기부터는 receiver내에서 엑티비티 명시적 수행 x -> 권한 부여
                        context.startActivity(newIntent)
                    }
                    pendingResult.finish()
                }
        }




//            // 문자 메세지 수신시 background에서 받아서 정보를 activity에 넘겨주는 작업
//            val newIntent = Intent(context,MainActivity::class.java)
//            // receiver가 백그라운모드일 경우 있으므로 flag정보로 상태 확인 필요 / activity실행중이지 않은 상황 : FLAG_ACTIVITY_NEW_TASK -> 새로운 테스크로 만들어서 엑티비티 실행
//            // activity가 background상태 : FLAG_ACTIVITY_SINGLE_TOP
//            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            newIntent.putExtra("msg",message.messageBody)
//            context.startActivity(newIntent)

        }

    }
}

