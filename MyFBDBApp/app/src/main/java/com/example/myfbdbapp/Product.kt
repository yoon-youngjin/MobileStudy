package com.example.myfbdbapp

data class Product(var pId:Int,var pName:String, var pQuantity:Int) {
    constructor() :this(0,"noinfo",0) // 디폴트 생성자 필수
}
