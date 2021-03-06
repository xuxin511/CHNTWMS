package chinetek.xx.chntwms.cywms.Receiption;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinetek.xx.chntwms.adapter.wms.Receiption.ReceiptScanDetailAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Base_Model;
import chinetek.xx.chntwms.model.Material.BarCodeInfo;
import chinetek.xx.chntwms.model.Receiption.ReceiptDetail_Model;
import chinetek.xx.chntwms.model.Receiption.Receipt_Model;
import chinetek.xx.chntwms.model.ReturnMsgModel;
import chinetek.xx.chntwms.model.ReturnMsgModelList;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.PlayVideo.PlayVoice;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.function.ArithUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;
import chinetek.xx.chntwms.util.function.DoubleClickCheck;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

import static chinetek.xx.chntwms.util.dialog.ToastUtil.show;
import static chinetek.xx.chntwms.util.function.GsonUtil.parseModelToJson;


@ContentView(R.layout.activity_cgreceiption_scan)
public class CGReceiptionScan extends BaseActivity {

    String TAG_GetT_InStockDetailListByHeaderIDADF="ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF="ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_InStockDetailADF="ReceiptionScan_SaveT_InStockDetailADF";

    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF=101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=102;
    private final int RESULT_Msg_SaveT_InStockDetailADF=103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                AnalysisGetT_InStockDetailListJson((String) msg.obj);
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByNoADF((String) msg.obj);
                break;
            case RESULT_Msg_SaveT_InStockDetailADF:
                AnalysisSaveT_InStockDetailADFJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtRecScanBarcode);
                break;
        }
    }


    Context context = CGReceiptionScan.this;
    @ViewInject(R.id.lsv_ReceiptScan)
    ListView lsvReceiptScan;
    @ViewInject(R.id.edt_RecScanBarcode)
    EditText edtRecScanBarcode;
    @ViewInject(R.id.txt_VoucherNo)
    TextView txtVoucherNo;
    @ViewInject(R.id.txt_ReceQTY)
    TextView txtInStockQty;
    @ViewInject(R.id.txt_CanReceQty)
    TextView txtRemainQty;
    @ViewInject(R.id.txt_ReceScanQty)
    TextView txtReceiveQty;
    @ViewInject(R.id.txt_ScanQty)
    TextView txtScanQty;
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

    ReceiptScanDetailAdapter receiptScanDetailAdapter;
    ArrayList<ReceiptDetail_Model> receiptDetailModels;
    ArrayList<BarCodeInfo> barCodeInfos=null;
    Receipt_Model receiptModel=null;
  //  boolean isDel=false;//删除已扫条码

//  List<ReceiptDetail_Model> receiptDetailModelsnew;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.receiptscan_subtitle), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
    }

    @Override
    protected void initData() {
        super.initData();
        receiptModel=getIntent().getParcelableExtra("receiptModel");
        txtVoucherNo.setText(receiptModel.getErpVoucherNo());
//        receiptDetailModelsnew=receiptModel.getLstDetail();
//        for ( int i=0;i<receiptModel.getLstDetail().size();i++){
//            receiptDetailModels.add(receiptModel.getLstDetail().get(i));
//        }

        BindListVIew(receiptModel.getLstDetail());
//        this.barCodeInfos=getIntent().getParcelableArrayListExtra("barCodeInfo");
//        GetReceiptDetail(receiptModel);

    }


    @Event(value = R.id.edt_RecScanBarcode,type = View.OnKeyListener.class)
    private  boolean edtRecScanBarcode(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String code=edtRecScanBarcode.getText().toString().trim();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("BarCode", code);
            params.put("UserJson", parseModelToJson(BaseApplication.userInfo));
            LogUtil.WriteLog(CGReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
        }
        return false;
    }

    @Event(value = R.id.lsv_ReceiptScan,type =  AdapterView.OnItemClickListener.class)
    private  boolean lsv_ReceiptScanItemClick(AdapterView<?> parent, View view, int position, long id){
        if(id>=0) {
            ReceiptDetail_Model receiptDetailModel=receiptModel.getLstDetail().get(position);
            if(receiptDetailModel.getLstBarCode()!=null && receiptDetailModel.getLstBarCode().size()!=0) {
                Intent intent = new Intent(context, ReceiptionBillDetail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("receiptDetailModel", receiptDetailModel);
                intent.putExtras(bundle);
                startActivityLeft(intent);
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            if (DoubleClickCheck.isFastDoubleClick(context)) {
                return false;
            }
            Boolean isFinishReceipt=true;
            //其他入库单
            if(receiptDetailModels!=null && receiptDetailModels.get(0).getVoucherType()==23) {
                for (ReceiptDetail_Model receiptDetail : receiptDetailModels) {
                    if (receiptDetail.getScanQty()!=receiptDetail.getRemainQty()) {
                        MessageBox.Show(context, "其他入库单,物料号【"+receiptDetail.getMaterialNo()+"】出库数量没有扫描齐全！");
                        PlayVoice.PlayError(context);
                        isFinishReceipt=false;
                        break;
                    }
                }
            }

            //非采购订单不能多次收货
            if(receiptDetailModels!=null && receiptDetailModels.get(0).getVoucherType()!=22) {
                for (ReceiptDetail_Model receiptDetail : receiptDetailModels) {
                    if (receiptDetail.getScanQty()>receiptDetail.getRemainQty()) {
                        MessageBox.Show(context, "扫描数量不能大于剩余数量！");
                        PlayVoice.PlayError(context);
                        isFinishReceipt=false;
                        break;
                    }
                }
            }
            if(isFinishReceipt) {
                final Map<String, String> params = new HashMap<String, String>();
                String ModelJson = parseModelToJson(receiptDetailModels);
                String UserJson = parseModelToJson(BaseApplication.userInfo);
                params.put("UserJson", UserJson);
                params.put("ModelJson", ModelJson);
                LogUtil.WriteLog(CGReceiptionScan.class, TAG_SaveT_InStockDetailADF, ModelJson);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_InStockDetailADF, getString(R.string.Msg_SaveT_InStockDetailADF), context, mHandler, RESULT_Msg_SaveT_InStockDetailADF, null, URLModel.GetURL().SaveT_InStockDetailADF, params, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    获取收货明细
     */
//    void GetReceiptDetail(Receipt_Model receiptModel){
//        if(receiptModel!=null) {
//            txtVoucherNo.setText(receiptModel.getErpVoucherNo());
//            final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
//            receiptDetailModel.setHeaderID(receiptModel.getID());
//            receiptDetailModel.setErpVoucherNo(receiptModel.getErpVoucherNo());
//            receiptDetailModel.setVoucherType(receiptModel.getVoucherType());
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
//            String para = (new JSONObject(params)).toString();
//            LogUtil.WriteLog(CGReceiptionScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null,  URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
//        }
//    }

    /*
    处理收货明细
     */
    void AnalysisGetT_InStockDetailListJson(String result){
        LogUtil.WriteLog(CGReceiptionScan.class, TAG_GetT_InStockDetailListByHeaderIDADF,result);
        try {
            ReturnMsgModelList<ReceiptDetail_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<ReceiptDetail_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptDetailModels = returnMsgModel.getModelJson();
                //自动确认扫描箱号
                BindListVIew(receiptDetailModels);
                if (barCodeInfos != null) {
                    isDel=false;
                    if(receiptModel.getVoucherType()==30||receiptModel.getVoucherType()==25){ BindbarcodeDB(barCodeInfos);}else{ Bindbarcode(barCodeInfos);}
                }
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
            }
        }catch (Exception ex) {
            MessageBox.Show(context,ex.getMessage());
        }
        CommonUtil.setEditFocus(edtRecScanBarcode);
    }

    /*
    扫描条码
     */
    void AnalysisGetT_PalletDetailByNoADF(String result){
        LogUtil.WriteLog(CGReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF,result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
                isDel=false;
                if(receiptModel.getVoucherType()==30||receiptModel.getVoucherType()==25){
                    BindbarcodeDB(barCodeInfos);
                }
                else{
                    Bindbarcode(barCodeInfos);
                }
//                Bindbarcode(barCodeInfos);
            } else {
                MessageBox.Show(context,returnMsgModel.getMessage());
                PlayVoice.PlayError(context);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.toString());
            PlayVoice.PlayError(context);
        }
        CommonUtil.setEditFocus(edtRecScanBarcode);
    }

    /*
    提交收货
     */
    void AnalysisSaveT_InStockDetailADFJson(String result){
        try {
            LogUtil.WriteLog(CGReceiptionScan.class, TAG_SaveT_InStockDetailADF,result);
            final ReturnMsgModel<Base_Model> returnMsgModel =  GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
            }.getType());
            if(returnMsgModel.getHeaderStatus().equals("S")){
                linm= new ArrayList<>();
                new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(returnMsgModel.getMessage())
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO 自动生成的方法
                                                if (receiptDetailModels.get(0).getVoucherType() == 22) {
//                                                    Intent intent = new Intent(context, QCMaterialChoice.class);
//                                                    String ErpVourcherNo = returnMsgModel.getMaterialDoc();
//                                                    intent.putExtra("ErpVourcherNo", ErpVourcherNo);
//                                                    startActivityLeft(intent);
                                                }
                                                closeActiviry();
                                            }
                                        }).show();
            }else {
                MessageBox.Show(context, returnMsgModel.getMessage());
                PlayVoice.PlayError(context);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
            PlayVoice.PlayError(context);
        }
    }


    void InitFrm(BarCodeInfo barCodeInfo){
        if(barCodeInfo!=null ){
            txtCompany.setText(barCodeInfo.getStrongHoldName());
            txtBatch.setText(barCodeInfo.getBatchNo());
            txtStatus.setText(barCodeInfo.getStrStatus());
            txtMaterialName.setText(barCodeInfo.getMaterialDesc());
            txtEDate.setText(CommonUtil.DateToString(barCodeInfo.getEDate()));
        }
        CommonUtil.setEditFocus(edtRecScanBarcode);
    }

    boolean isDel=false;
    boolean isGo=false;
    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    if (barCodeInfo != null && receiptDetailModels != null) {
                        ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model(barCodeInfo.getMaterialNo(), barCodeInfo.getRowNo());
                        final int index = receiptDetailModels.indexOf(receiptDetailModel);
                        if (index != -1) {
                            if(!barCodeInfo.getErpVoucherNo().equals(receiptDetailModels.get(index).getErpVoucherNo())){
                                MessageBox.Show(context,getString(R.string.Error_ErpvoucherNoMatch) + "|" + barCodeInfo.getSerialNo());
                                PlayVoice.PlayError(context);
                                break;
                            }

                            //是否指定批次
                            if (receiptDetailModels.get(index).getIsSpcBatch().equals("Y")) {
                                if (!receiptDetailModels.get(index).getFromBatchNo().equals(barCodeInfo.getBatchNo())) {
                                    MessageBox.Show(context, getString(R.string.Error_batchNONotMatch) + "|" + barCodeInfo.getSerialNo());
                                    PlayVoice.PlayError(context);
                                    break;
                                }
                            }

                            if (receiptDetailModels.get(index).getLstBarCode() == null)
                                receiptDetailModels.get(index).setLstBarCode(new ArrayList<BarCodeInfo>());
                            final int barIndex = receiptDetailModels.get(index).getLstBarCode().indexOf(barCodeInfo);
                            if (barIndex != -1) {
                                if (isDel) {
                                    RemoveBarcode(index, barIndex);
                                } else {
                                    new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // TODO 自动生成的方法
                                                    //RemoveBarcode(index, barIndex);
                                                    isDel = true;
                                                    Bindbarcode(barCodeInfos);
                                                }
                                            }).setNegativeButton("取消", null).show();
                                    break;
                                }
                            } else {
                                if (!CheckBarcode(barCodeInfo, index)){
                                    PlayVoice.PlayError(context);
                                    break;
                                }

                            }
                            RefeshFrm(index);
                        } else {
                            MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + barCodeInfo.getSerialNo());
                            PlayVoice.PlayError(context);
                            break;
                        }
                    }

                }
                InitFrm(barCodeInfos.get(0));
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtRecScanBarcode);
            }

        }
    }

     ArrayList<String> linm = new ArrayList<String>();
    void BindbarcodeDB(final ArrayList<BarCodeInfo> barCodeInfos){
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                isDel=false;
                isGo=false;
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    if (barCodeInfo != null && receiptDetailModels != null) {
                        //查找复核要求的调拨单明细行
                        int indexL=-1;
                        for(int i=0;i<receiptDetailModels.size();i++){
                            if(receiptDetailModels.get(i).getMaterialNo().equals(barCodeInfo.getMaterialNo())
                                    &&receiptDetailModels.get(i).getRemainQty()>receiptDetailModels.get(i).getScanQty()){
                                indexL=i;
                                break;
                            }
                        }
                        if (indexL != -1) {
                            if(receiptModel.getVoucherType()!=25){
                                if(!barCodeInfo.getErpVoucherNo().equals(receiptDetailModels.get(indexL).getErpVoucherNo())){
                                    MessageBox.Show(context,getString(R.string.Error_ErpvoucherNoMatch) + "|" + barCodeInfo.getSerialNo());
                                    PlayVoice.PlayError(context);
                                    break;
                                }
                            }

                            //是否指定批次
                            if (receiptDetailModels.get(indexL).getIsSpcBatch().equals("Y")) {
                                if (!receiptDetailModels.get(indexL).getFromBatchNo().equals(barCodeInfo.getBatchNo())) {
                                    MessageBox.Show(context, getString(R.string.Error_batchNONotMatch) + "|" + barCodeInfo.getSerialNo());
                                    PlayVoice.PlayError(context);
                                    break;
                                }
                            }

                            if(linm==null||linm.size()==0){
                                receiptDetailModels.get(indexL).setLstBarCode(new ArrayList<BarCodeInfo>());
                                if (!CheckBarcode(barCodeInfo, indexL))
                                    break;
                            }else{
                                boolean flag=false;
                                for(int j=0;j<linm.size();j++){
                                    if(linm.get(j).contains(barCodeInfo.getSerialNo())){
                                        flag=true;
                                        final int jChuan=j;
                                        final String[] sub = linm.get(j).split(",");
                                        final BarCodeInfo barCodeInfoChuan=barCodeInfo;
                                        if(isGo){
                                            break;
                                        }
                                        if(isDel){
                                            linm.remove(jChuan);
                                            int barIndexl = receiptDetailModels.get(Integer.parseInt(sub[1])).getLstBarCode().indexOf(barCodeInfoChuan);
                                            RemoveBarcode(Integer.parseInt(sub[1]), barIndexl);
                                            break;
                                        }
                                        new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除已扫描条码？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // TODO 自动生成的方法
                                                        //RemoveBarcode(index, barIndex);
                                                        isDel = true;
                                                        linm.remove(jChuan);
                                                        int barIndexl = receiptDetailModels.get(Integer.parseInt(sub[1])).getLstBarCode().indexOf(barCodeInfoChuan);
                                                        RemoveBarcode(Integer.parseInt(sub[1]), barIndexl);
                                                    }
                                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // TODO 自动生成的方法
                                                            isGo = true;
                                                        }
                                        }).show();
//                                        break;
                                    }
                                }
                                if(!flag){
                                    if (!CheckBarcode(barCodeInfo, indexL)){
                                        PlayVoice.PlayError(context);
                                        break;
                                    }

                                }
                            }
                            RefeshFrm(indexL);
                        } else {
                            MessageBox.Show(context, "收货物料行已满或者不存在该物料行！");
                            PlayVoice.PlayError(context);
                            break;
                        }
                    }

                }
                InitFrm(barCodeInfos.get(0));
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtRecScanBarcode);
            }

        }
    }

















    boolean CheckBarcode(BarCodeInfo barCodeInfo,int index) {
        boolean isChecked = false;
        if (receiptDetailModels.get(index).getRemainQty() == 0) {
            MessageBox.Show(context, getString(R.string.Error_ReceiveFinish));
            return false;
        }

//        if (receiptDetailModels.get(index).getLstBarCode().size() != 0) {
//            if (!barCodeInfo.getBatchNo().equals(receiptDetailModels.get(index).getLstBarCode().get(0).getBatchNo())) {
//                MessageBox.Show(context, getString(R.string.Error_ReceivebatchError) + "|" + barCodeInfo.getSerialNo());
//                return false;
//            }
//            if (!barCodeInfo.getSupPrdBatch().equals(receiptDetailModels.get(index).getLstBarCode().get(0).getSupPrdBatch())) {
//                MessageBox.Show(context, getString(R.string.Error_ProductbatchError) + "|" + barCodeInfo.getSerialNo());
//                return false;
//            }
//        }
        isChecked =Addbarcode(index, barCodeInfo);
        return isChecked;
    }


    boolean RemoveBarcode(final  int index, final int barIndex){
        float qty= ArithUtil.sub(receiptDetailModels.get(index).getScanQty(),receiptDetailModels.get(index).getLstBarCode().get(barIndex).getQty());
                //receiptDetailModels.get(index).getScanQty()-receiptDetailModels.get(index).getLstBarCode().get(barIndex).getQty();
        receiptDetailModels.get(index).getLstBarCode().remove(barIndex);
        receiptDetailModels.get(index).setScanQty(qty);
        return true;
    }

    boolean Addbarcode(int index,BarCodeInfo barCodeInfo){
        float qty= ArithUtil.add(receiptDetailModels.get(index).getScanQty(),barCodeInfo.getQty());
                //receiptDetailModels.get(index).getScanQty()+barCodeInfo.getQty();

          if(qty<=receiptDetailModels.get(index).getRemainQty()) {
            receiptDetailModels.get(index).getLstBarCode().add(0, barCodeInfo);
            receiptDetailModels.get(index).setBatchNo(barCodeInfo.getBatchNo());
            receiptDetailModels.get(index).setScanQty(qty);

            if(receiptModel.getVoucherType()==30||receiptModel.getVoucherType()==25){
                //调拨,采购订单用临时放条码
                linm.add(barCodeInfo.getSerialNo()+","+index);
            }

            return true;
        }else{
            MessageBox.Show(context, getString(R.string.Error_ReceiveOver));
        }
        return false;
    }

    void RefeshFrm(int index){
        txtInStockQty.setText(receiptDetailModels.get(index).getInStockQty() + "");
        txtReceiveQty.setText(receiptDetailModels.get(index).getReceiveQty() + "");
        txtRemainQty.setText(receiptDetailModels.get(index).getRemainQty() + "");
        txtScanQty.setText(receiptDetailModels.get(index).getScanQty() + "");
        BindListVIew(receiptDetailModels);
    }

    private void BindListVIew(List<ReceiptDetail_Model> receiptDetailModels) {
        receiptScanDetailAdapter=new ReceiptScanDetailAdapter(context,receiptDetailModels);
        lsvReceiptScan.setAdapter(receiptScanDetailAdapter);
    }
}
