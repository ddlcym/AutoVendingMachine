package com.doing.flat.coffee;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.doing.flat.coffee.socket.SendTcpData;
import com.doing.flat.coffee.socket.TcpDataFac;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by cym on 2017/11/16.
 */


public class TestProductsDialog extends Dialog {
    private Context context;
    private GridView gv;
    private int[] imageIds = {R.mipmap.test_binghongcha, R.mipmap.test_bsklg, R.mipmap.test_cococle,
            R.mipmap.test_cococlegz, R.mipmap.test_fenda, R.mipmap.test_guolicheng,
            R.mipmap.test_nfsq, R.mipmap.test_xuebigz, R.mipmap.test_yibao};
    private Handler parentHandler;
    public static final int delayDismiss = 1000;
    public static final int shipment = 1001;
    public static final int delayTime = 10000;

    public TestProductsDialog(Context context, Handler testHandler) {
        super(context, R.style.Translucent_NoTitle);
        setContentView(R.layout.test_sell_dialog);
        this.context = context;
        this.parentHandler = testHandler;
        initView();
        initData();
    }

    private void initView() {
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        window.setGravity(Gravity.CENTER);
        gv = (GridView) findViewById(R.id.gv);

    }

    private void initData() {



        MyAdapter myAdapter=new MyAdapter(context);
        gv.setAdapter(myAdapter);
        myAdapter.setData(imageIds);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //发送数据

                if (parentHandler.hasMessages(delayDismiss)) {
                    parentHandler.removeMessages(delayDismiss);
                }
                parentHandler.sendEmptyMessageDelayed(delayDismiss, delayTime);

                Message msg = new Message();
                msg.what = shipment;
                msg.arg1 = i + 1;
                parentHandler.sendMessage(msg);


                //发送数据到银联服务器
//                byte sData[]= TcpDataFac.getInstance().getSignData();
//                byte sData[]= TcpDataFac.getInstance().getExpendData();
//                new SendTcpData().sendTCPData(sData);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private int[] myData;
        private LayoutInflater mInflater = null;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public void setData(int[] list) {
            this.myData = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return myData != null && myData.length > 0 ? myData.length : 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;

            if (view == null) {
                view = mInflater.inflate(R.layout.test_gvprod_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.test_gvitem_iv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ImageLoader.getInstance().displayImage("drawable://"+myData[i],holder.iv);

            return view;
        }

        class ViewHolder {
            ImageView iv;
        }

    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
