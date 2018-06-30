package chinetek.xx.chntwms.cywms.Intentory;

import android.content.Context;
import android.os.Message;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chinetek.xx.chntwms.adapter.wms.Intentory.InventoryCanAdapter;
import chinetek.xx.chntwms.adapter.wms.Intentory.InventoryFincItemAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.ReturnMsgModelList;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.model.check.CheckAnalyze;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

@ContentView(R.layout.activity_intentory_can)
public class IntentoryCan extends BaseActivity {

    String TAG_GetCheckCan="IntentoryCan_GetCheckCan";
    private final int RESULT_Msg_GetCheckcan=101;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetCheckcan:
                AnalysisGetCheckCanJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }

    String checkno="";
Context context=IntentoryCan.this;
    @ViewInject(R.id.lsvInventoryDetail)
    ListView lsvInventoryDetail;
    InventoryCanAdapter InventorycanAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle("盘点参照", true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        checkno= getIntent().getStringExtra("checkno");
        InitListview(checkno);
    }

    private void InitListview(String checkno) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("no", checkno);
        String para = (new JSONObject(params)).toString();
        LogUtil.WriteLog(IntentoryCan.class, TAG_GetCheckCan, para);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetCheckCan,getString(R.string.Msg_GetCheckDetail), context, mHandler, RESULT_Msg_GetCheckcan,null,URLModel.GetURL().GetCkrefe, params, null);
    }



    void AnalysisGetCheckCanJson(String result){
        ArrayList<CheckAnalyze> CheckAnalyzes=new ArrayList<>();
        LogUtil.WriteLog(IntentoryCan.class, TAG_GetCheckCan,result);
        ReturnMsgModelList<CheckAnalyze> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<CheckAnalyze>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            CheckAnalyzes=returnMsgModel.getModelJson();
            InventorycanAdapter = new InventoryCanAdapter(context,CheckAnalyzes);
            lsvInventoryDetail.setAdapter(InventorycanAdapter);

        }else{
            MessageBox.Show(context,returnMsgModel.getMessage());
        }
    }

}
