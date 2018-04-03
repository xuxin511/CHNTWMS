package chinetek.xx.chntwms.cywms.InnerMove;

import android.content.Context;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import chinetek.xx.chntwms.adapter.wms.Receiption.ReceiptBillDetailAdapter;
import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Material.BarCodeInfo;

@ContentView(R.layout.activity_inner_move_detail)
public class InnerMoveDetail extends BaseActivity {

    Context context = InnerMoveDetail.this;
    @ViewInject(R.id.lsv_InnerMoveList)
    ListView lsvInnerMoveList;

    ReceiptBillDetailAdapter receiptDetailAdapter;
    ArrayList<BarCodeInfo> barCodeInfos;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        x.view().inject(this);
        barCodeInfos=getIntent().getParcelableArrayListExtra("barCodeInfos");
        bindListview();
    }


    void bindListview(){
        if(barCodeInfos!=null){
            receiptDetailAdapter = new ReceiptBillDetailAdapter(context, barCodeInfos);
            lsvInnerMoveList.setAdapter(receiptDetailAdapter);
        }

    }
}
