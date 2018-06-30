package chinetek.xx.chntwms.Box;

import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Base_Model;
import chinetek.xx.chntwms.model.CheckNumRefMaterial;
import chinetek.xx.chntwms.model.ReturnMsgModel;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.model.WMS.Inventory.Barcode_Model;
import chinetek.xx.chntwms.model.WMS.Stock.StockInfo_Model;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;
import chinetek.xx.chntwms.util.function.DoubleClickCheck;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

@ContentView(R.layout.activity_boxing)
public class Boxing extends BaseActivity {

    String TAG_GetT_OutBarCodeInfoByBoxADF="Boxing_GetT_OutBarCodeInfoByBoxADF";
    String TAG_SaveT_BarCodeToStockADF="Boxing_SaveT_BarCodeToStockADF";

    private final int RESULT_GetT_OutBarCodeInfoByBoxADF = 101;
    private final int RESULT_SaveT_BarCodeToStockADF = 102;

    @Override
    public void onHandleMessage(Message msg) {

        switch (msg.what) {
            case RESULT_GetT_OutBarCodeInfoByBoxADF:
                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
                break;
        }
    }

    Context context=Boxing.this;

    @ViewInject(R.id.SW_Box)
    Switch SWBox;
    @ViewInject(R.id.edt_BoxCode)
    EditText edtBoxCode;
    @ViewInject(R.id.edt_UnboxCode)
    EditText edtUnboxCode;
//    @ViewInject(R.id.edt_BoxNum)
//    EditText edtBoxNum;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txt_EDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.txt_boxQty)
    TextView txtBoxQty;
    @ViewInject(R.id.txt_unCompany)
    TextView txtunCompany;
    @ViewInject(R.id.txt_unBatch)
    TextView txtunBatch;
    @ViewInject(R.id.txt_unStatus)
    TextView txtunStatus;
    @ViewInject(R.id.txt_UnEDate)
    TextView txt_UnEDate;
    @ViewInject(R.id.txt_unMaterialName)
    TextView txtunMaterialName;
    @ViewInject(R.id.txt_unboxQty)
    TextView txtunBoxQty;
    @ViewInject(R.id.txt_box)
    TextView txtbox;
//    @ViewInject(R.id.txt_boxNum)
//    TextView txtboxNum;
    @ViewInject(R.id.btn_BoxConfig)
    TextView btnBoxConfig;
    @ViewInject(R.id.conLay_unboxInfo)
    ConstraintLayout conLayunboxInfo;
    @ViewInject(R.id.conLay_boxInfo)
    ConstraintLayout conLayboxInfo;

    boolean isUnbox=false;//判断扫描箱子类型 true:拆箱扫描
    StockInfo_Model unStockInfoModel=new StockInfo_Model();
    StockInfo_Model stockInfoModel=new StockInfo_Model();

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.Boxing_title), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        ShowBoxScan(SWBox.isChecked());
    }

    @Event(value = R.id.SW_Box,type = CompoundButton.OnCheckedChangeListener.class)
    private void SwPalletonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ShowBoxScan(isChecked);
    }

    @Event(value ={R.id.edt_UnboxCode,R.id.edt_BoxCode} ,type = View.OnKeyListener.class)
    private  boolean edtboxCodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            isUnbox=v.getId()==R.id.edt_UnboxCode;
            String barcode=v.getId()==R.id.edt_UnboxCode?
                    edtUnboxCode.getText().toString().trim():edtBoxCode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", barcode);
                LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF, barcode);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoByBoxADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_OutBarCodeInfoByBoxADF, null,  URLModel.GetURL().GetT_GetT_OutBarCodeInfoByBoxADF, params, null);
            return false;
        }
        return false;
    }

    @Event(R.id.btn_BoxConfig)
    private void BtnBoxConfigClick(View v){
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
        if(SWBox.isChecked()) {
//            String num = edtBoxNum.getText().toString().trim();
//            String returnMsg = CheckInputQty(num);
//            if (!returnMsg.equals("")) {
//                MessageBox.Show(context, returnMsg);
//                CommonUtil.setEditFocus(edtBoxNum);
//                return;
//            }
//            Float qty = Float.parseFloat(num);
//            unStockInfoModel.setAmountQty(qty);
        }
        if(!SWBox.isChecked()&& !(unbarcodeModel.get(0).getMaterialNo().equals(barcodeModel.getMaterialNo()) )){
//            MessageBox.Show(context, getString(R.string.Error_MaterialNotMatch));
            MessageBox.Show(context, getString(R.string.Error_MaterialNotMatch),1);
            CommonUtil.setEditFocus(edtBoxCode);
            return;
        }

        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String strOldBarCode = GsonUtil.parseModelToJson(unbarcodeModel);
        String strNewBarCode =SWBox.isChecked()?"": GsonUtil.parseModelToJson(barcodeModel);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
//        params.put(!SWBox.isChecked()?"strNewBarCode":"strOldBarCode", strOldBarCode);
//        params.put(!SWBox.isChecked()?"strOldBarCode":"strNewBarCode", strNewBarCode);
        params.put("strOldBarCode", strOldBarCode);
        params.put("strNewBarCode", strNewBarCode);
        params.put("PrintFlag","2"); //1：打印 2：不打印
        LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode+"||"+strNewBarCode);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);



//        if (DoubleClickCheck.isFastDoubleClick(context)) {
//            return;
//        }
//        if(SWBox.isChecked()) {
//            String num = edtBoxNum.getText().toString().trim();
//            String returnMsg = CheckInputQty(num);
//            if (!returnMsg.equals("")) {
//                MessageBox.Show(context, returnMsg);
//                CommonUtil.setEditFocus(edtBoxNum);
//                return;
//            }
//            Float qty = Float.parseFloat(num);
//            unStockInfoModel.setAmountQty(qty);
//        }
//        if(!SWBox.isChecked()&& !(unStockInfoModel.getMaterialNo().equals(stockInfoModel.getMaterialNo()) &&
//            unStockInfoModel.getStrongHoldCode().equals(stockInfoModel.getStrongHoldCode()))){
//            MessageBox.Show(context, getString(R.string.Error_MaterialNotMatch));
//            CommonUtil.setEditFocus(edtBoxCode);
//            return;
//        }
//
//        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
//        String strOldBarCode = GsonUtil.parseModelToJson(unStockInfoModel);
//        String strNewBarCode =SWBox.isChecked()?"": GsonUtil.parseModelToJson(stockInfoModel);
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("UserJson", userJson);
//        params.put(!SWBox.isChecked()?"strNewBarCode":"strOldBarCode", strOldBarCode);
//        params.put(!SWBox.isChecked()?"strOldBarCode":"strNewBarCode", strNewBarCode);
//        params.put("PrintFlag","1"); //1：打印 2：不打印
//        LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode+"||"+strNewBarCode);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
    }



    List<StockInfo_Model> unbarcodeModel = new ArrayList<StockInfo_Model>();//拆箱
    StockInfo_Model barcodeModel=new StockInfo_Model();//装箱
    /*解析物料条码扫描*/
    void AnalysisGetT_SerialNoByPalletAD(String result){
        LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF,result);
        ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {}.getType());
        if(returnMsgModel.getHeaderStatus().equals("S")){
            try {
                StockInfo_Model BModel = returnMsgModel.getModelJson();
                if (!isUnbox) {
                    if(unbarcodeModel.size()!=0&&unbarcodeModel.get(0).getWareHouseID()!=BModel.getWareHouseID()){
//                        MessageBox.Show(context,"装箱条码和拆箱条码不在同一个仓库！");
                        MessageBox.Show(context,"装箱条码和拆箱条码不在同一个仓库！",1);
                        return;
                    }

                    if(barcodeModel.getSerialNo()!=null){
//                        MessageBox.Show(context,"装箱条码不能为多个！");
                        MessageBox.Show(context,"装箱条码不能为多个！",1);
                        return;
                    }
                    if(unbarcodeModel.size()!=0&&unbarcodeModel.contains(BModel)){
//                        MessageBox.Show(context,"该条码已经存在于拆箱条码中！");
                        MessageBox.Show(context,"该条码已经存在于拆箱条码中！",1);
                        return;
                    }
                    if(BModel.getBarcodeType()==0){
//                        MessageBox.Show(context,"装箱条码不能为内盒！");
                        MessageBox.Show(context,"装箱条码不能为内盒！",1);
                        return;
                    }
                    //装箱条码不能为拆箱中的父级条码
                    if (unbarcodeModel.size()!=0&&unbarcodeModel.get(0).getBarcodeType()==0&&unbarcodeModel.get(0).getFserialno().equals(BModel.getSerialNo())){
//                        MessageBox.Show(context,"装箱条码为拆箱条码的父级条码");
                        MessageBox.Show(context,"装箱条码为拆箱条码的父级条码",1);
                        return;
                    }

                    //判断装箱拆箱条码是否同一个批次，物料号，和仓库
                    if(unbarcodeModel.size()!=0){
                        if(!unbarcodeModel.get(0).getBatchNo().equals(BModel.getBatchNo())){
//                            MessageBox.Show(context,"扫描条码批次号不一致");
                            MessageBox.Show(context,"扫描条码批次号不一致",1);
                            return;
                        }
                        if(!unbarcodeModel.get(0).getMaterialNo().equals(BModel.getMaterialNo())){
//                            MessageBox.Show(context,"扫描条码物料号不一致");
                            MessageBox.Show(context,"扫描条码物料号不一致",1);
                            return;
                        }

                    }



                    //装箱条码
                    barcodeModel = BModel;
                    txtBatch.setText(BModel.getBatchNo());
                    txtBoxQty.setText(BModel.getQty() + "");
                    txtMaterialName.setText(BModel.getMaterialDesc());
                    txtCompany.setText(BModel.getSpec());


                } else {
                    //拆箱模式
                    if(Unflag==true&&(BModel.getFserialno()==null||BModel.getFserialno()=="")){
//                        MessageBox.Show(context,"拆箱状态不能扫描外箱标签！");
                        MessageBox.Show(context,"拆箱状态不能扫描外箱标签！",1);
                        return;
                    }

                    //装箱条码不能为拆箱中的父级条码
                    if(barcodeModel.getSerialNo()!=null&&BModel.getWareHouseID()!=barcodeModel.getWareHouseID()){
//                        MessageBox.Show(context,"装箱条码和拆箱条码不在同一个仓库");
                        MessageBox.Show(context,"装箱条码和拆箱条码不在同一个仓库",1);
                        return;
                    }

                    //装箱条码不能为拆箱中的父级条码
                    if(BModel.getBarcodeType()==0&&barcodeModel.getSerialNo()!=null&&barcodeModel.getSerialNo().equals(BModel.getFserialno())){
//                        MessageBox.Show(context,"装箱条码为拆箱条码的父级条码");
                        MessageBox.Show(context,"装箱条码为拆箱条码的父级条码",1);
                        return;
                    }

                    //判断装箱拆箱条码是否同一个批次，物料号，和仓库
                    if(barcodeModel.getSerialNo()!=null&&barcodeModel.getMaterialNo().equals(BModel.getMaterialNo())){
//                        MessageBox.Show(context,"扫描条码物料号不一致");
                        MessageBox.Show(context,"扫描条码物料号不一致",1);

                        return;
                    }
                    if(barcodeModel.getSerialNo()!=null&&barcodeModel.getBatchNo().equals(BModel.getBatchNo())){
//                        MessageBox.Show(context,"扫描条码批次号不一致");
                        MessageBox.Show(context,"扫描条码批次号不一致",1);

                        return;
                    }

                    //拆箱条码
                    if(unbarcodeModel.size()==0){
                        //判断扫描的条码不存在于装箱条码
                        if((barcodeModel.getSerialNo()!=null)&&barcodeModel.getSerialNo().equals(BModel.getSerialNo())){
//                            MessageBox.Show(context,"该条码已经存在于装箱条码中！");
                            MessageBox.Show(context,"该条码已经存在于装箱条码中！",1);
                            return;
                        }
                        unbarcodeModel.add(BModel);
                    }else{
                        //判断条码是内盒还是外箱条码
                        if(BModel.getBarcodeType()==0){
                            //内盒
                            if(unbarcodeModel.get(0).getBarcodeType()==1){
//                                MessageBox.Show(context,"不能混合扫描外箱和内盒条码！");
                                MessageBox.Show(context,"不能混合扫描外箱和内盒条码！",1);

                                return;
                            }
                            if(!unbarcodeModel.get(0).getFserialno().equals(BModel.getFserialno())){
//                                MessageBox.Show(context,"扫描外盒必须是同一个外箱！");
                                MessageBox.Show(context,"扫描外盒必须是同一个外箱！",1);

                                return;
                            }
                            //判断扫描的条码是否重复
                            if(unbarcodeModel.contains(BModel)){
//                                MessageBox.Show(context,"该条码已经被扫描！");
                                MessageBox.Show(context,"该条码已经被扫描！",1);

                                return;
                            }

                            unbarcodeModel.add(BModel);

                        }else{
                            //外箱
//                            MessageBox.Show(context,"只能允许扫描单个外箱装箱！");
                            MessageBox.Show(context,"只能允许扫描单个外箱装箱！",1);

                            return;
                        }
                    }

//                    this.stockInfoModel = stockInfoModel;
                    txtunBatch.setText(BModel.getBatchNo());

                    float Allnum=0;
                    for(int j=0;j<unbarcodeModel.size();j++){
                        Allnum=Allnum+unbarcodeModel.get(j).getQty();
                    }
                    txtunBoxQty.setText(Allnum+"");
                    txtunMaterialName.setText(BModel.getMaterialDesc());
                    txtunCompany.setText(BModel.getSpec());
//                    txtCompany.setText(stockInfoModel.getStrongHoldName());
//                    txtStatus.setText(stockInfoModel.getStrStatus());
//                    txt_EDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
                }
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
            }
        }else
        {
            MessageBox.Show(context,returnMsgModel.getMessage());
            CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
        }


//        LogUtil.WriteLog(Boxing.class, TAG_GetT_OutBarCodeInfoByBoxADF,result);
//        ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {}.getType());
//        if(returnMsgModel.getHeaderStatus().equals("S")){
//            try {
//                StockInfo_Model stockInfoModel = returnMsgModel.getModelJson();
//                if (isUnbox) {
//                    this.unStockInfoModel = stockInfoModel;
//                    txtunBatch.setText(stockInfoModel.getBatchNo());
//                    txtunBoxQty.setText(stockInfoModel.getQty() + "");
//                    txtunMaterialName.setText(stockInfoModel.getMaterialDesc());
//                    txtunCompany.setText(stockInfoModel.getStrongHoldName());
//                    txtunStatus.setText(stockInfoModel.getStrStatus());
//                    txt_UnEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
//                    if (SWBox.isChecked()) {
//                        this.stockInfoModel = new StockInfo_Model();
//                        this.stockInfoModel.setQty(0f);
//                    }
//                } else {
//                    this.stockInfoModel = stockInfoModel;
//                    txtBatch.setText(stockInfoModel.getBatchNo());
//                    txtBoxQty.setText(stockInfoModel.getQty() + "");
//                    txtMaterialName.setText(stockInfoModel.getMaterialDesc());
//                    txtCompany.setText(stockInfoModel.getStrongHoldName());
//                    txtStatus.setText(stockInfoModel.getStrStatus());
//                    txt_EDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
//                }
//            }catch (Exception ex){
//                MessageBox.Show(context,returnMsgModel.getMessage());
//                CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
//            }
//        }else
//        {
//            MessageBox.Show(context,returnMsgModel.getMessage());
//            CommonUtil.setEditFocus(isUnbox?edtUnboxCode:edtBoxCode);
//        }
    }

    /*
    装箱拆箱提交
     */
   void AnalysisSaveT_BarCodeToStockADF(String result){
       try {
           LogUtil.WriteLog(Boxing.class, TAG_SaveT_BarCodeToStockADF, result);
           ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
           }.getType());
           MessageBox.Show(context, returnMsgModel.getMessage());
           if(returnMsgModel.getHeaderStatus().equals("S")){
               clearFrm();
           }
       } catch (Exception ex) {
           MessageBox.Show(context, ex.getMessage());
       }
       CommonUtil.setEditFocus(edtUnboxCode);
   }


    boolean Unflag=false;
    /*
    显示隐藏物料信息
     */
    void ShowBoxScan(boolean check){
        edtBoxCode.setText("");
        edtUnboxCode.setText("");
//        edtBoxNum.setText("");
        if(!check){
            Unflag=false;
            conLayboxInfo.setVisibility(View.VISIBLE);
            edtBoxCode.setVisibility(View.VISIBLE);
            txtbox.setVisibility(View.VISIBLE);
//            txtboxNum.setVisibility(View.GONE);
//            edtBoxNum.setVisibility(View.GONE);
        }else{
            Unflag=true;
            conLayboxInfo.setVisibility(View.GONE);
            edtBoxCode.setVisibility(View.GONE);
            txtbox.setVisibility(View.GONE);
//            txtboxNum.setVisibility(View.VISIBLE);
//            edtBoxNum.setVisibility(View.VISIBLE);
        }
        CommonUtil.setEditFocus(edtUnboxCode);
    }

//    String CheckInputQty(String num){
//
//        CheckNumRefMaterial checkNumRefMaterial=CheckMaterialNumFormat(num,unStockInfoModel.getUnitTypeCode(),unStockInfoModel.getDecimalLngth());
//        if(!checkNumRefMaterial.ischeck()) {
//           return checkNumRefMaterial.getErrMsg();
//        }
//        Float qty=checkNumRefMaterial.getCheckQty();
//        if(qty>unStockInfoModel.getQty()){
//            return getString(R.string.Error_QtyBiger);
//        }
////        if(qty>Integer.parseInt(txtunBoxQty.getText().toString().split("/")[1])-Integer.parseInt(txtunBoxQty.getText().toString().split("/")[0])){
////            return getString(R.string.Error_PackageQtyBiger);
////        }
//        return "";
//    }


    void clearFrm(){
        unbarcodeModel = new ArrayList<StockInfo_Model>();//拆箱
        barcodeModel=new StockInfo_Model();//装箱


        this.unStockInfoModel=new StockInfo_Model();
        this.stockInfoModel=new StockInfo_Model();
        edtBoxCode.setText("");
        edtUnboxCode.setText("");
//        edtBoxNum.setText("");
        txtunBatch.setText("");
        txtunBoxQty.setText("");
        txtunMaterialName.setText("");
        txtunCompany.setText("");
        txtunStatus.setText("");
        txt_UnEDate.setText("");
        txtBatch.setText("");
        txtBoxQty.setText("");
        txtMaterialName.setText("");
        txtCompany.setText("");
        txtStatus.setText("");
        txt_EDate.setText("");
        CommonUtil.setEditFocus(edtUnboxCode);
    }
}
