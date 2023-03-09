package com.xeecos.motionmaker;

import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Handler;
        import android.os.Message;
        import android.util.AttributeSet;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class EzImageView extends ImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"network error",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"service error",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public EzImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EzImageView(Context context) {
        super(context);
    }

    public EzImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(final String path) {
        new Thread() {
            @Override
            public void run() {
                try {
                      URL url = new URL(path);
                      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                      connection.setRequestMethod("GET");
                      connection.setConnectTimeout(10000);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

}