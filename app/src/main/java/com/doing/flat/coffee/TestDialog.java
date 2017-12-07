package com.doing.flat.coffee;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.doing.flat.coffee.socket.SendTcpData;
import com.doing.flat.coffee.socket.TcpDataFac;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by FT on 2017/11/19.
 */

public class TestDialog extends Dialog {

    public final static int SendMSG=1000;
    public final static int Dismiss_Delay =1001;
    public final static int DelayTime=10000;


    private GridView gv;
    private Context context;
    private Handler pHandler;
    private int[] myData = {R.mipmap.testys1, R.mipmap.testys2, R.mipmap.testys3, R.mipmap.testys4, R.mipmap.testys5, R.mipmap.testys6, R.mipmap.testys7, R.mipmap.testys8, R.mipmap.testys9,};

    public TestDialog(Context context, Handler hand) {
        super(context, R.style.Translucent_NoTitle);
        this.context = context;
        this.pHandler = hand;
        initView();
        initData();
    }

    private void initView() {

        setContentView(R.layout.test_dialog);

        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);


        gv = (GridView) findViewById(R.id.test_gv);
        MyAdapter myAdapter = new MyAdapter(context);
        gv.setAdapter(myAdapter);
        myAdapter.setData(myData);

    }

    private void initData(){
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Message msg=new Message();
                msg.what=SendMSG;
                msg.arg1=i;
                pHandler.sendMessage(msg);
                if(pHandler.hasMessages(Dismiss_Delay)){
                pHandler.removeMessages(Dismiss_Delay);
                }
                pHandler.sendEmptyMessageDelayed(Dismiss_Delay,DelayTime);

                //发送数据到银联服务器
                byte sData[]= TcpDataFac.getInstance().getSignData();
                new SendTcpData().sendTCPData(sData);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private int[] pData=myData;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        public void setData(int[] pData) {
            this.pData = pData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return pData != null && pData.length > 0 ? pData.length : 0;
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.test_dialog_item, null);

                viewHolder.iv = (ImageView) convertView.findViewById(R.id.test_iv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage("drawable://" + pData[i], viewHolder.iv);

            return convertView;
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
    public void cancel() {
        super.cancel();
    }
}
