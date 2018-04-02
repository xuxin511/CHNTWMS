package chinetek.xx.chntwms.cywms.UpShelf;

import android.content.Context;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import chinetek.xx.chntwms.R;
import chinetek.xx.chntwms.adapter.wms.Receiption.ReceiptBillDetailAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.model.Receiption.ReceiptDetail_Model;

/*
没有用
 */
@ContentView(R.layout.activity_upshelf_bill_detail)
public class UpshelfBillDetail extends BaseActivity {

    Context context = UpshelfBillDetail.this;
    @ViewInject(R.id.lsvUpshelfDetail)
    ListView  lsvUpshelfDetail;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.UpShelfscan_billdetail), true);
        x.view().inject(this);
        List<ReceiptDetail_Model> receiptDetailModels=getData();
        ReceiptBillDetailAdapter receiptScanDetailAdapter=new ReceiptBillDetailAdapter(context,receiptDetailModels.get(0).getLstBarCode());
        lsvUpshelfDetail.setAdapter(receiptScanDetailAdapter);
    }

    List<ReceiptDetail_Model> getData(){
        List<ReceiptDetail_Model> receiptDetailModels=new ArrayList<>();
        for(int i=0;i<10;i++){
            ReceiptDetail_Model receiptDetailModel=new ReceiptDetail_Model();
            receiptDetailModel.setMaterialNo("123455"+i);
            receiptDetailModel.setMaterialDesc("物料描述"+i);
            receiptDetailModel.setScanQty(1f);
            receiptDetailModel.setCompany("据点");
            receiptDetailModel.setDepartment("部门");
            receiptDetailModels.add(receiptDetailModel);
        }
        return receiptDetailModels;
    }
}
