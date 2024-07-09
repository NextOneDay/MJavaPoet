package com.nextoneday.mybutterknife;
/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 *  	                   	Create/Add/Modify/Delete
 * ===========================================================================================
 */

import android.util.Log;

/**
 * Author: shah
 * Date : 2020/5/5.
 * Desc : MyButterknife
 */
public class MyButterknife {
    public static final String TAG = MyButterknife.class.getSimpleName();
    public static  void bind(Object target) {

       String name =  target.getClass().getName() + "$ViewBinder";
        Log.d(TAG, "name:" + name);
        try {
            Class<?> aClass = Class.forName(name);
            ViewBinder  binder= (ViewBinder) aClass.newInstance();
            binder.bind(target);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
