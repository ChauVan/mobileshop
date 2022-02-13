package com.example.mobileshop.Item;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileshop.R;

public class Item_Cart extends AppCompatActivity implements View.OnClickListener {
    
    private EditText edtItemCartAmount;
    private TextView txtplus,txtsub,txtItemCartProductDel;
    private CheckBox ckbItemCartCheck;


    //Này tạo bị lố vì nó được điều khiển bên Recyle View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cart);
        edtItemCartAmount = findViewById(R.id.edtItemCartAmount);
        txtplus = findViewById(R.id.txtplus);
        txtsub = findViewById(R.id.txtsub);
        txtItemCartProductDel = findViewById(R.id.txtItemCartProductDel);
        ckbItemCartCheck = findViewById(R.id.ckbItemCartCheck);

        txtplus.setOnClickListener(this);
        txtsub.setOnClickListener(this);
        txtItemCartProductDel.setOnClickListener(this);
        ckbItemCartCheck.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ckbItemCartCheck:{

            }break;

            case R.id.txtplus:{
                int s = Integer.parseInt(edtItemCartAmount.getText().toString()) + 1 ;
                edtItemCartAmount.setText(s);
            }break;

            case R.id.txtsub:{

            }break;

        }
    }
}