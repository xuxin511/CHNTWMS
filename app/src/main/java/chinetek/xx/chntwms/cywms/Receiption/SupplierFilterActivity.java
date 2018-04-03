package chinetek.xx.chntwms.cywms.Receiption;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.cywms.R;

@ContentView(R.layout.activity_supplier_filter)
public class SupplierFilterActivity extends BaseActivity {

    Context context = SupplierFilterActivity.this;
    @ViewInject(R.id.lsvSupplier)
    ListView lsvSupplier;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle( getString(R.string.supplier_subtitle), true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ArrayList list= getIntent().getParcelableArrayListExtra("SupplierList");
        //从List中将参数转回 List<Map<String, Object>>
        List<Map<String, String>> SupplierList= (List<Map<String, String>>)list.get(0);
        Set<Map<String, String>> setMap = new HashSet<Map<String, String>>();
        setMap.addAll(SupplierList);
        SupplierList.clear();
        SupplierList.addAll(setMap);
        SimpleAdapter adapter = new SimpleAdapter(this, SupplierList, android.R.layout.simple_list_item_2, // List
                // 显示两行item1、item2
                new String[] { "SupplierName", "SupplierID" }, new int[] { android.R.id.text1, android.R.id.text2 });
        lsvSupplier.setAdapter(adapter);

    }

    @Event(value = R.id.lsvSupplier,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map=(HashMap<String,String>)lsvSupplier.getItemAtPosition(position);
        String SupplierID=map.get("SupplierID");
        String SupplierName=map.get("SupplierName");
        Intent mIntent = new Intent();
        mIntent.putExtra("SupplierID",SupplierID);
        mIntent.putExtra("SupplierName",SupplierName);
        setResult(RESULT_OK, mIntent);
        closeActiviry();
    }
}
