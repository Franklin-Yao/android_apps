package com.yao.activity;
import java.util.ArrayList;
import java.util.List;

import com.example.yao.R;
import com.yao.dao.FlagDAO;
import com.yao.model.Tb_flag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Button mAddBtn;
	private ArrayList<String> mNotes;
	private ListView mNoteListView;
	private ArrayAdapter<String> mlistAdapter;
	final FlagDAO flagDAO = new FlagDAO(this);// 创建FlagDAO对象
	private AdapterView.OnItemClickListener mOnItemClickListener=
			new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) 
				{
					if(arg0.getId()==R.id.no_listview)
					{
						Intent intent=new Intent();
						intent.setClass(MainActivity.this,note.class);
						intent.putExtra("position", arg2);
						intent.putExtra("content", flagDAO.findTitle(arg2+1));					
						intent.putExtra("title",String.valueOf(((TextView) arg1).getText()));				
						startActivityForResult(intent,0);
					}
				}
	};
	private OnClickListener mOnClickListener=new OnClickListener(){
		public void onClick(View v)
		{			
				Intent intent=new Intent(MainActivity.this, note.class);
				startActivityForResult(intent,0);
		}
	};
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	  
	private void init()
	{
		mAddBtn=(Button)findViewById(R.id.add);
		mAddBtn.setOnClickListener(mOnClickListener);
		mNotes=new ArrayList<String>();
		mNoteListView=(ListView)findViewById(R.id.no_listview);
		showInfo();// 显示收入信息
		mlistAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,mNotes);
		//mNoteListView.setAdapter(mlistAdapter);
		mNoteListView.setOnItemClickListener(mOnItemClickListener);
	}
	
	protected void onRestart() {
        super.onRestart();// 实现基类中的方法
        showInfo();// 显示收入信息
    }
	protected void onDestroy() {
        super.onDestroy();// 实现基类中的方法
        flagDAO.close();
    }
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null)
		{
			int position=data.getIntExtra("position", -1);
			String content=data.getStringExtra("content");
			String title=data.getStringExtra("title");
			switch(resultCode)
			{
			case RESULT_CANCELED:
				if(position!=-1)
				{
					mNotes.remove(position);
					mlistAdapter.notifyDataSetChanged();
					flagDAO.detele(position+1);// 根据指定的id删除便签信息
			        Toast.makeText(MainActivity.this, "〖便签数据〗删除成功！", Toast.LENGTH_SHORT).show();
				}				
				break;
			case RESULT_OK:
				if(position!=-1)
				{
					mNotes.set(position,content);
					Tb_flag tb_flag = new Tb_flag();// 创建Tb_flag对象
			        tb_flag.setid(position+1);// 设置便签id
			        tb_flag.setFlag(content);// 设置便签值
			        tb_flag.setTitle(title);
			        flagDAO.update(tb_flag);// 修改便签信息
			        // 弹出信息提示
			        Toast.makeText(MainActivity.this, "〖便签数据〗修改成功！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					mNotes.add(title);
					Tb_flag tb_flag = new Tb_flag();// 创建Tb_flag对象
			        tb_flag.setid(flagDAO.getMaxId()+1);// 设置便签id
			        tb_flag.setFlag(content);// 设置便签值
			        tb_flag.setTitle(title);
			        flagDAO.add(tb_flag);// 修改便签信息
			        // 弹出信息提示
			        Toast.makeText(MainActivity.this, "〖便签数据〗添加成功！", Toast.LENGTH_SHORT).show();
				}
				mlistAdapter.notifyDataSetChanged();
				break;	
			default:
				break;
			}
		}
	}
	
	private void showInfo() {// 用来根据传入的管理类型，显示相应的信息
	    String[] strInfos = null;// 定义字符串数组，用来存储收入信息
	    ArrayAdapter<String> arrayAdapter = null;// 创建ArrayAdapter对象
	    FlagDAO flag = new FlagDAO(MainActivity.this);// 创建InaccountDAO对象
	    // 获取所有收入信息，并存储到List泛型集合中
	    List<Tb_flag> listinfos = flag.getScrollData(0, (int) flag.getCount());
	    strInfos = new String[listinfos.size()];// 设置字符串数组的长度
	    int m = 0;// 定义一个开始标识
	    for (Tb_flag tb_flag : listinfos) {// 遍历List泛型集合
	        // 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
	        strInfos[m] = tb_flag.getTitle();
	        mNotes.add(tb_flag.getTitle());
	        m++;// 标识加1
	    }
	    // 使用字符串数组初始化ArrayAdapter对象
	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strInfos);
	    mNoteListView.setAdapter(arrayAdapter);// 为ListView列表设置数据源
	}
}
