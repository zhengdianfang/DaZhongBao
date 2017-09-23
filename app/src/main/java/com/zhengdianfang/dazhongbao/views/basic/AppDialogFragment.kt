package com.zhengdianfang.dazhongbao.views.basic

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Created by dfgzheng on 25/08/2017.
 */
class AppDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog.Builder(context).progress(true, 100).build()!!
    }

    override fun show(manager: FragmentManager, tag: String) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}