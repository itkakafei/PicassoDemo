package testsdcard.cai.maiyu.picassodemo;

import android.app.ProgressDialog;
import android.content.Entity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import testsdcard.cai.maiyu.picassodemo.entity.NewsItem;
import testsdcard.cai.maiyu.picassodemo.utils.PicassoUtils;

/**
 * Created by maiyu on 2017/6/7.
 */

public class ListViewActivity extends AppCompatActivity{

    //定义ListView
    private ListView listView ;
    //定义进度条窗口
    private ProgressDialog dialog ;

    private MyAdapter adapter ;

    private static final String TAG = "ListViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);

        //初始化界面
        initView();

        //listView.setOnScrollListener(new ListScroller());
        //创建进度条窗口对象
        dialog = new ProgressDialog(this);
        //设置窗口标题
        dialog.setTitle("loading......");

        new MyTask().execute("http://litchiapi.jstv.com/api/GetFeeds?column=17&PageSize=20&pageIndex=1&val=AD908EDAB9C3ED111A58AF86542CCF50");
    }


    /**
     * 初始化布局
     */
    private void initView() {
        listView    =   (ListView)findViewById(R.id.listView);
    }


    /**
     * 自定义OnScrollListener类
     */
    public class  ListScroller implements AbsListView.OnScrollListener{

        /**
         * 下滑状态改变时调用
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //创建Picasso
            final Picasso picasso = Picasso.with(ListViewActivity.this);
            //判断状态
            if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL){
                //更新
                picasso.resumeTag(ListViewActivity.this);
            }else {
                //暂停
                picasso.pauseTag(ListViewActivity.this);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    /**
     * 自定义适配器
     */
    class MyAdapter extends BaseAdapter{

        //List<NewsItem>集合
        private List<NewsItem> data;

        /**
         * 初始化适配器
         * @param data
         */
        public MyAdapter(List<NewsItem> data){
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //创建MyViewHolder
            MyViewHolder holder = null;

            //一次
            if(convertView == null){
                holder = new MyViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.my_adapter_layout , parent , false);
                holder.cover = (ImageView)convertView.findViewById(R.id.cover_img);
                holder.subject = (TextView)convertView.findViewById(R.id.subject);
                holder.summary = (TextView)convertView.findViewById(R.id.summary);
                convertView.setTag(holder);
                Log.d(TAG , "convertView:" +position);
            }else {
                holder = (MyViewHolder)convertView.getTag();
            }

            //分别设置图片，主题，内容
            holder.subject.setText(data.get(position).getSubject());
            //Log.d(TAG , data.get(position).getSubject());

            holder.summary.setText(data.get(position).getSummary());
            //Log.d(TAG , data.get(position).getSummary());

            PicassoUtils.loadImageWithSize(ListViewActivity.this , "http://litchiapi.jstv.com" + data.get(position).getCover(),
                    400,300,holder.cover);

            return convertView;
        }
    }

    /**
     * MyViewHolder类
     */
    private static class MyViewHolder{
        ImageView cover;
        TextView subject;
        TextView summary;
    }


    /**
     * 异步任务实现：String,void,返回类型：一个list<NewsItem>集合
     */
    class MyTask extends AsyncTask<String , Void , List<NewsItem>>{

        /**
         * 准备执行前：显示窗口进度条
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //进度条窗口显示
            dialog.show();
        }

        @Override
        protected List<NewsItem> doInBackground(String... params) {

            //创建HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            //创建HttpGet
            HttpGet httpGet = new HttpGet(params[0]);
            //创建返回对象
            HttpResponse response = null;

            //创建List<NewsItem>集合
            List<NewsItem> list = new ArrayList<>();

            try {
                //获取返回结果对象Response
                response = httpClient.execute(httpGet);

                //判断是否请求成功
                if(response.getStatusLine().getStatusCode() == 200){

                    //获取HttpEntity对象
                    HttpEntity entity =  response.getEntity();
                    String json_value = EntityUtils.toString(entity , "utf-8");

                    //根据返回数据进行解析：
                    //http://litchiapi.jstv.com/api/GetFeeds?column=17&PageSize=20&pageIndex=1&val=AD908EDAB9C3ED111A58AF86542CCF50
                    JSONArray jsonArray = new JSONObject(json_value)
                            .getJSONObject("paramz").getJSONArray("feeds");

                    //获取每一条数据
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject element = jsonArray.getJSONObject(i).getJSONObject("data");
                        NewsItem item = new NewsItem();
                        item.setCover(element.getString("cover"));
                        item.setSubject(element.getString("subject"));
                        item.setSummary(element.getString("summary"));
                        list.add(item);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return list;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            super.onPostExecute(newsItems);

            //完成时初始化MyAdapter
            adapter = new MyAdapter(newsItems);
            //为listView设置MyAdapter
            listView.setAdapter(adapter);
            //进度条窗口取消
            dialog.cancel();
        }
    }
}
