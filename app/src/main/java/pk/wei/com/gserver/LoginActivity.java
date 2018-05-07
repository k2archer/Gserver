package pk.wei.com.gserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = new Intent(this, BookService.class);
        startService(i);

        initView();
    }

    private void initView() {
        loginName = (EditText) findViewById(R.id.login_name_et);
        findViewById(R.id.login_login_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_bt:

                break;
        }
    }
}
