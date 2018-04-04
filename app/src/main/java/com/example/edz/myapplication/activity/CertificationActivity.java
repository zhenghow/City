package com.example.edz.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edz.myapplication.R;
import com.example.edz.myapplication.bean.BaseBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CertificationActivity extends AppCompatActivity {

    @Bind(R.id.back_certificationActivity)
    ImageView backCertificationActivity;
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.edit_useridcard)
    EditText editUseridcard;
    @Bind(R.id.button_certificationActivity)
    Button buttonCertificationActivity;
    private BaseBean baseBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_certificationActivity, R.id.button_certificationActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_certificationActivity:
                finish();
                break;
            case R.id.button_certificationActivity:
                String name=editName.getText().toString();
                String idcard=editUseridcard.getText().toString();
//                OkGo.post().tag(this)
//                        .params("id",)
//                        .params("name",name)
//                        .params("idcard",idcard)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                Gson gson=new Gson();
//                                baseBean = gson.fromJson(response,BaseBean.class);
//                                selectionState(baseBean.getCode());
//                            }
//                        });
                break;
        }
    }

    private void selectionState(int code) {
        switch (code){
            case 10000:
                Intent intent=new Intent(CertificationActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case 10400:
                Toast.makeText(CertificationActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                break;
            case 10410:
                Toast.makeText(CertificationActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                break;
            case 10420:
                Toast.makeText(CertificationActivity.this, baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
