package com.zhengdianfang.dazhongbao.wxapi

import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.helpers.WechatUtils
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity

class WXEntryActivity : BaseActivity(), IWXAPIEventHandler {

    private var wxapi: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxapi = WXAPIFactory.createWXAPI(this, WechatUtils.APP_ID)
        wxapi!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxapi!!.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {
        toast(baseReq.transaction.toLowerCase())
    }

    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_SENDAUTH) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (baseResp is SendAuth.Resp) {
                    RxBus.instance.post(Action(Action.WEIXIN_OUTH_RESULT_ACTION, baseResp.code))
                }

            }else{
                RxBus.instance.post(Action(Action.WEIXIN_OUTH_RESULT_FAIL_ACTION, ""))
            }
        }
        finish()
    }
}
