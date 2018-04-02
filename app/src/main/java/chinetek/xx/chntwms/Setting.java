package chinetek.xx.chntwms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chinetek.xx.chntwms.base.BaseActivity;
import chinetek.xx.chntwms.base.BaseApplication;
import chinetek.xx.chntwms.base.ToolBarTitle;
import chinetek.xx.chntwms.model.URLModel;
import chinetek.xx.chntwms.util.Network.RequestHandler;
import chinetek.xx.chntwms.util.SharePreferUtil;
import chinetek.xx.chntwms.util.dialog.LoadingDialog;
import chinetek.xx.chntwms.util.dialog.MessageBox;
import chinetek.xx.chntwms.util.dialog.ToastUtil;
import chinetek.xx.chntwms.util.function.CommonUtil;


@ContentView(R.layout.activity_setting)
public class Setting extends BaseActivity {

    Context context=Setting.this;

    @ViewInject(R.id.edt_IPAdress)
    EditText edtIPAdress;
    @ViewInject(R.id.edt_Port)
    EditText edtPort;
    @ViewInject(R.id.edt_TimeOut)
    EditText edtTimeOut;
    @ViewInject(R.id.edt_PrintIP)
    EditText edtPrintIP;
    @ViewInject(R.id.edt_ElecIP)
    EditText edtElecIP;
    @ViewInject(R.id.rb_WMS)
    RadioButton rbWMS;
    @ViewInject(R.id.rb_Product)
    RadioButton rbProduct;

    final  int LogUploadIndex=1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.login_setting),true);
        x.view().inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        BaseApplication.DialogShowText = getString(R.string.Msg_UploadLogFile);
        SharePreferUtil.ReadShare(context);
        edtIPAdress.setText(URLModel.IPAdress);
        edtPort.setText(URLModel.Port+"");
        edtPrintIP.setText(URLModel.PrintIP);
        edtElecIP.setText(URLModel.ElecIP);
//        edtIPAdress.setEnabled(false);
//        edtPort.setEnabled(false);
        if(URLModel.isWMS) rbWMS.setChecked(true); else rbProduct.setChecked(true);
        edtTimeOut.setText(RequestHandler.SOCKET_TIMEOUT/1000+"");
        CommonUtil.setEditFocus(edtPrintIP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusetting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            final LoadingDialog dialog = new LoadingDialog(context);
            dialog.show();
            String url="http://"+ URLModel.IPAdress+":"+URLModel.Port+"/UpLoad.ashx";
            File[] files = new File(Environment.getExternalStorageDirectory()+"/wmshht/").listFiles();
            final List<File> list= Arrays.asList(files);
            Collections.sort(list, new FileComparator());

            for(int i=0;i<LogUploadIndex;i++) {
                final  int index=i;
                RequestParams params = new RequestParams(url);
                params.setMultipart(true);
                params.addBodyParameter("file", new File(list.get(list.size()-i-1).getAbsolutePath()));
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        //加载成功回调，返回获取到的数据
                        if(index==LogUploadIndex-1) {
                            ToastUtil.show(result);
                        }
                    }

                    @Override
                    public void onFinished() {
                        if(index==LogUploadIndex-1) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastUtil.show(ex.toString());
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public class FileComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            if(file1.getName().compareTo(file2.getName())<1)
            {
                return -1;
            }else
            {
                return 1;
            }
        }
    }

    @Event(R.id.btn_SaveSetting)
    private void btnSetting(View view){
        String IPAdress=edtIPAdress.getText().toString().trim();
        String PrintIp=edtPrintIP.getText().toString().trim();
        String ElecIP=edtElecIP.getText().toString().trim();
        Integer Port=Integer.parseInt(edtPort.getText().toString().trim());
        Integer TimeOut=Integer.parseInt(edtTimeOut.getText().toString().trim())*1000;
        if(CommonUtil.MatcherIP(ElecIP)){//CommonUtil.MatcherIP(IPAdress) &&
            SharePreferUtil.SetShare(context,IPAdress,PrintIp,ElecIP,Port,TimeOut,rbWMS.isChecked());
            new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setMessage(getResources().getString(R.string.SaveSuccess)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   closeActiviry();
                }
            }).show();
        }else{
            MessageBox.Show(context,getResources().getString(R.string.Error_Setting_IPAdressError));
            CommonUtil.setEditFocus(edtIPAdress);
        }
    }
}
