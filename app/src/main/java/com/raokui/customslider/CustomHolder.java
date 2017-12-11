package com.raokui.customslider;

import android.content.Context;
import android.view.View;

/**
 * Created by 饶魁 on 2017/12/11.
 */

public interface CustomHolder<T> {

    View create(Context context);

    void bindData(Context context, int position, T data);

}
