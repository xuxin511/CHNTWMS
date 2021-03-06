package chinetek.xx.chntwms.cywms.Intentory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinetek.xx.chntwms.adapter.wms.Intentory.InventoryScanItemAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Base_Model;
import chinetek.xx.chntwms.model.ReturnMsgModel;
import chinetek.xx.chntwms.model.ReturnMsgModelList;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.model.WMS.Inventory.Barcode_Model;
import chinetek.xx.chntwms.model.WMS.Inventory.CheckArea_Model;
import chinetek.xx.chntwms.model.WMS.Inventory.Check_Model;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.ArithUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

@ContentView(R.layout.activity_intentory_scan)
public class IntentoryScan extends BaseActivity {

    String TAG_GetAreanobyCheckno="IntentoryScan_GetAreanobyCheckno";
    String TAG_GetAreanobyCheckno2="IntentoryScan_GetAreanobyCheckno2";
    String TAG_GetScanInfo="IntentoryScan_GetScanInfo";
    String TAG_InsertCheckDetail="IntentoryScan_InsertCheckDetail";

    private final int RESULT_Msg_GetAreanobyCheckno=101;
    private final int RESULT_Msg_GetScanInfo=102;
    private final int RESULT_Msg_InsertCheckDetail=103;
    private final int RESULT_GetAreanobyCheckno2=104;

    String TAG_SaveCheckChai="IntentoryScan_SaveCheckChai";
    private final int RESULT_Msg_SaveCheckChai=105;

//    String[] QCStatus={"待检","检验合格","检验不合格"};
    String[] QCStatus={"待检","检验合格"};
    int[] QCStatusType={1,3};

    Integer whichflag=1;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetAreanobyCheckno:
                AnalysisGetAreanobyChecknoJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveCheckChai:
                AnalysisSaveCheckChaiJson((String) msg.obj);
                break;
            case RESULT_Msg_GetScanInfo:
                AnalysisGetScanInfoJson((String) msg.obj);
                break;
            case RESULT_Msg_InsertCheckDetail:
                AnalysisInsertCheckDetailJson((String) msg.obj);
                break;
            case RESULT_GetAreanobyCheckno2:
                AnalysisGetAreanobyCheckno2Json((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }


  Context context=IntentoryScan.this;
    @ViewInject(R.id.lsv_IntentoryScan)
    ListView lsvIntentoryScan;
    @ViewInject(R.id.txt_VourcherNo)
    TextView txtVourcherNo;
    @ViewInject(R.id.txt_StockContain)
    TextView txtStockContain;
    @ViewInject(R.id.txt_StockNum)
    TextView txtStockNum;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_QCStatus)
    TextView txtQCStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.edt_InvScanBarcode)
    EditText edtInvScanBarcode;
    @ViewInject(R.id.edt_StockScan)
    EditText edtStockScan;
    @ViewInject(R.id.edt_InvNum)
    EditText edtInvNum;
    @ViewInject(R.id.btn_PalletDetail)
    Button btnpalletDetail;
    @ViewInject(R.id.btn_PalletConfig)
    Button btnPalletConfig;
    @ViewInject(R.id.btn_can)
    Button btncan;



    Check_Model checkModel;
    List<CheckArea_Model> checkAreaModels; //可以盘点货位
    CheckArea_Model checkAreaModel;//盘点货位
    ArrayList<Barcode_Model> barcodeModels;
    InventoryScanItemAdapter inventoryScanItemAdapter;
    int StatusType=-1;
    int model=-1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Intentory_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        btncan.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        txtQCStatus.setText(QCStatus[1]);
        StatusType=1;
        checkModel=getIntent().getParcelableExtra("check_model");
        model=getIntent().getIntExtra("model",0);
        GetAreanobyCheckno(checkModel);

    }


    @Event(value =R.id.edt_StockScan,type = View.OnKeyListener.class)
    private  boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String areaNo=edtStockScan.getText().toString().trim();
            if (!TextUtils.isEmpty(areaNo)) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("checkno", checkModel.getCHECKNO());
                params.put("areano", areaNo);
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(IntentoryScan.class, TAG_GetAreanobyCheckno2, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreanobyCheckno2, getString(R.string.Msg_GetAreanobyCheckno2), context, mHandler, RESULT_GetAreanobyCheckno2, null, URLModel.GetURL().GetAreanobyCheckno2, params, null);
            }

        }
        return false;
    }

    @Event(value = R.id.txt_QCStatus,type =View.OnClickListener.class )
    private void txtQCStatusClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择质检状态");
        builder.setItems(QCStatus, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                whichflag=which;
                txtQCStatus.setText(QCStatus[which]);
                StatusType=which;
            }
        });
        builder.show();

    }

    @Event(value =R.id.edt_InvScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtInvScanBarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            if(checkAreaModel==null){
//                MessageBox.Show(context,getString(R.string.Error_StockInCorrect));
                MessageBox.Show(context,getString(R.string.Error_StockInCorrect),1);
                return true;
            }
            String barcode = edtInvScanBarcode.getText().toString().trim();
            String checkno=txtVourcherNo.getText().toString().trim();
            if (!barcode.equals("")) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                params.put("strOldBarCode", barcode);
                params.put("checkno", checkno);
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(IntentoryScan.class, TAG_GetScanInfo, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetScanInfo, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetScanInfo, null, URLModel.GetURL().GetScanInfoChai, params, null);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveCheckChai, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_SaveCheckChai, null, URLModel.GetURL().SaveCheckChai, params, null);
            }


//            if (!barcode.equals("")) {
//                final Map<String, String> params = new HashMap<String, String>();
//                params.put("barcode", barcode);
//                String para = (new JSONObject(params)).toString();
//                LogUtil.WriteLog(IntentoryScan.class, TAG_GetScanInfo, para);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetScanInfo, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetScanInfo, null, URLModel.GetURL().GetScanInfo, params, null);
//            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            edtInvScanBarcode.setText("");
            CommonUtil.setEditFocus(edtStockScan);
            return true;
        }
        return false;
    }

    @Event(value =R.id.edt_InvNum,type = View.OnKeyListener.class)
    private  boolean edtInvNumClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String qty = edtInvNum.getText().toString().trim();
            if(!CommonUtil.isFloat(qty)){
//                MessageBox.Show(context,getString(R.string.Error_isnotnum));
                MessageBox.Show(context,getString(R.string.Error_isnotnum),1);
                CommonUtil.setEditFocus(edtInvNum);
                return true;
            }
            if(TextUtils.isEmpty(edtStockScan.getText())||TextUtils.isEmpty(edtInvScanBarcode.getText()))
            {
//                MessageBox.Show(context,getString(R.string.Error_isnotnull));
                MessageBox.Show(context,getString(R.string.Error_isnotnull),1);
                CommonUtil.setEditFocus(edtInvScanBarcode);
                return true;
            }
            if(StatusType!=-1) {
                barcodeModels.get(0).setSTATUS(QCStatusType[whichflag]);
                barcodeModels.get(0).setQty(Float.parseFloat(qty));
                barcodeModels.get(0).setCreater(BaseApplication.userInfo.getUserName());
                SumbitStockInfo();
            }
            else{
//                MessageBox.Show(context,getString(R.string.Error_SelectQcStatus));
                MessageBox.Show(context,getString(R.string.Error_SelectQcStatus),1);
                CommonUtil.setEditFocus(edtInvNum);
            }

        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
                edtInvNum.setText("");
                CommonUtil.setEditFocus(edtInvScanBarcode);
            return true;
        }
        return false;
    }


    void GetAreanobyCheckno(Check_Model checkModel){
        if(checkModel!=null) {
            txtVourcherNo.setText(checkModel.getCHECKNO());
            final Map<String, String> params = new HashMap<String, String>();
            params.put("checkno", checkModel.getCHECKNO());
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(IntentoryScan.class, TAG_GetAreanobyCheckno, para);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreanobyCheckno, getString(R.string.Msg_GetAreanobyCheckno), context, mHandler, RESULT_Msg_GetAreanobyCheckno, null,  URLModel.GetURL().GetAreanobyCheckno, params, null);
        }
    }

    private List<String> materialnos = new ArrayList<>();

    void AnalysisGetAreanobyChecknoJson(String result){
        LogUtil.WriteLog(IntentoryScan.class, TAG_GetAreanobyCheckno,result);
        ReturnMsgModelList<CheckArea_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<CheckArea_Model>>() {}.getType());
        if(!returnMsgModel.getHeaderStatus().equals("S")){
            MessageBox.Show(context,returnMsgModel.getMessage());
           // checkAreaModels=returnMsgModel.getModelJson();
        }else{
            if(!returnMsgModel.getMessage().equals("")){
                materialnos= Arrays.asList(returnMsgModel.getMessage().split(","));
                btncan.setVisibility(View.VISIBLE);
            }
        }
    }

    //盘点拆零
    void AnalysisSaveCheckChaiJson(String result){
        LogUtil.WriteLog(IntentoryScan.class, TAG_SaveCheckChai,result);
        ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            String newserialno=returnMsgModel.getModelJson();
            if (!newserialno.equals("")) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", newserialno);
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(IntentoryScan.class, TAG_GetScanInfo, para);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetScanInfo, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetScanInfo, null, URLModel.GetURL().GetScanInfo, params, null);
            }
        }else{
            MessageBox.Show(context,"获取条码信息出错！");
        }
    }


    void AnalysisGetScanInfoJson(String result){
        LogUtil.WriteLog(IntentoryScan.class, TAG_GetScanInfo,result);
        try{
            ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {}.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                barcodeModels=returnMsgModel.getModelJson();
                if(barcodeModels!=null && barcodeModels.size()!=0){
                    if(materialnos.size()!=0){
                        if(materialnos.indexOf(barcodeModels.get(0).getMaterialNo())==-1){
                            MessageBox.Show(context,"扫描的条码不在选定的物料列表中！");
                            CommonUtil.setEditFocus(edtInvScanBarcode);
                            return;
                        }
                    }
                    StatusType = barcodeModels.get(0).getSTATUS();
//                    Integer which=1;
                    if(StatusType!=1&&StatusType!=3)
                    {
                        whichflag=1;
                    }
                    if(StatusType==1)
                    {
                        whichflag=0;
                    }
                    if(StatusType==3)
                    {
                        whichflag=1;
                    }
                    txtQCStatus.setText(QCStatus[whichflag]);
                    txtCompany.setText(barcodeModels.get(0).getDimension());
                    txtBatch.setText(barcodeModels.get(0).getBatchNo());
                    txtStatus.setText("");
                    txtMaterialName.setText(barcodeModels.get(0).getMaterialDesc());
                    txtStockNum.setText(barcodeModels.size()+"");
                    Float packageNum=0f;
                    for (int i=0;i<barcodeModels.size();i++) {
                        barcodeModels.get(i).setCHECKNO(checkModel.getCHECKNO());//盘点单号
                        barcodeModels.get(i).setAREAID(checkAreaModel.getID());//盘点库位
                        packageNum= ArithUtil.add(packageNum, barcodeModels.get(i).getQty());
                    }
                    edtInvNum.setText(packageNum+"");

                    inventoryScanItemAdapter=new InventoryScanItemAdapter(context,model,barcodeModels);
                    lsvIntentoryScan.setAdapter(inventoryScanItemAdapter);
                    if(barcodeModels.size()>1){
                        btnPalletConfig.setVisibility(View.VISIBLE);
                    }else{
                        CommonUtil.setEditFocus(edtInvNum);
                    }

                }
            }else{
//                MessageBox.Show(context,returnMsgModel.getMessage());
                MessageBox.Show(context,returnMsgModel.getMessage(),1);
                CommonUtil.setEditFocus(edtInvScanBarcode);
            }
        }catch(Exception ex){
            MessageBox.Show(context,ex.toString());
            MessageBox.Show(context,ex.toString(),1);
        }

    }

    void AnalysisGetAreanobyCheckno2Json(String result){
        LogUtil.WriteLog(IntentoryScan.class, TAG_GetAreanobyCheckno2,result);
        ReturnMsgModelList<CheckArea_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<CheckArea_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            checkAreaModel=returnMsgModel.getModelJson().get(0);
        }else{
            edtStockScan.setText("");
            CommonUtil.setEditFocus(edtStockScan);
//            edtStockScan.selectAll();
            MessageBox.Show(context,returnMsgModel.getMessage());

        }
    }

    void  AnalysisInsertCheckDetailJson(String result){
        try {
            LogUtil.WriteLog(IntentoryScan.class, TAG_InsertCheckDetail,result);
            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                edtInvScanBarcode.setText("");
                edtInvNum.setText("");
                txtCompany.setText("");
                txtBatch.setText("");
                txtStatus.setText("");
                txtMaterialName.setText("");
                txtStockNum.setText("");
                txtQCStatus.setText(QCStatus[1]);
                StatusType=1;
                btnPalletConfig.setVisibility(View.GONE);
                barcodeModels=new ArrayList<>();
                inventoryScanItemAdapter=new InventoryScanItemAdapter(context,model,barcodeModels);
                lsvIntentoryScan.setAdapter(inventoryScanItemAdapter);
                CommonUtil.setEditFocus(edtInvScanBarcode);
            }else{
//                MessageBox.Show(context, returnMsgModel.getMessage());
                MessageBox.Show(context, returnMsgModel.getMessage(),1);
            }

        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
            MessageBox.Show(context, ex.getMessage(),1);
        }
    }

    @Event(R.id.btn_PalletConfig)
    private void btnPalletConfigClick(View view){
        if(barcodeModels!=null)
            SumbitStockInfo();
    }

    @Event(R.id.btn_PalletDetail)
    private void btnPalletDetailClick(View view){
        Intent intent=new Intent(context,IntentoryDetial.class);
        intent.putExtra("checkno", txtVourcherNo.getText().toString().trim());
        intent.putExtra("model", model);
        startActivityLeft(intent);
    }

    @Event(R.id.btn_can)
    private void btncanClick(View view){
        Intent intent=new Intent(context,IntentoryCan.class);
        intent.putExtra("checkno", txtVourcherNo.getText().toString().trim());
        startActivityLeft(intent);
    }


    void SumbitStockInfo(){
        final Map<String, String> params = new HashMap<String, String>();
        String ModelJson= GsonUtil.parseModelToJson(barcodeModels);
        params.put("json", ModelJson);
        LogUtil.WriteLog(IntentoryScan.class, TAG_InsertCheckDetail, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InsertCheckDetail, getString(R.string.Msg_InsertCheckDetail), context, mHandler, RESULT_Msg_InsertCheckDetail, null, URLModel.GetURL().InsertCheckDetail, params, null);

    }
}
