package chinetek.xx.chntwms.adapter.wms.Receiption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.Receiption.Receipt_Model;


/**
 * Created by GHOST on 2017/1/13.
 */

public class ReceiptcgBillChioceItemAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<Receipt_Model> receiptModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器
    private int selectItem = -1;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
       // public TextView txtSupplierName;
        public TextView txtERPVoucherNo;
        public TextView txtStrVoucherType;
        public TextView txtCompany;
        public TextView txtdepartment;
        public TextView txtSupplierCode;
        public TextView txtSupplier;
    }

    public ReceiptcgBillChioceItemAdapter(Context context, List<Receipt_Model> receiptModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptModels = receiptModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return receiptModels==null?0: receiptModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_receiptcgbillchoice_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
           // listItemView.txtSupplierName = (TextView) convertView.findViewById(R.id.txtSupplierName);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtStrVoucherType = (TextView) convertView.findViewById(R.id.txtStrVoucherType);
            listItemView.txtCompany = (TextView) convertView.findViewById(R.id.txtCompany);
            listItemView.txtdepartment = (TextView) convertView.findViewById(R.id.txtdepartment);
            listItemView.txtSupplierCode = (TextView) convertView.findViewById(R.id.txtSupplierCode);
            listItemView.txtSupplier = (TextView) convertView.findViewById(R.id.txtSupplier);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        Receipt_Model receiptModel=receiptModels.get(selectID);
        listItemView.txtTaskNo.setText(receiptModel.getErpVoucherNo());
      //  listItemView.txtSupplierName.setText(receiptModel.getSupplierName());
        listItemView.txtERPVoucherNo.setText("剩余数："+receiptModel.getRemainnum());
//        listItemView.txtStrVoucherType.setText(receiptModel.getStrVoucherType());
        listItemView.txtStrVoucherType.setText(receiptModel.getVoucherNo());
        listItemView.txtCompany.setText("扫描数："+receiptModel.getScannum());//receiptModel.getStrongHoldName()
        listItemView.txtdepartment.setText(receiptModel.getDepartmentName());
        listItemView.txtSupplierCode.setText(receiptModel.getSupplierNo());
        listItemView.txtSupplier.setText(receiptModel.getSupplierName());
//        if (selectItem == position) {
//            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
//        }else{
//            convertView.setBackgroundResource(R.color.trans);
//        }
//
        if (receiptModel.getRemainnum()==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
