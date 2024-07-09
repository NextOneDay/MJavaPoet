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

import android.view.View;

/**
 * Author: shah
 * Date : 2020/5/6.
 * Desc : MyOnClickListener
 */
public abstract class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
       doClick(v);
    }

    public abstract  void doClick(View view);

}
