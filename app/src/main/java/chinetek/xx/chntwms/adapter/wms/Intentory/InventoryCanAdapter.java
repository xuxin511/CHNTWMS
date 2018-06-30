package chinetek.xx.chntwms.adapter.wms.Intentory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import chinetek.xx.chntwms.cywms.R;
import chinetek.xx.chntwms.model.WMS.Inventory.Barcode_Model;
import chinetek.xx.chntwms.model.check.CheckAnalyze;


/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryCanAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private ArrayList<CheckAnalyze> CheckAnalyzes; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtSerialno;
        public TextView txtMaterialno;
        public TextView txtMaterialdesc;
        public TextView txtWarehouseno;
        public TextView txtRemark;
        public TextView txtQty;
        public TextView txtSQty;
    }

    public InventoryCanAdapter(Context context, ArrayList<CheckAnalyze> CheckAnalyzes) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.CheckAnalyzes = CheckAnalyzes;

    }

    @Override
    public int getCount() {
        return CheckAnalyzes==null?0:CheckAnalyzes.size();
    }

    @Override
    public Object getItem(int position) {
        return CheckAnalyzes.get(position);
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
            convertView = listContainer.inflate(R.layout.item_inventorycan_listview,null);
            listItemView.txtSerialno = (TextView) convertView.findViewById(R.id.txtSerialno);
            listItemView.txtMaterialno = (TextView) convertView.findViewById(R.id.txtMaterialno);
            listItemView.txtMaterialdesc = (TextView) convertView.findViewById(R.id.txtMaterialdesc);
            listItemView.txtWarehouseno = (TextView) convertView.findViewById(R.id.txtWarehouseno);
            listItemView.txtRemark = (TextView) convertView.findViewById(R.id.txtRemark);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            listItemView.txtSQty = (TextView) convertView.findViewById(R.id.txtSQty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        CheckAnalyze CheckAnalyzemodel=CheckAnalyzes.get(selectID);
        listItemView.txtSerialno.setText(CheckAnalyzemodel.getSERIALNO());
        listItemView.txtWarehouseno.setText(CheckAnalyzemodel.getWarehouseno());
        listItemView.txtMaterialno.setText(CheckAnalyzemodel.getMATERIALNO());
        listItemView.txtMaterialdesc.setText(CheckAnalyzemodel.getMATERIALDESC());
        listItemView.txtRemark.setText(CheckAnalyzemodel.getRemark());
        listItemView.txtQty.setText("数量："+CheckAnalyzemodel.getQTY()+"");
        listItemView.txtSQty.setText("扫描数："+CheckAnalyzemodel.getSQTY()+"");

        if (CheckAnalyzemodel.getRemark().equals("赢")) {
//            listItemView.txtRemark.setBackgroundColor(1);
            convertView.setBackgroundResource(R.color.orangered);
        }
        if (CheckAnalyzemodel.getRemark().equals("亏")) {
            convertView.setBackgroundResource(R.color.green);
        }
        if (CheckAnalyzemodel.getRemark().equals("数量赢")) {
            convertView.setBackgroundResource(R.color.deeppink);
        }
        if (CheckAnalyzemodel.getRemark().equals("数量亏")) {
            convertView.setBackgroundResource(R.color.aquamarine);
        }
        if (CheckAnalyzemodel.getRemark().equals("平")) {
            convertView.setBackgroundResource(R.color.white);
        }


        return convertView;
    }


}
