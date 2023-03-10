package com.xeecos.motionmaker;

import static android.graphics.Bitmap.createBitmap;

import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
import android.graphics.Color;
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
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int[] pixels = new int[width * height];
                        int[] grays = new int[width*height];
                        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                        for(int y=0;y<height;y++)
                        {
                            for(int x=0;x<width;x++)
                            {
                                int idx = y*width+x;
                                grays[idx] = (int)(((pixels[idx]&0xff0000)>>16)*0.3+((pixels[idx]&0xff00)>>8)*0.6+(pixels[idx]&0xff)*0.1);
                            }
                        }
                        Bitmap bm= createBitmap (width,height, Bitmap.Config.ARGB_8888);
                        int r,g,b;
                        for(int y=1,ylen = height-1;y<ylen;y++)
                        {
                            for(int x=1,xlen=width-1;x<xlen;x++)
                            {
                                int idx = y*width+x;;
                                if(y%2==0)
                                {
                                    if(x%2==1)
                                    {
                                        r = (grays[idx-1]+grays[idx+1])>>1;
                                        g = grays[idx];
                                        b = (grays[idx-width]+grays[idx+width])>>1;
                                    }
                                    else
                                    {
                                        r = grays[idx];
                                        g = (grays[idx-1]+grays[idx+1])>>1;
                                        b = (grays[idx-width-1]+grays[idx+width-1]+grays[idx-width+1]+grays[idx+width+1])>>2;
                                    }
                                }
                                else
                                {
                                    if(x%2==1)
                                    {
                                       r = (grays[idx-width-1]+grays[idx+width-1]+grays[idx-width+1]+grays[idx+width+1])>>2;
                                       g = (grays[idx-width]+grays[idx+width])>>1;
                                       b = grays[idx];
                                    }
                                    else
                                    {
                                        r = (grays[idx-width]+grays[idx+width])>>1;
                                        g = grays[idx];
                                        b = (grays[idx-1]+grays[idx+1])>>1;
                                    }
                                }
                                bm.setPixel(x,y, Color.argb(0xff,r,g,b));
                            }
                        }
                        Message msg = Message.obtain();
                        msg.obj = bm;
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