package chinetek.xx.chntwms.cywms.Receiption;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinetek.xx.chntwms.adapter.wms.Receiption.ReceiptBillChioceItemAdapter;
import chinetek.xx.chntwms.adapter.wms.Receiption.ReceiptcgBillChioceItemAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Material.BarCodeInfo;
import chinetek.xx.chntwms.model.Receiption.ReceiptDetail_Model;
import chinetek.xx.chntwms.model.Receiption.Receipt_Model;
import chinetek.xx.chntwms.model.ReturnMsgModelList;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.util.Network.NetworkError;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.PlayVideo.PlayVoice;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.ArithUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;
import chinetek.xx.chntwms.util.function.GsonUtil;
import chinetek.xx.chntwms.util.log.LogUtil;

import static chinetek.xx.chntwms.util.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_cgreceipt_bill_choice)
public class CGReceiptBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    String TAG_GetT_InStockList = "CGReceiptBillChoice_GetT_InStockList";
    String TAG_GetT_PalletDetailByBarCode = "CGReceiptBillChoice_GetT_PalletDetailByBarCode";
    private final int RESULT_GetT_CGInStockList = 101;
    private final int RESULT_GetT_PalletDetailByBarCode=102;
    private final int supplierRequestCode = 1001;
    String TAG_GetT_PalletDetailByBarCodeADF="CGReceiptionScan_GetT_PalletDetailByBarCodeADF";
    private final int RESULT_Msg_GetT_PalletDetailByBarCode=103;


    String TAG_Msg_Post = "CGReceiptBillChoice_post";
    private final int RESULT_Post = 104;


    Context context = CGReceiptBillChoice.this;
    boolean isCancelFilterButton=false; //供应商筛选标志


    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        switch (msg.what) {
            case RESULT_Post:
                AnalysisGetT_PostJson((String) msg.obj);//2
                break;
            case RESULT_GetT_CGInStockList:
                AnalysisGetT_InStockListJson((String) msg.obj);//2
                break;
            case RESULT_GetT_PalletDetailByBarCode:
                AnalysisGetT_PalletDetailByBarCodeJson((String) msg.obj);//1
                break;
//            case RESULT_Msg_GetT_PalletDetailByBarCode:
//                AnalysisGetT_PalletDetailByNoADF((String) msg.obj);
//                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                CommonUtil.setEditFocus(edtfilterContent);
                break;
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView lsvChoiceReceipt;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText edtfilterContent;
    @ViewInject(R.id.txt_Suppliername)
    TextView txtSuppliername;
    MenuItem  gMenuItem=null;


    ArrayList<Receipt_Model> receiptModels = new ArrayList<Receipt_Model>();//单据信息
    List<Map<String, String>> SupplierList= new ArrayList<Map<String, String>>();//供应商列表
    ReceiptcgBillChioceItemAdapter receiptcgBillChioceItemAdapter;

    ArrayList<BarCodeInfo> barCodeInfos;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.receipt_subtitle), false);
        x.view().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!isCancelFilterButton)
//            InitListView();
    }

    @Override
    protected void initData() {
        super.initData();
//        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    public void onRefresh() {
        if(isCancelFilterButton){
            isCancelFilterButton=false;
//            gMenuItem.setTitle(getResources().getString(R.string.filter));
//            txtSuppliername.setText(getResources().getString(R.string.supplierNoFilter));
        }
        InitListView();
    }


    /**
     * 初始化加载listview
     */
    private void InitListView() {
        barCodeInfos=new ArrayList<>();
        receiptModels=new ArrayList<>();
        edtfilterContent.setText("");
//        Receipt_Model receiptModel = new Receipt_Model();
//        receiptModel.setStatus(1);
//        GetT_InStockList(receiptModel);
    }

    /*扫描条码*/
//    void AnalysisGetT_PalletDetailByNoADF(String result){
//        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF,result);
//        try {
//            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                ArrayList<BarCodeInfo> barCodeInfos = returnMsgModel.getModelJson();
//                Bindbarcode(barCodeInfos);
//                BindListVIew(receiptModels);
//            } else {
//                MessageBox.Show(context,returnMsgModel.getMessage());
//                PlayVoice.PlayError(context);
//            }
//        }catch (Exception ex){
//            MessageBox.Show(context,ex.toString());
//            PlayVoice.PlayError(context);
//        }
//        CommonUtil.setEditFocus(edtfilterContent);
//    }

    boolean isDel=false;
    boolean isover=false;
    List<ReceiptDetail_Model> receiptDetailModels;
    int  CurrentreceiptModelIndex;//当前被选中的表头
    void Bindbarcode(final ArrayList<BarCodeInfo> barCodeInfos){
        isover=false;
        if (barCodeInfos != null && barCodeInfos.size() != 0) {
            try {
                int forindex=0;
                for (BarCodeInfo barCodeInfo : barCodeInfos) {
                    if(isover){return;}
                    forindex++;
                    receiptDetailModels= new ArrayList<ReceiptDetail_Model>();
                    CurrentreceiptModelIndex = -1;
                    //找到对应的采购订单
                    Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getErpVoucherNo());
                    int indexR = receiptModels.indexOf(receiptModel);
                    if (indexR != -1) {
                        CurrentreceiptModelIndex=indexR;
                        receiptDetailModels = receiptModels.get(indexR).getLstDetail();

                        ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model(barCodeInfo.getMaterialNo(), barCodeInfo.getRowNo());
                        final int index = receiptDetailModels.indexOf(receiptDetailModel);
                        if (index != -1) {
                            if (!barCodeInfo.getErpVoucherNo().equals(receiptDetailModels.get(index).getErpVoucherNo())) {
                                MessageBox.Show(context, getString(R.string.Error_ErpvoucherNoMatch) + "|" + barCodeInfo.getSerialNo());
                                PlayVoice.PlayError(context);
                                break;
                            }

                            //是否指定批次
//                            if (receiptDetailModels.get(index).getIsSpcBatch().equals("Y")) {
//                                if (!receiptDetailModels.get(index).getFromBatchNo().equals(barCodeInfo.getBatchNo())) {
//                                    MessageBox.Show(context, getString(R.string.Error_batchNONotMatch) + "|" + barCodeInfo.getSerialNo());
//                                    PlayVoice.PlayError(context);
//                                    break;
//                                }
//                            }

                            if (receiptDetailModels.get(index).getLstBarCode() == null)
                                receiptDetailModels.get(index).setLstBarCode(new ArrayList<BarCodeInfo>());
                            final int barIndex = receiptDetailModels.get(index).getLstBarCode().indexOf(barCodeInfo);
                            if (barIndex != -1) {
                                if (isDel) {
                                    float allqty=0f;
                                    if(barCodeInfos.size()==1){
                                        RemoveBarcode(index, barIndex);
                                        allqty=barCodeInfo.getQty();
                                    }else{
                                        for (int i=0;i<barCodeInfos.size();i++){
                                             int bIndex = receiptDetailModels.get(index).getLstBarCode().indexOf(barCodeInfos.get(i));
                                            allqty=barCodeInfos.get(i).getQty()+allqty;
                                            RemoveBarcode(index, bIndex);
                                        }
                                    }
//                                    receiptModels.get(CurrentreceiptModelIndex).setScannum(receiptModels.get(CurrentreceiptModelIndex).getScannum()-barCodeInfo.getQty());
//                                    receiptModels.get(CurrentreceiptModelIndex).setRemainnum((int)(receiptModels.get(CurrentreceiptModelIndex).getRemainnum()+barCodeInfo.getQty()));

                                    receiptModels.get(CurrentreceiptModelIndex).setScannum(receiptModels.get(CurrentreceiptModelIndex).getScannum()-allqty);
                                    receiptModels.get(CurrentreceiptModelIndex).setRemainnum((int)(receiptModels.get(CurrentreceiptModelIndex).getRemainnum()+allqty));
                                    BindListVIew(receiptModels);
                                    isDel = false;
                                    isover=true;
                                } else {
                                    if(forindex==1){
                                        PlayVoice.PlayError(context);
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
                                    }
                                    break;
                                }
                            } else {
                                if (!CheckBarcode(barCodeInfo, index)) {
                                    PlayVoice.PlayError(context);
                                    break;
                                }

                            }
//                            RefeshFrm(index);
                        } else {
                            MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + barCodeInfo.getSerialNo());
                            PlayVoice.PlayError(context);
                            break;
                        }
                    }
                }
            }catch (Exception ex){
                MessageBox.Show(context,ex.getMessage());
                CommonUtil.setEditFocus(edtfilterContent);
            }

        }
    }

    boolean RemoveBarcode(final  int index, final int barIndex){
        float qty= ArithUtil.sub(receiptDetailModels.get(index).getScanQty(),receiptDetailModels.get(index).getLstBarCode().get(barIndex).getQty());
        receiptDetailModels.get(index).getLstBarCode().remove(barIndex);
        receiptDetailModels.get(index).setScanQty(qty);
        return true;
    }

    boolean CheckBarcode(BarCodeInfo barCodeInfo,int index) {
        boolean isChecked = false;
        if (receiptDetailModels.get(index).getRemainQty() == 0) {
            MessageBox.Show(context, getString(R.string.Error_ReceiveFinish));
            return false;
        }
        isChecked =Addbarcode(index, barCodeInfo);
        if(isChecked){
            receiptModels.get(CurrentreceiptModelIndex).setScannum((receiptModels.get(CurrentreceiptModelIndex).getScannum()==null?0:receiptModels.get(CurrentreceiptModelIndex).getScannum())+barCodeInfo.getQty());
            receiptModels.get(CurrentreceiptModelIndex).setRemainnum((int)(receiptModels.get(CurrentreceiptModelIndex).getRemainnum()-barCodeInfo.getQty()));
        }
        return isChecked;
    }

    boolean Addbarcode(int index,BarCodeInfo barCodeInfo){
        float qty= ArithUtil.add(receiptDetailModels.get(index).getScanQty(),barCodeInfo.getQty());
        if(qty<=receiptDetailModels.get(index).getRemainQty()) {
            receiptDetailModels.get(index).getLstBarCode().add(0, barCodeInfo);
            receiptDetailModels.get(index).setBatchNo(barCodeInfo.getBatchNo());
            receiptDetailModels.get(index).setScanQty(qty);
            return true;
        }else{
            MessageBox.Show(context, getString(R.string.Error_ReceiveOver));
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbillpost, menu);
        gMenuItem=menu.findItem(R.id.action_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            boolean flag=false;
            ArrayList<ReceiptDetail_Model> modelpost=new ArrayList<>();
            if (receiptModels!=null && receiptModels.size() != 0) {
                for (Receipt_Model model:receiptModels) {
                    List<ReceiptDetail_Model> modeldetails =  model.getLstDetail();
                    if(modeldetails!=null&&modeldetails.size()!=0){
                        for (ReceiptDetail_Model modeldetail:modeldetails) {
                            modelpost.add(modeldetail);
                            if(modeldetail.getLstBarCode()!=null&&modeldetail.getLstBarCode().size()>0){
                                flag=true;
                            }
                        }
                    }
                }
                if(flag){
                    final Map<String, String> params = new HashMap<String, String>();
                    String ModelJson = parseModelToJson(modelpost);
                    String UserJson = parseModelToJson(BaseApplication.userInfo);
                    params.put("UserJson", UserJson);
                    params.put("ModelJson", ModelJson);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Msg_Post, getString(R.string.Msg_Post), context, mHandler, RESULT_Post, null,  URLModel.GetURL().SaveT_InStockDetailADF, params, null);
                }else{
                    MessageBox.Show(context,"请先扫描收货条码！");
                }

//                if(isCancelFilterButton){
//                    isCancelFilterButton=false;
//                    txtSuppliername.setText(getResources().getString(R.string.supplierNoFilter));
//                    item.setTitle(getResources().getString(R.string.filter));
//                    BindListVIew(receiptModels);
//                }else {
//                    for(int i=0;i<receiptModels.size();i++) {
//                        Map<String, String> map = new HashMap<String, String>();
//                        String SupplierName = receiptModels.get(i).getSupplierName();
//                        String SupplierID = receiptModels.get(i).getSupplierNo();
//                        map = new HashMap<String, String>();
//                        map.put("SupplierName", SupplierName == null || SupplierName.isEmpty() ? "空" : SupplierName);
//                        map.put("SupplierID", SupplierID == null || SupplierID.isEmpty() ? "000000" : SupplierID);
//                        SupplierList.add(map);
//                    }
//                    Intent intent = new Intent(context, SupplierFilterActivity.class);
//                    Bundle bundle = new Bundle();
//                    ArrayList bundlelist = new ArrayList();
//                    bundlelist.add(SupplierList);
//                    bundle.putParcelableArrayList("SupplierList", bundlelist);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, supplierRequestCode);
//                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 供应商筛选界面返回值接收
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==supplierRequestCode && resultCode==RESULT_OK){
            String SupplierID=data.getStringExtra("SupplierID");
            String SupplierName=data.getStringExtra("SupplierName");
            txtSuppliername.setText(SupplierName);
            ArrayList<Receipt_Model> receiptModelList= new ArrayList<>();
            for (  Receipt_Model tempreceiptModel:receiptModels ) {
                if(tempreceiptModel.getSupplierNo()!=null && tempreceiptModel.getSupplierNo().equals(SupplierID) && tempreceiptModel.getSupplierName().equals(SupplierName)){
                    receiptModelList.add(tempreceiptModel);
                }
           }
            isCancelFilterButton=true;
            if(gMenuItem!=null ){
                gMenuItem.setTitle(getResources().getString(R.string.title_Receipt_RightFilter));
            }
            BindListVIew(receiptModelList);
        }
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Receipt_Model receiptModel=(Receipt_Model) receiptcgBillChioceItemAdapter.getItem(position);
        StartScanIntent(receiptModel,null);
     }

    @Event(value = R.id.edt_filterContent,type = View.OnKeyListener.class)
    private  boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
                String code = edtfilterContent.getText().toString().trim();
                //扫描箱条码
                final Map<String, String> params = new HashMap<String, String>();
                params.put("BarCode", code);
                params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
                LogUtil.WriteLog(CGReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, code);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCode, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
                return false;
//            CommonUtil.setEditFocus(edtfilterContent);
        }
        return false;
    }


    void AnalysisGetT_PostJson(String result){
        try {
            LogUtil.WriteLog(CGReceiptBillChoice.class, TAG_GetT_InStockList, result);
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            MessageBox.Show(context,returnMsgModel.getMessage());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                receiptModels= new ArrayList<Receipt_Model>();
                BindListVIew(receiptModels);
            }
        }catch (Exception ex){
            MessageBox.Show(context,ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }



    void AnalysisGetT_InStockListJson(String result){
        try {
            LogUtil.WriteLog(CGReceiptBillChoice.class, TAG_GetT_InStockList, result);
            ReturnMsgModelList<Receipt_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Receipt_Model>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                ArrayList<Receipt_Model> currentreceiptModels =  returnMsgModel.getModelJson();
                if(receiptModels.indexOf(currentreceiptModels.get(0))==-1){
                    //不在表头中，添加入列表中

                    //判断同一个采购订单和同一个部门
                    for(int i=0;i<receiptModels.size();i++){
                        if(!receiptModels.get(i).getSupplierNo().equals(currentreceiptModels.get(0).getSupplierNo())){
                            MessageBox.Show(context,"不属于同一个供应商！");
                            return;
                        }
                        if(!receiptModels.get(i).getDepartmentCode().equals(currentreceiptModels.get(0).getDepartmentCode())){
                            MessageBox.Show(context,"不属于同一个部门！");
                            return;
                        }
                    }

                    receiptModels.add(currentreceiptModels.get(0));
                }

                if (returnMsgModel.getHeaderStatus().equals("S")) {
                    ArrayList<BarCodeInfo> barCodeInfos = this.barCodeInfos;
                    Bindbarcode(barCodeInfos);
                    BindListVIew(receiptModels);
                } else {
                    MessageBox.Show(context,returnMsgModel.getMessage());
                    PlayVoice.PlayError(context);
                }



//                String code=edtfilterContent.getText().toString().trim();
//                final Map<String, String> params = new HashMap<String, String>();
//                params.put("BarCode", code);
//                params.put("UserJson", parseModelToJson(BaseApplication.userInfo));
//                LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, code);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null,  URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        }catch (Exception ex){
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }

    void AnalysisGetT_PalletDetailByBarCodeJson(String result) {
        LogUtil.WriteLog(CGReceiptBillChoice.class, TAG_GetT_PalletDetailByBarCode, result);
        try {
            ReturnMsgModelList<BarCodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<BarCodeInfo>>() {
            }.getType());
            if (returnMsgModel.getHeaderStatus().equals("S")) {
                this.barCodeInfos = returnMsgModel.getModelJson();
                if (barCodeInfos != null) {
                    //判断条码是否同一个采购订单
                    String CGNo=barCodeInfos.get(0).getErpVoucherNo();
                    for(int i=0;i<barCodeInfos.size();i++){
                        if(!barCodeInfos.get(i).getErpVoucherNo().equals(CGNo)){
                            MessageBox.Show(context,"获取的条码不能所属与多个采购订单！");
                            return;
                        }
                    }
                    Receipt_Model receiptModel = new Receipt_Model();
                    receiptModel.setStatus(1);
                    receiptModel.setErpVoucherNo(barCodeInfos.get(0).getErpVoucherNo());
                    GetT_InStockList(receiptModel);
                }
            } else {
                ToastUtil.show(returnMsgModel.getMessage());
            }
        } catch (Exception ex) {
            ToastUtil.show(ex.getMessage());
        }
        CommonUtil.setEditFocus(edtfilterContent);
    }
    void GetT_InStockList(Receipt_Model receiptModel){
        try {
            String ModelJson = GsonUtil.parseModelToJson(receiptModel);
            Map<String, String> params = new HashMap<>();
            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.userInfo));
            params.put("ModelJson", ModelJson);
            LogUtil.WriteLog(CGReceiptBillChoice.class, TAG_GetT_InStockList, ModelJson);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_CGInStockList, null,  URLModel.GetURL().GetT_CGInStockListADF, params, null);
        } catch (Exception ex) {
//            mSwipeLayout.setRefreshing(false);
            MessageBox.Show(context, ex.getMessage());
            MessageBox.Show(context, ex.getMessage());
        }
    }

    void StartScanIntent(Receipt_Model receiptModel,ArrayList<BarCodeInfo> barCodeInfo){
        Intent intent=new Intent(context,CGReceiptionScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiptModel",receiptModel);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    private void BindListVIew(ArrayList<Receipt_Model> receiptModels) {
        receiptcgBillChioceItemAdapter=new ReceiptcgBillChioceItemAdapter(context,receiptModels);
        lsvChoiceReceipt.setAdapter(receiptcgBillChioceItemAdapter);
    }


}

