package com.example.mobileshop.Others;

import android.content.Context;
import android.content.SharedPreferences;

public class DataToken {
    private Context context ;

    public DataToken(Context context) {
        this.context = context;
    }

    public void saveToken(String token){
        SharedPreferences settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", "Bearer " +  token);
        //Này là định thời gian hết hạn của token
        editor.putLong("expires", System.currentTimeMillis() + 82800000); //23 * 3600000 = 82800000
        editor.apply();
    }

    public String getToken() {
        SharedPreferences settings = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //Nếu mà thời gian token hết hạn thì lấy cái token mới
        long expires = settings.getLong("expires", 0);
        if (expires < System.currentTimeMillis()) {
            GetNewToken getNewToken = new GetNewToken(context);
            getNewToken.CallAPILoginGetToken();
        }
        String s = settings.getString("token", "");
        return s;
    }
}
