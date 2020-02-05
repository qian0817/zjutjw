package com.qianlei.jiaowu.ui.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qianlei.jiaowu.MainApplication;
import com.qianlei.jiaowu.R;
import com.qianlei.jiaowu.common.Result;
import com.qianlei.jiaowu.databinding.FragmentLoginBinding;

import org.jetbrains.annotations.NotNull;

/**
 * @author qianlei
 */
public class LoginFragment extends Fragment {
    private static final String ID = "id";
    private static final String PASSWORD = "password";
    private static final String USER = "user";

    private LoginViewModel mViewModel;
    private SharedPreferences preferences;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        ViewModelProvider.AndroidViewModelFactory factory = new ViewModelProvider.AndroidViewModelFactory(MainApplication.getInstance());
        mViewModel = factory.create(LoginViewModel.class);

        preferences = MainApplication.getInstance().getSharedPreferences(USER, Context.MODE_PRIVATE);
        mViewModel.getCaptcha().observe(this.getViewLifecycleOwner(), this::changeCaptcha);
        mViewModel.getLoginResult().observe(this.getViewLifecycleOwner(), this::login);
        //获取之前填写的学号和密码
        getRememberPassword();
        binding.setLoginViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        mViewModel.changeCaptcha();

        return binding.getRoot();
    }

    private void getRememberPassword() {
        String studentId = preferences.getString(ID, "");
        String password = preferences.getString(PASSWORD, "");
        binding.setPassword(password);
        binding.setStudentId(studentId);
    }

    private void changeCaptcha(@NotNull Result<Bitmap> result) {
        if (result.isSuccess()) {
            binding.captchaImage.setImageBitmap(result.getData());
        } else {
            Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private void login(@NotNull Result<String> result) {
        final String studentId = binding.getStudentId();
        final String password = binding.getPassword();
        if (result.isSuccess()) {
            //保存用户名 密码
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ID, studentId);
            editor.putString(PASSWORD, password);
            editor.apply();
            //这个广播用于修改header中的信息
            Context context = MainApplication.getInstance();
            Intent intent = new Intent("com.qianlei.jiaowu.LOGIN_BROADCAST");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            //跳转到课程界面
            NavController controller = Navigation.findNavController(binding.getRoot());
            controller.navigate(R.id.action_navigation_login_to_navigation_lesson);
        } else {
            mViewModel.changeCaptcha();
            binding.setCaptcha("");
            Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
