package chinetek.xx.chntwms.cywms.OffShelf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinetek.xx.chntwms.adapter.wms.OffShelf.OffShelfScanDetailAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.Query.Query;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Base_Model;
import chinetek.xx.chntwms.model.CheckNumRefMaterial;
import chinetek.xx.chntwms.model.ReturnMsgModel;
import chinetek.xx.chntwms.model.ReturnMsgModelList;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.model.WMS.OffShelf.OutStockTaskDetailsInfo_Model;
import chinetek.xx.chntwms.model.WMS.OffShelf.OutStockTaskInfo_Model;
import chinetek.xx.chntwms.model.WMS.Stock.StockInfo_Model;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.PlayVideo.PlayVoice;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.ArithUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;
import chinetek.xx.chntwms.util.function.DoubleClickCheck;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

import static chinetek.xx.chntwms.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_offshelf_scan)
public class OffshelfScan extends BaseActivity {

    String TAG_GetT_OutTaskDetailListByHeaderIDADF="OffshelfScan_GetT_OutTaskDetailListByHeaderIDADF";
    String TAG_GetStockModelADF="OffshelfScan_GetStockModelADF";
    String TAG_SaveT_OutStockTaskDetailADF="OffshelfScan_SaveT_OutStockTaskDetailADF";
    String TAG_SaveT_BarCodeToStockADF="OffshelfScan_SaveT_BarCodeToStockADF";

    private final int RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF=101;
   private final int RESULT_Msg_GetStockModelADF=102;
    private final int RESULT_Msg_SaveT_OutStockTaskDetailADF=103;
    private final int RESULT_SaveT_BarCodeToStockADF = 104;


//    String TAG_GetT_OutBarCodeInfoByBoxADF="Boxing_GetT_OutBarCodeInfoByBoxADF";
//    private final int RESULT_GetT_OutBarCodeInfoByBoxADF = 105;


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF:
                AnalysisGetT_OutTaskDetailListByHeaderIDADFJson((String) msg.obj);
                break;
            case RESULT_Msg_GetStockModelADF:
                AnalysisGetStockModelADFJson((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_OutStockTaskDetailADF:
                AnalysisSaveT_OutStockTaskDetailADFJson((String) msg.obj);
                break;
            case RESULT_SaveT_BarCodeToStockADF:
                AnalysisSaveT_BarCodeToStockADF((String) msg.obj);
                break;
//            case RESULT_GetT_OutBarCodeInfoByBoxADF:
//                AnalysisGetT_SerialNoByPalletAD((String) msg.obj);
//                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                break;
        }
    }

   Context context=OffshelfScan.this;
    @ViewInject(R.id.txt_Company)
    TextView txtCompany;
    @ViewInject(R.id.txt_Batch)
    TextView txtBatch;
    @ViewInject(R.id.txt_Status)
    TextView txtStatus;
    @ViewInject(R.id.txt_EDate)
    TextView txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView txtMaterialName;
    @ViewInject(R.id.tb_UnboxType)
    ToggleButton tbUnboxType;
    @ViewInject(R.id.tb_PalletType)
    ToggleButton tbPalletType;
    @ViewInject(R.id.tb_BoxType)
    ToggleButton tbBoxType;
    @ViewInject(R.id.edt_OffShelfScanbarcode)
    EditText edtOffShelfScanbarcode;
    @ViewInject(R.id.edt_Unboxing)
    EditText edtUnboxing;
    @ViewInject(R.id.txt_SugestStock)
    TextView txtSugestStock;
    @ViewInject(R.id.txt_OffshelfNum)
    TextView txtOffshelfNum;
    @ViewInject(R.id.txt_currentPickNum)
    TextView txtcurrentPickNum;
    @ViewInject(R.id.txt_Unboxing)
    TextView txtUnboxing;
    @ViewInject(R.id.btn_OutOfStock)
    TextView btnOutOfStock;
    @ViewInject(R.id.btn_BillDetail)
    TextView btnBillDetail;
    @ViewInject(R.id.btn_PrintBox)
    TextView btnPrintBox;
    @ViewInject(R.id.lsv_PickList)
    ListView lsvPickList;
    @ViewInject(R.id.btn_InOfStock)
    TextView btnInOfStock;
    @ViewInject(R.id.innerNum)
    TextView innerNum;



    ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels;
    ArrayList<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfoModels;
    ArrayList<OutStockTaskDetailsInfo_Model> SameLineoutStockTaskDetailsInfoModels; //相同行物料集合


    List<StockInfo_Model> stockInfoModels;//扫描条码
    OffShelfScanDetailAdapter offShelfScanDetailAdapter;
    Float SumReaminQty=0f; //当前拣货物料剩余拣货数量合计
    int currentPickMaterialIndex=-1;
    String IsEdate="";

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.OffShelf_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        outStockTaskInfoModels=getIntent().getParcelableArrayListExtra("outStockTaskInfoModel");
        GetT_OutTaskDetailListByHeaderIDADF(outStockTaskInfoModels);
    }

    @Event(value ={R.id.tb_UnboxType,R.id.tb_PalletType,R.id.tb_BoxType} ,type = CompoundButton.OnClickListener.class)
    private void TBonCheckedChanged(View view) {
        tbUnboxType.setChecked(view.getId()== R.id.tb_UnboxType);
        tbPalletType.setChecked(view.getId()== R.id.tb_PalletType);
        tbBoxType.setChecked(view.getId()== R.id.tb_BoxType);
        ShowUnboxing(view.getId()== R.id.tb_UnboxType);
    }

    @Event(value =R.id.lsv_PickList,type =AdapterView.OnItemClickListener.class )
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel=(OutStockTaskDetailsInfo_Model)offShelfScanDetailAdapter.getItem(position);
        Intent intent = new Intent(context, Query.class);
        intent.putExtra("Type",1);
        intent.putExtra("MaterialNO",outStockTaskDetailsInfoModel.getMaterialNo());
        startActivityLeft(intent);
    }


    @Event(value ={R.id.edt_Unboxing,} ,type = View.OnKeyListener.class)
    private  boolean edtboxCodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
//            if(barcodeModel==null||barcodeModel.getSerialNo()==null||barcodeModel.getSerialNo().equals("")){
//                MessageBox.Show(context,"拆零请先扫描外箱条码！");
//                return false;
//            }
//            String barcode=edtUnboxing.getText().toString().trim();
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("BarCode", barcode);
//            LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutBarCodeInfoByBoxADF, barcode);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutBarCodeInfoByBoxADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetT_OutBarCodeInfoByBoxADF, null,  URLModel.GetURL().GetT_GetT_OutBarCodeInfoByBoxADF, params, null);

            if(stockin.size()!=0&&edtOffShelfScanbarcode.getText().toString().equals("")){
                PlayVoice.PlayError(context);
                CommonUtil.setEditFocus(edtUnboxing);
                MessageBox.Show(context,"请先验证外箱条码！");
                return false;
            }


            String outbarcode=edtOffShelfScanbarcode.getText().toString().trim();
            String barcode=edtUnboxing.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", outbarcode);
            params.put("InnerBarCode", barcode);
            params.put("ScanType", "3");
            params.put("MoveType", "1"); //1：下架 2:移库
            params.put("IsEdate", IsEdate); //1：不判断有效期 2:判断有效期
            LogUtil.WriteLog(OffshelfScan.class, TAG_GetStockModelADF, barcode);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);

            return false;
        }
        return false;
    }


//    List<StockInfo_Model> unbarcodeModel = new ArrayList<StockInfo_Model>();//拆箱
//    StockInfo_Model barcodeModel=new StockInfo_Model();//装箱
//    /*解析物料条码扫描*/
//    void AnalysisGetT_SerialNoByPalletAD(String result){
//        try {
//            LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutBarCodeInfoByBoxADF,result);
//            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {}.getType());
//            if(returnMsgModel.getHeaderStatus().equals("S")){
//                StockInfo_Model BModel = returnMsgModel.getModelJson();
//                if(!BModel.getFserialno().equals(barcodeModel.getSerialNo())){
//                    MessageBox.Show(context,"拆箱条码不在外箱条码中！");
//                    CommonUtil.setEditFocus(edtUnboxing);
//                    return;
//                }else{
//                    unbarcodeModel.add(BModel);
//                    changeinnernum();
//                    CommonUtil.setEditFocus(edtUnboxing);
//                }
//            }else{
//                MessageBox.Show(context,returnMsgModel.getMessage());
//                CommonUtil.setEditFocus(edtUnboxing);
//            }
//        }catch (Exception ex){
//            MessageBox.Show(context,ex.getMessage());
//            CommonUtil.setEditFocus(edtUnboxing);
//        }
//    }

    private void changeinnernum(){
        float num=0;
        if(stockin!=null&&stockin.size()>0){
            for(StockInfo_Model model  : stockin){
                num= ArithUtil.add(num,model.getQty());
            }
        }
        innerNum.setText(num+"");
    }


//    @Event(value =R.id.edt_Unboxing,type = View.OnKeyListener.class)
//    private  boolean edtUnboxingClick(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
////            String num=edtUnboxing.getText().toString().trim();
//            if (stockInfoModels != null && stockInfoModels.size() != 0) {
//                CheckNumRefMaterial checkNumRefMaterial = CheckMaterialNumFormat(num, stockInfoModels.get(0).getUnitTypeCode(), stockInfoModels.get(0).getDecimalLngth());
//                if (!checkNumRefMaterial.ischeck()) {
//                    MessageBox.Show(context, checkNumRefMaterial.getErrMsg());
//                    CommonUtil.setEditFocus(edtUnboxing);
//                    return true;
//                }
//                Float qty = checkNumRefMaterial.getCheckQty(); //输入数量
//                Float scanQty = stockInfoModels.get(0).getQty(); //箱数量
//                if (qty >=scanQty) {
//                    MessageBox.Show(context, getString(R.string.Error_PackageQtyBiger));
//                    CommonUtil.setEditFocus(edtUnboxing);
//                    return true;
//                }
//                if (currentPickMaterialIndex != -1) {
////                    Float remainqty = ArithUtil.sub(outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getRePickQty(),
////                            outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getScanQty());
//                    if (SumReaminQty < qty) {//qty > remainqty ||
//                        MessageBox.Show(context, getString(R.string.Error_offshelfQtyBiger)
//                                +"\n需下架数量："+SumReaminQty
//                                +"\n扫描数量："+qty
//                                +"\n拆零后剩余数量："+ ArithUtil.sub(qty,SumReaminQty));
//                        CommonUtil.setEditFocus(edtUnboxing);
//                        return true;
//                    }
//
//                    //拆零
//                    stockInfoModels.get(0).setPickModel(3);
//                    stockInfoModels.get(0).setAmountQty(qty);
//                    String userJson = parseModelToJson(BaseApplication.userInfo);
//                    String strOldBarCode = parseModelToJson(stockInfoModels.get(0));
//                    final Map<String, String> params = new HashMap<String, String>();
//                    params.put("UserJson", userJson);
//                    params.put("strOldBarCode", strOldBarCode);
//                    params.put("strNewBarCode", "");
//                    params.put("PrintFlag", "1"); //1：打印 2：不打印
//                    LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode);
//                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null, URLModel.GetURL().SaveT_BarCodeToStockADF, params, null);
//                }
//
//            } else {
//                MessageBox.Show(context, getString(R.string.Hit_ScanBarcode));
//                edtUnboxing.setText("");
//                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
//                return true;
//            }
//        }
//        return false;
//    }

    @Event(value =R.id.edt_OffShelfScanbarcode,type = View.OnKeyListener.class)
    private  boolean edtOffShelfScanbarcodeClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtOffShelfScanbarcode.getText().toString().trim();
            int type=tbPalletType.isChecked()?1:(tbBoxType.isChecked()?2:3);

            if(tbUnboxType.isChecked()){
                if(stockin.size()==0||edtUnboxing.getText().toString().equals("")){
                    PlayVoice.PlayError(context);
                    CommonUtil.setEditFocus(edtUnboxing);
                    edtOffShelfScanbarcode.setText("");
                    MessageBox.Show(context,"请先扫描内盒条码！");
                    return false;
                }
            }

//            int type=tbPalletType.isChecked()?1:2;
//            int type=tbPalletType.isChecked()?1:2;
//            扫描内盒检查是否有内容
//            String innercode=edtUnboxing.getText().toString().trim();
//            if(type==3&&code.equals("")){
//                MessageBox.Show(context,"先扫描外箱条码！");
//                return false;
//            }

            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("InnerBarCode", tbUnboxType.isChecked()?edtUnboxing.getText().toString():"");
            params.put("ScanType", type+"");
            params.put("MoveType", "1"); //1：下架 2:移库
            params.put("IsEdate", IsEdate); //1：不判断有效期 2:判断有效期
            LogUtil.WriteLog(OffshelfScan.class, TAG_GetStockModelADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);
        }
        return false;
    }


    @Event(R.id.btn_OutOfStock)
    private void btnOutofStockClick(View view) {
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }
        if (currentPickMaterialIndex!=-1) {
            final String MaterialDesc = outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getMaterialDesc();
            final String MaterialNo = outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getMaterialNo();
            new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否跳过物料：\n" +MaterialNo+"\n"+MaterialDesc + "\n拣货？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).setOutOfstock(true);
                             currentPickMaterialIndex=FindFirstCanPickMaterial();
                            ShowPickMaterialInfo();
                        }
                    }).setNegativeButton("取消", null).show();
        }
    }


    @Event(R.id.btn_InOfStock)
    private void btninofStockClick(View view) {
        if (DoubleClickCheck.isFastDoubleClick(context)) {
            return;
        }

//        if(stockout==null||stockout.getSerialNo()==null||stockin==null||stockin.size()==0)
        String outbarcode=edtOffShelfScanbarcode.getText().toString().trim();
        if(stockin==null||stockin.size()==0||outbarcode=="")
        {
            MessageBox.Show(context,"请先扫描拆零和外箱条码！");
            PlayVoice.PlayError(context);
            return;
        }
        //判断拆零的数量不能大于下架的数量
        float sumnum =0f;
        for (int j=0;j<stockin.size();j++){
            sumnum =sumnum+stockin.get(j).getQty();
        }
        boolean flag=false;
        for (OutStockTaskDetailsInfo_Model taskmodel:outStockTaskDetailsInfoModels){
            if(taskmodel.getMaterialNo().equals(stockin.get(0).getMaterialNo())&&ArithUtil.sub(taskmodel.getRePickQty(),taskmodel.getScanQty())<sumnum ){
                MessageBox.Show(context,"拆零数量超过需要下架的数量！");
                cleanchai();//清空拆零数据
                PlayVoice.PlayError(context);
                return;
            }
            if(taskmodel.getMaterialNo().equals(stockin.get(0).getMaterialNo())){
                flag=true;
            }
        }
        if(flag==false){
            MessageBox.Show(context,"拆零条码物料不在该任务列表中！");
            cleanchai();//清空拆零数据
            PlayVoice.PlayError(context);
            return;
        }


        //拆零
        String userJson = GsonUtil.parseModelToJson(BaseApplication.userInfo);
        String strOldBarCode = GsonUtil.parseModelToJson(stockin);
//        String strNewBarCode = GsonUtil.parseModelToJson(barcodeModel);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("UserJson", userJson);
        params.put("strOldBarCode", strOldBarCode);
        params.put("strNewBarCode", "");
        params.put("PrintFlag","2"); //1：打印 2：不打印
        LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_BarCodeToStockADF, strOldBarCode);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_BarCodeToStockADF, getString(R.string.Msg_SaveT_BarCodeToStockADF), context, mHandler, RESULT_SaveT_BarCodeToStockADF, null,  URLModel.GetURL().SaveT_BarCodeToStockymhADF, params, null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = parseModelToJson(outStockTaskDetailsInfoModels);
            String UserJson= parseModelToJson(BaseApplication.userInfo);
            params.put("UserJson",UserJson );
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_OutStockTaskDetailADF, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_OutStockTaskDetailADF, getString(R.string.Msg_SaveT_OutStockTaskDetailADF), context, mHandler, RESULT_Msg_SaveT_OutStockTaskDetailADF, null,  URLModel.GetURL().SaveT_OutStockTaskDetailADF, params, null);
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    下架明细获取
     */
    void GetT_OutTaskDetailListByHeaderIDADF(ArrayList<OutStockTaskInfo_Model> outStockTaskInfoModels){
        if(outStockTaskInfoModels!=null) {
            IsEdate=outStockTaskInfoModels.get(0).getIsEdate();
            final Map<String, String> params = new HashMap<String, String>();
            String modelJson= parseModelToJson(outStockTaskInfoModels);
            params.put("ModelDetailJson",modelJson);
            LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF, modelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_OutTaskDetailListByHeaderIDADF, getString(R.string.Msg_QualityDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_OutTaskDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_OutTaskDetailListByHeaderIDADF, params, null);
        }
    }

    /*
    处理下架明细
     */
    void AnalysisGetT_OutTaskDetailListByHeaderIDADFJson(String result){
        LogUtil.WriteLog(OffshelfScan.class, TAG_GetT_OutTaskDetailListByHeaderIDADF,result);
       try {
           ReturnMsgModelList<OutStockTaskDetailsInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutStockTaskDetailsInfo_Model>>() {
           }.getType());
           if (returnMsgModel.getHeaderStatus().equals("S")) {
               outStockTaskDetailsInfoModels = returnMsgModel.getModelJson();
               int size=outStockTaskDetailsInfoModels.size();
               MoveIndex=size;
               //处理拣货数量为0的
               for(int i=0;i<size;i++) {
                   if(ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRePickQty(),
                           outStockTaskDetailsInfoModels.get(i).getScanQty())==0f)
                               Collections.swap(outStockTaskDetailsInfoModels, i, size-1);
              }
               currentPickMaterialIndex = FindFirstCanPickMaterial(); //查找需要拣货物料行
               ShowPickMaterialInfo();//显示需要拣货物料
           } else {
               MessageBox.Show(context, returnMsgModel.getMessage());
           }
       }catch (Exception ex){
           MessageBox.Show(context,ex.getMessage());
       }
    }

    void cleanchai(){
        stockin = new ArrayList<StockInfo_Model>();
        innerNum.setText("0.0");
        edtOffShelfScanbarcode.setText("");
        edtUnboxing.setText("");
    }



//    List<String> Unbarcode;

    ArrayList<StockInfo_Model> stockin = new ArrayList<StockInfo_Model>();//扫描内盒的条码
//    StockInfo_Model stockout = new StockInfo_Model();//扫描内盒条码的外箱标签
    /*扫描条码*/
    void AnalysisGetStockModelADFJson(String result) {
        LogUtil.WriteLog(OffshelfScan.class, TAG_GetStockModelADF, result);
        try {
            ReturnMsgModelList<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<StockInfo_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                stockInfoModels = returnMsgModel.getModelJson();
                if (stockInfoModels != null && stockInfoModels.size() != 0) {
                    if(tbUnboxType.isChecked()) {
                        if (stockin.size() == 0) {
                            //第一次扫描
                            if (stockInfoModels.get(0).getIsAmount() == 1) {
                                //原装的
                                stockin.add(stockInfoModels.get(0));
                                CommonUtil.setEditFocus(edtUnboxing);
                                changeinnernum();
                                if(stockin.size()==1){
                                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                                }
                                return;
                            } else {
                                //拆零的

                            }
                        } else {
                            if(!stockInfoModels.get(0).getFserialno().equals(stockin.get(0).getFserialno())){
                                PlayVoice.PlayError(context);
                                MessageBox.Show(context,"扫描的内盒条码不匹配之前的扫描条码！");
                                CommonUtil.setEditFocus(edtUnboxing);
                                return;
                            }
                            if(!stockin.get(0).getSerialNo().equals(stockInfoModels.get(0).getSerialNo())){
                                if(stockin.indexOf(stockInfoModels.get(0))>=0){
                                    PlayVoice.PlayError(context);
                                    MessageBox.Show(context,"该内盒条码已经扫描！");
                                    CommonUtil.setEditFocus(edtUnboxing);
                                    return;
                                }
                                stockin.add(stockInfoModels.get(0));
                            }
                            changeinnernum();
                            CommonUtil.setEditFocus(edtUnboxing);

                            return;
                        }

                    }




//                        //扫描内盒
//                        if(stockInfoModels.get(0).getBarcodeType()==0){
//                            if(stockin!=null&&stockin.size()>0){
//                                if(!stockin.get(0).getFserialno().equals(stockInfoModels.get(0).getFserialno())){
//                                    PlayVoice.PlayError(context);
//                                    MessageBox.Show(context,"扫描的内盒条码不属于同一个外箱！");
//                                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
//                                    return;
//                                }
//                            }
//                            stockin.add(stockInfoModels.get(0));
//                            changeinnernum();
//                            return;
//                        }else{
//                            //扫描外箱盒
//                            if(stockin==null||stockin.size()==0){
//                                if(stockInfoModels.get(0).getISAMOUNT()!=1){
//                                    //拆箱标签直接插入，不return 走下面流程
//
//                                }else{
//                                    PlayVoice.PlayError(context);
//                                    MessageBox.Show(context,"先扫描外箱标签！");
//                                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
//                                    return;
//                                }
//                            }else{
//                                if(!stockin.get(0).getFserialno().equals(stockInfoModels.get(0).getSerialNo())){
//                                    PlayVoice.PlayError(context);
//                                    MessageBox.Show(context,"扫描的内盒条码不存在该外箱条码中！");
//                                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
//                                    return;
//                                }else{
//                                    stockout=stockInfoModels.get(0);
//                                    return;
//                                }
//
//                            }
//                        }

//                    }

                    //判断条码是否已经扫描
                    final int index = CheckBarcodeScaned();
                    if (index == -1) {
                        insertStockInfo();
                    } else {
                        PlayVoice.PlayError(context);
                        MessageBox.Show(context, getString(R.string.Error_Barcode_hasScan));
                        CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                        //RemoveStockInfo(index);
                    }
                }
            } else {
                PlayVoice.PlayError(context);
                MessageBox.Show(context, returnMsgModel.getMessage());
                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
            }
        } catch (Exception ex) {
            PlayVoice.PlayError(context);
            MessageBox.Show(context, ex.getMessage());
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }

    }

    void AnalysisSaveT_OutStockTaskDetailADFJson(String result){
        try {
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_OutStockTaskDetailADF,result);
            ReturnMsgModelList<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Base_Model>>() {}.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                clearFrm();
                new AlertDialog.Builder(context).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage(returnMsgModel.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
                             closeActiviry();
                            }
                        }).show();
            }else {
                PlayVoice.PlayError(context);
                MessageBox.Show(context, returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            PlayVoice.PlayError(context);
            MessageBox.Show(context, ex.getMessage());
            LogUtil.WriteLog(OffshelfScan.class,"error",ex.getMessage());
        }
    }

    /*
  拆箱提交
   */
    void AnalysisSaveT_BarCodeToStockADF(String result){
        try {
            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_BarCodeToStockADF, result);
            ReturnMsgModel<List<StockInfo_Model>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<List<StockInfo_Model>>>() {}.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                List<StockInfo_Model> models= returnMsgModel.getModelJson();
//                flag=2;
                stockInfoModels=models;

                //判断条码是否已经扫描
                final int index = CheckBarcodeScaned();
                if (index == -1) {
                    insertStockInfo();
                    cleanchai();//清空拆零数据
                } else {
                    PlayVoice.PlayError(context);
                    MessageBox.Show(context, getString(R.string.Error_Barcode_hasScan));
                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                    //RemoveStockInfo(index);
                }






//                MessageBox.Show(context,"拆零成功，新条码序列号："+model.getSerialNo());
//                 unbarcodeModel = new ArrayList<StockInfo_Model>();//拆箱
//                 barcodeModel=new StockInfo_Model();//装箱

//                //获取新的拆零条码后调用外箱的条码扫描
//                final Map<String, String> params = new HashMap<String, String>();
//                params.put("BarCode", model.getBarcode());
//                params.put("InnerBarCode", "");
//                params.put("ScanType", "2");
//                params.put("MoveType", "1"); //1：下架 2:移库
//                params.put("IsEdate", IsEdate); //1：不判断有效期 2:判断有效期
//                LogUtil.WriteLog(OffshelfScan.class, TAG_GetStockModelADF, model.getBarcode());
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetStockModelADF, null, URLModel.GetURL().GetStockModelADF, params, null);

            }else{
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }





//        try {
//            LogUtil.WriteLog(OffshelfScan.class, TAG_SaveT_BarCodeToStockADF, result);
//            ReturnMsgModel<StockInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo_Model>>() {
//            }.getType());
//           if(returnMsgModel.getHeaderStatus().equals("S")){
//               StockInfo_Model stockInfoModel=returnMsgModel.getModelJson();
//               stockInfoModels=new ArrayList<>();
//               stockInfoModels.add(stockInfoModel);
////               currentPickMaterialIndex=FindFirstCanPickMaterialByMaterialNo(stockInfoModels.get(0).getMaterialNo(), stockInfoModels.get(0).getStrongHoldCode());
//               currentPickMaterialIndex=FindFirstCanPickMaterialByMaterialNo(stockInfoModels.get(0).getMaterialNo());
//               if (currentPickMaterialIndex != -1) {
//                   if (CheckStockInfo()) {  //判断是否拣货完毕、是否指定批次
//                       ShowPickMaterialInfo();
//                       txtEDate.setText(CommonUtil.DateToString(stockInfoModels.get(0).getEDate()));
//                       txtStatus.setText(stockInfoModels.get(0).getStrStatus());
//                       SetOutStockTaskDetailsInfoModels(stockInfoModel.getQty(), 3);
//                   }
//               }else {
//                   MessageBox.Show(context, getString(R.string.Error_NotPickMaterial));
//                   CommonUtil.setEditFocus(edtOffShelfScanbarcode);
//               }
//           }
//           else{
//               MessageBox.Show(context, returnMsgModel.getMessage());
//            }
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//        }
//        edtUnboxing.setText("");
//        CommonUtil.setEditFocus(edtOffShelfScanbarcode);

    }

//    private int flag=1;
    void insertStockInfo(){
        currentPickMaterialIndex=FindFirstCanPickMaterialByMaterialNo(stockInfoModels.get(0).getMaterialNo());
        if (currentPickMaterialIndex != -1) {
            if (CheckStockInfo()) {  //判断是否拣货完毕、是否指定批次
               //拆零的时候直接跳过
//                if(tbUnboxType.isChecked()&&flag==1){
//                    barcodeModel=stockInfoModels.get(0);
////                    MessageBox.Show(context,"外箱扫描成功，开始扫描内盒条码！");
//                    CommonUtil.setEditFocus(edtUnboxing);
//                    return;
//                }
//                flag=1;

                if(tbUnboxType.isChecked()&&stockin.size()>1){
                    float sumPalletQty=0f;
                    for(int i=0;i<stockin.size();i++){
                        sumPalletQty=sumPalletQty+stockin.get(i).getQty();
                    }
                    for(int i=0;i<stockInfoModels.size();i++){
                        stockInfoModels.get(i).setPalletQty(sumPalletQty);
                    }
//                    stockInfoModels=stockin;
//                    ShowPickMaterialInfo();
                    txtEDate.setText(CommonUtil.DateToString(stockInfoModels.get(0).getEDate()));
                    txtStatus.setText(stockInfoModels.get(0).getStrStatus());
                    Float scanQty = stockInfoModels.get(0).getPalletQty();
                    checkQTY(scanQty, true);
                    stockin=new ArrayList<StockInfo_Model>();;
                    ShowPickMaterialInfo();
//                    stockout=new StockInfo_Model();
                    return;
                }


                ShowPickMaterialInfo();
                txtEDate.setText(CommonUtil.DateToString(stockInfoModels.get(0).getEDate()));
                txtStatus.setText(stockInfoModels.get(0).getStrStatus());
                if (tbPalletType.isChecked()) {//整托
                    Float scanQty = stockInfoModels.get(0).getPalletQty();
                    checkQTY(scanQty, true);
                } else if (tbBoxType.isChecked()||tbUnboxType.isChecked()) { //整箱
                    Float scanQty = stockInfoModels.get(0).getQty();
                    checkQTY(scanQty, false);
                }
                CommonUtil.setEditFocus(tbUnboxType.isChecked() ? edtUnboxing : edtOffShelfScanbarcode);

            }
        } else {
            MessageBox.Show(context, getString(R.string.Error_NotPickMaterial));
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }
    }

    void RemoveStockInfo(final int index){
//        currentPickMaterialIndex=FindFirstCanPickMaterialByMaterialNoDelete(stockInfoModels.get(0).getMaterialNo(), stockInfoModels.get(0).getStrongHoldCode());
        currentPickMaterialIndex=FindFirstCanPickMaterialByMaterialNoDelete(stockInfoModels.get(0).getMaterialNo());
        if (currentPickMaterialIndex != -1) {
            new AlertDialog.Builder(context).setTitle("提示")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage("是否删除已扫描条码？")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            ShowPickMaterialInfo();
                            for (StockInfo_Model stockInfoModel : stockInfoModels) {
//                            outStockTaskDetailsInfoModels.get(index).
//                                    setScanQty(ArithUtil.sub(outStockTaskDetailsInfoModels.get(index).getScanQty(), stockInfoModel.getQty()));
                                RemoveSameLineMaterialNum(stockInfoModel.getQty());
                                outStockTaskDetailsInfoModels.get(index).getLstStockInfo().remove(stockInfoModel);
                            }
                            currentPickMaterialIndex = FindFirstCanPickMaterial();
                            ShowPickMaterialInfo(); //显示下一拣货物料
                            CommonUtil.setEditFocus(edtOffShelfScanbarcode);

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stockInfoModels = null;
                    CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                }
            }).show();
        } else {
            MessageBox.Show(context, getString(R.string.Error_NotPickMaterial));
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }
    }

    void checkQTY(float scanQty,Boolean isPallet) {
        //根据物料查询扫描剩余数量的总数
      // Float qty=ArithUtil.sub(outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getRemainQty(),
//       Float qty=ArithUtil.sub(outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getRePickQty(),
//               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getScanQty());
//        if (qty< scanQty ||  SumReaminQty<scanQty ) {
       if (SumReaminQty<scanQty ) {
           PlayVoice.PlayError(context);
            MessageBox.Show(context, getString(R.string.Error_offshelfQtyBiger)
                    +"\n需下架数量："+SumReaminQty
                    +"\n扫描数量："+scanQty
                    +"\n拆零后剩余数量："+ArithUtil.sub(scanQty,SumReaminQty));
           stockInfoModels=new ArrayList<>();
           CommonUtil.setEditFocus(edtOffShelfScanbarcode);
            return;
        }
        SetOutStockTaskDetailsInfoModels(scanQty,isPallet?1:2);

    }

    void clearFrm(){
        outStockTaskInfoModels=new ArrayList<>();
        outStockTaskDetailsInfoModels=new ArrayList<>();
        stockInfoModels=new ArrayList<>();
        SumReaminQty=0f; //当前拣货物料剩余拣货数量合计
        currentPickMaterialIndex=-1;
        IsEdate="";
        BindListVIew(outStockTaskDetailsInfoModels);
    }


    //赋值
   void  SetOutStockTaskDetailsInfoModels(Float scanQty,int type) {

       switch (type) {
           case 1: //托盘
               AddSameLineMaterialNum(scanQty);
               for (StockInfo_Model stockInfoModel : stockInfoModels) {
                   stockInfoModel.setPickModel(1);
//                   outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).
//                           setScanQty(ArithUtil.add(outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getScanQty(),stockInfoModel.getQty()));
                   outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getLstStockInfo().add(0, stockInfoModel);
               }
               break;
           case 2://箱子
               AddSameLineMaterialNum(scanQty);
               stockInfoModels.get(0).setPickModel(2);
//               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).
//                       setScanQty(ArithUtil.add(
//                               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getScanQty() , scanQty));
               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getLstStockInfo().add(0, stockInfoModels.get(0));
               break;
           case 3: //拆零
               AddSameLineMaterialNum(scanQty);
               stockInfoModels.get(0).setPickModel(3);
//               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).
//                       setScanQty(ArithUtil.add(outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getScanQty(),scanQty));
               outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getLstStockInfo().add(0, stockInfoModels.get(0));
               break;
       }
       stockInfoModels=new ArrayList<>();
       currentPickMaterialIndex=FindFirstCanPickMaterial();
       ShowPickMaterialInfo(); //显示下一拣货物料
   }


   void AddSameLineMaterialNum(Float ScanReaminQty){
       for(int i=0;i<SameLineoutStockTaskDetailsInfoModels.size();i++){
           if(ScanReaminQty==0f) break;
           Float remainQty=ArithUtil.sub(SameLineoutStockTaskDetailsInfoModels.get(i).getRePickQty(),SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty());
           Float addQty=remainQty<ScanReaminQty?remainQty:ScanReaminQty;
           SameLineoutStockTaskDetailsInfoModels.get(i)
                   .setScanQty(ArithUtil.add( SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty(),addQty));

           SameLineoutStockTaskDetailsInfoModels.get(i).setVoucherType(9996);
           SameLineoutStockTaskDetailsInfoModels.get(i).setFromErpAreaNo( stockInfoModels.get(0).getAreaNo());
           SameLineoutStockTaskDetailsInfoModels.get(i).setFromErpWarehouse( stockInfoModels.get(0).getWarehouseNo());
           SameLineoutStockTaskDetailsInfoModels.get(i).setFromBatchNo( stockInfoModels.get(0).getBatchNo());

           ScanReaminQty=ArithUtil.sub(ScanReaminQty,addQty);
           remainQty=ArithUtil.sub(SameLineoutStockTaskDetailsInfoModels.get(i).getRePickQty(),SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty());
           if(remainQty==0f)
               SameLineoutStockTaskDetailsInfoModels.get(i).setPickFinish(true);
       }
   }

    void RemoveSameLineMaterialNum(Float ScanReaminQty){
        for(int i=0;i<SameLineoutStockTaskDetailsInfoModels.size();i++){
            if(ScanReaminQty==0f) break;
            Float remainQty= SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty();
            Float removeQty=remainQty<ScanReaminQty?remainQty:ScanReaminQty;
            SameLineoutStockTaskDetailsInfoModels.get(i)
                    .setScanQty(ArithUtil.sub( SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty(),removeQty));
            ScanReaminQty=ArithUtil.sub(ScanReaminQty,removeQty);
            remainQty=ArithUtil.sub(SameLineoutStockTaskDetailsInfoModels.get(i).getRePickQty(),SameLineoutStockTaskDetailsInfoModels.get(i).getScanQty());
            if(remainQty!=0f)
                SameLineoutStockTaskDetailsInfoModels.get(i).setPickFinish(false);
        }
    }

    /*
    刷新界面
     */
    void ShowPickMaterialInfo(){
        btnOutOfStock.setEnabled(true);
        if(currentPickMaterialIndex!=-1) {
            if (outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).getLstStockInfo() == null)
                outStockTaskDetailsInfoModels.get(currentPickMaterialIndex).setLstStockInfo(new ArrayList<StockInfo_Model>());
            OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel = outStockTaskDetailsInfoModels.get(currentPickMaterialIndex);
            txtCompany.setText(outStockTaskDetailsInfoModel.getSpec());
            txtBatch.setText(outStockTaskDetailsInfoModel.getFromBatchNo() + " / " + outStockTaskDetailsInfoModel.getIsSpcBatch());
            txtStatus.setText(outStockTaskDetailsInfoModel.getStrStatus());
            txtMaterialName.setText(outStockTaskDetailsInfoModel.getMaterialDesc());
            txtSugestStock.setText(outStockTaskDetailsInfoModel.getAreaNo());
            txtEDate.setText("");
         //   Float qty = ArithUtil.sub(outStockTaskDetailsInfoModel.getRePickQty(),outStockTaskDetailsInfoModel.getScanQty());
            FindSumQtyByMaterialNo(outStockTaskDetailsInfoModel.getMaterialNo());
            //"库："+outStockTaskDetailsInfoModel.getStockQty() + "/
           // txtOffshelfNum.setText("剩余拣货数：" + SumReaminQty);
            txtcurrentPickNum.setText(SumReaminQty+"");

            BindListVIew(outStockTaskDetailsInfoModels);
        }
        else {
            MessageBox.Show(context, getString(R.string.Error_PickingFinish));
            BindListVIew(outStockTaskDetailsInfoModels);
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
        }

    }

    void ShowUnboxing(Boolean show){
        int visiable=show? View.VISIBLE:View.GONE;
        txtUnboxing.setVisibility(visiable);
        edtUnboxing.setVisibility(visiable);
        btnInOfStock.setVisibility(visiable);
        innerNum.setVisibility(visiable);
        CommonUtil.setEditFocus(edtUnboxing);
       // btnPrintBox.setVisibility(visiable);
    }

    /*
    判断物料是否已经扫描
     */
    int CheckBarcodeScaned(){
        for (int i=0;i<outStockTaskDetailsInfoModels.size();i++) {
                if(outStockTaskDetailsInfoModels.get(i).getLstStockInfo()!=null) {
                    if (outStockTaskDetailsInfoModels.get(i).getLstStockInfo().indexOf(stockInfoModels.get(0)) != -1) {
                        return i;
                    }
                }
            }
        return -1;
    }

    Boolean CheckStockInfo(){
        OutStockTaskDetailsInfo_Model currentOustStock = outStockTaskDetailsInfoModels.get(currentPickMaterialIndex);
        //判断是否拣货完毕
        if (currentOustStock.getRePickQty().compareTo(currentOustStock.getScanQty()) == 0) {
            btnOutOfStock.setEnabled(false);
            MessageBox.Show(context, getString(R.string.Error_MaterialPickFinish));
            CommonUtil.setEditFocus(edtOffShelfScanbarcode);
            return  false;
        }
        //判断是否指定批次
        if(currentOustStock.getIsSpcBatch().toUpperCase().equals("Y")){
            if(!currentOustStock.getFromBatchNo().equals(stockInfoModels.get(0).getBatchNo())){
                MessageBox.Show(context, getString(R.string.Error_batchNONotMatch)+"|批次号："+currentOustStock.getFromBatchNo());
                CommonUtil.setEditFocus(edtOffShelfScanbarcode);
                return false;
            }
        }
        return true;
    }

    /*
    分配拣货数量，优先满足第一个拣货数量不满的物料
     */
    void DistributionPickingNum(String MaterialNo,Float PickNum){
        for(int i=0;i<outStockTaskDetailsInfoModels.size();i++){
            if(outStockTaskDetailsInfoModels.get(i).getMaterialNo().equals(MaterialNo)){
              Float remainQty=ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRemainQty(),outStockTaskDetailsInfoModels.get(i).getScanQty());
                 if(remainQty==0f){
                     continue;
                 }
                 if(PickNum>=remainQty){
                     outStockTaskDetailsInfoModels.get(i).setScanQty(ArithUtil.add(outStockTaskDetailsInfoModels.get(i).getScanQty(),remainQty));
                     PickNum=ArithUtil.sub(PickNum,remainQty);
                 }else{
                     outStockTaskDetailsInfoModels.get(i).setScanQty(ArithUtil.add(outStockTaskDetailsInfoModels.get(i).getScanQty(),PickNum));
                     break;
                 }

            }
        }
    }
    String ErpVoucherno="";
    /*
    统计相同行物料剩余拣货数量
     */
    void FindSumQtyByMaterialNo(String MaterialNo){
        SumReaminQty=0.0f;
        ErpVoucherno="";
        SameLineoutStockTaskDetailsInfoModels=new ArrayList<>();
       // for(int i=outStockTaskDetailsInfoModels.size()-1;i>=0;i--){
        for(int i=0;i<outStockTaskDetailsInfoModels.size();i++){
            if(outStockTaskDetailsInfoModels.get(i).getMaterialNo().equals(MaterialNo)) {
                if (TextUtils.isEmpty(ErpVoucherno))
                    ErpVoucherno = outStockTaskDetailsInfoModels.get(i).getErpVoucherNo();
                if (ErpVoucherno.equals(outStockTaskDetailsInfoModels.get(i).getErpVoucherNo())) {
                    SameLineoutStockTaskDetailsInfoModels.add(outStockTaskDetailsInfoModels.get(i));
                    SumReaminQty = ArithUtil.add(SumReaminQty, ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRePickQty(), outStockTaskDetailsInfoModels.get(i).getScanQty()));
                }
            }
        }
    }


    /*
    查找需要拣货物料位置，拣货数量为0，且不是缺货状态
     */
    int FindFirstCanPickMaterial(){
        int size=outStockTaskDetailsInfoModels.size();
        MovePickFinishMaterial();
        int index=-1;
        for(int i=0;i<size;i++){
//            if(outStockTaskDetailsInfoModels.get(i).getScanQty()!=null
//            && (outStockTaskDetailsInfoModels.get(i).getScanQty()!=outStockTaskDetailsInfoModels.get(i).getTaskQty()
//            // && ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRemainQty(),outStockTaskDetailsInfoModels.get(i).getScanQty())!=0
//             && ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRePickQty(),outStockTaskDetailsInfoModels.get(i).getScanQty())!=0
//            ) && !outStockTaskDetailsInfoModels.get(i).getOutOfstock() ){
            if(!outStockTaskDetailsInfoModels.get(i).getPickFinish()
                    && !outStockTaskDetailsInfoModels.get(i).getOutOfstock() ){
                index= i;
                break;
            }
        }
        return index;
    }

//    int FindFirstCanPickMaterialByMaterialNo(String MaterialNo,String StrongHoldCode){
        int FindFirstCanPickMaterialByMaterialNo(String MaterialNo){
            int size=outStockTaskDetailsInfoModels.size();
        int index=-1;
        for(int i=0;i<size;i++){
//            if(outStockTaskDetailsInfoModels.get(i).getScanQty()!=null
//                    && (outStockTaskDetailsInfoModels.get(i).getScanQty()!=outStockTaskDetailsInfoModels.get(i).getTaskQty()
//                   // &&ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRemainQty(),outStockTaskDetailsInfoModels.get(i).getScanQty())!=0
//                    &&ArithUtil.sub(outStockTaskDetailsInfoModels.get(i).getRePickQty(),outStockTaskDetailsInfoModels.get(i).getScanQty())!=0
//            ) && outStockTaskDetailsInfoModels.get(i).getMaterialNo().equals(MaterialNo)
//                    && outStockTaskDetailsInfoModels.get(i).getStrongHoldCode().equals(StrongHoldCode)){

            if( (!outStockTaskDetailsInfoModels.get(i).getPickFinish())//没有拣货完毕
                    && outStockTaskDetailsInfoModels.get(i).getMaterialNo().equals(MaterialNo)
//                    && outStockTaskDetailsInfoModels.get(i).getStrongHoldCode().equals(StrongHoldCode)
                    ){
                   // && outStockTaskDetailsInfoModels.get(i).getHeaderID()==HeadID){
            ErpVoucherno= outStockTaskDetailsInfoModels.get(i).getErpVoucherNo();
                index= i;
                break;
            }
        }
        return index;
    }

//    int FindFirstCanPickMaterialByMaterialNoDelete(String MaterialNo,String StrongHoldCode){
        int FindFirstCanPickMaterialByMaterialNoDelete(String MaterialNo){
        int size=outStockTaskDetailsInfoModels.size();
        int index=-1;
        for(int i=0;i<size;i++){
            if(outStockTaskDetailsInfoModels.get(i).getMaterialNo().equals(MaterialNo)
//                    && outStockTaskDetailsInfoModels.get(i).getStrongHoldCode().equals(StrongHoldCode)
                    )
                    {
                ErpVoucherno= outStockTaskDetailsInfoModels.get(i).getErpVoucherNo();
                index= i;
                break;
            }
        }
        return index;
    }

    private void BindListVIew(ArrayList<OutStockTaskDetailsInfo_Model> outStockTaskDetailsInfoModels) {
        offShelfScanDetailAdapter=new OffShelfScanDetailAdapter(context,outStockTaskDetailsInfoModels);
        lsvPickList.setAdapter(offShelfScanDetailAdapter);
    }

    int MoveIndex=-1;
    /*
   移动拣货完毕物料至末尾
    */
    void MovePickFinishMaterial() {
       // int size = outStockTaskDetailsInfoModels.size();

        for (int i = 0; i < MoveIndex; i++) {
            if (outStockTaskDetailsInfoModels.get(i).getPickFinish()) {
                Collections.swap(outStockTaskDetailsInfoModels, i, MoveIndex - 1);
                MoveIndex = MoveIndex - 1;
            }

            //}
        }

    }


}
