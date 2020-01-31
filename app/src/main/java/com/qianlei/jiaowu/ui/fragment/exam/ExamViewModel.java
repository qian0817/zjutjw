package com.qianlei.jiaowu.ui.fragment.exam;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 考试信息相关的ViewModel
 *
 * @author qianlei
 */
public class ExamViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    LiveData<String> getText() {
        return mText;
    }
}