package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editSignUpActivityFullName,editSignUpActivityName,editSignUpActivityPassword,editSignUpActivityRePassword;
    private Button btnSignUpActivityNext;
    private ImageButton btnSignUpActivityBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editSignUpActivityFullName = findViewById(R.id.editSignUpActivityFullName);
        editSignUpActivityName = findViewById(R.id.editSignUpActivityName);
        editSignUpActivityPassword = findViewById(R.id.editSignUpActivityPassword);
        editSignUpActivityRePassword = findViewById(R.id.editSignUpActivityRePassword);
        btnSignUpActivityNext = findViewById(R.id.btnSignUpActivityNext);
        btnSignUpActivityBack = findViewById(R.id.btnSignUpActivityBack);

        btnSignUpActivityNext.setOnClickListener(this);
        btnSignUpActivityBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUpActivityNext:{
                if(editSignUpActivityFullName.getText().toString().equals("") || editSignUpActivityName.getText().toString().equals("")
                        ||editSignUpActivityPassword.getText().toString().equals("") ||editSignUpActivityRePassword.getText().toString().equals(""))
                {
                    Toast.makeText(SignUpActivity.this , "Vui lòng nhập đầy đủ thông tin đăng ký", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!editSignUpActivityPassword.getText().toString().equals(editSignUpActivityRePassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(editSignUpActivityPassword.length()<9){
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải lớn hơn 8 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                intent.putExtra("intend_User",editSignUpActivityName.getText().toString());
                intent.putExtra("intend_FullName",editSignUpActivityFullName.getText().toString());
                intent.putExtra("intend_Password",editSignUpActivityPassword.getText().toString());
                startActivity(intent);
            }break;
            case R.id.btnSignUpActivityBack:{
                onBackPressed();
            }break;
        }

    }
}