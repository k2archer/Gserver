package pk.wei.com.gserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginName;
    private DataManager dataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataManager = DataManager.getInstance();

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
                int check = dataManager.login(loginName.getText().toString());
                if (check == 1) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    this.finish();
                } else {
                    Toast.makeText(this, "Login fail." + check, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
