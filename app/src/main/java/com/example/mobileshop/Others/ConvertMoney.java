package com.example.mobileshop.Others;

import java.text.DecimalFormat;

public class ConvertMoney {


    //Chuyển tìền có dấu . với thêm đ cho đẹp
    public ConvertMoney(){

    }
    public String StringToMoney(String string){
        double amount = Double.parseDouble(string);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount)+" đ";
    }

    public String MoneyToString(String money){
        money = money.replace(",","");
        money = money.replace(" đ","");
        return money;
    }
}
