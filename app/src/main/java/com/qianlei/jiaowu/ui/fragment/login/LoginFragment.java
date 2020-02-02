package com.qianlei.jiaowu.ui.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qianlei.jiaowu.R;

/**
 * @author qianlei
 */
public class LoginFragment extends Fragment {
    private static final String ID = "id";
    private static final String PASSWORD = "password";

    private LoginViewModel mViewModel;
    private ImageView captchaImageView;
    private EditText captchaEditText;
    private Button loginButton;
    private EditText passwordEditText;
    private EditText studentIdEditText;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        init(root);
        mViewModel.getCaptcha().observe(this, result -> {
            if (result.isSuccess()) {
                captchaImageView.setImageBitmap(result.getData());
            } else {
                Toast.makeText(LoginFragment.this.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getLoginResult().observe(this, result -> {
            final String studentId = studentIdEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            if (result.isSuccess()) {
                //保存用户名 密码
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ID, studentId);
                editor.putString(PASSWORD, password);
                editor.apply();
                if (getContext() != null) {
                    Intent intent = new Intent("com.qianlei.jiaowu.LOGIN_BROADCAST");
                    LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
                }
                //跳转到课程界面
                NavController controller = Navigation.findNavController(root);
                controller.navigate(R.id.action_navigation_login_to_navigation_lesson);
            } else {
                mViewModel.changeCaptcha();
                captchaEditText.setText("");
                Toast.makeText(this.getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        captchaEditText.setOnClickListener(v -> mViewModel.changeCaptcha());

        loginButton.setOnClickListener(v -> {
            final String studentId = studentIdEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            final String captcha = captchaEditText.getText().toString();
            mViewModel.login(studentId, password, captcha);
        });
        getRememberPassword();
        mViewModel.changeCaptcha();
        return root;
    }

    /**
     * 获取之前的学号和密码
     */
    private void getRememberPassword() {
        if (getContext() != null) {
            preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            final String studentId = preferences.getString(ID, "");
            final String password = preferences.getString(PASSWORD, "");
            studentIdEditText.setText(studentId);
            passwordEditText.setText(password);
        }
    }

    private void init(View view) {
        captchaImageView = view.findViewById(R.id.captchaImage);
        captchaEditText = view.findViewById(R.id.captchaText);
        loginButton = view.findViewById(R.id.loginButton);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        studentIdEditText = view.findViewById(R.id.studentIdEditText);
    }

}
