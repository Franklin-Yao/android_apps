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
	final FlagDAO flagDAO = new FlagDAO(this);// ����FlagDAO����
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
		showInfo();// ��ʾ������Ϣ
		mlistAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,mNotes);
		//mNoteListView.setAdapter(mlistAdapter);
		mNoteListView.setOnItemClickListener(mOnItemClickListener);
	}
	
	protected void onRestart() {
        super.onRestart();// ʵ�ֻ����еķ���
        showInfo();// ��ʾ������Ϣ
    }
	protected void onDestroy() {
        super.onDestroy();// ʵ�ֻ����еķ���
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
					flagDAO.detele(position+1);// ����ָ����idɾ����ǩ��Ϣ
			        Toast.makeText(MainActivity.this, "����ǩ���ݡ�ɾ���ɹ���", Toast.LENGTH_SHORT).show();
				}				
				break;
			case RESULT_OK:
				if(position!=-1)
				{
					mNotes.set(position,content);
					Tb_flag tb_flag = new Tb_flag();// ����Tb_flag����
			        tb_flag.setid(position+1);// ���ñ�ǩid
			        tb_flag.setFlag(content);// ���ñ�ǩֵ
			        tb_flag.setTitle(title);
			        flagDAO.update(tb_flag);// �޸ı�ǩ��Ϣ
			        // ������Ϣ��ʾ
			        Toast.makeText(MainActivity.this, "����ǩ���ݡ��޸ĳɹ���", Toast.LENGTH_SHORT).show();
				}
				else
				{
					mNotes.add(title);
					Tb_flag tb_flag = new Tb_flag();// ����Tb_flag����
			        tb_flag.setid(flagDAO.getMaxId()+1);// ���ñ�ǩid
			        tb_flag.setFlag(content);// ���ñ�ǩֵ
			        tb_flag.setTitle(title);
			        flagDAO.add(tb_flag);// �޸ı�ǩ��Ϣ
			        // ������Ϣ��ʾ
			        Toast.makeText(MainActivity.this, "����ǩ���ݡ���ӳɹ���", Toast.LENGTH_SHORT).show();
				}
				mlistAdapter.notifyDataSetChanged();
				break;	
			default:
				break;
			}
		}
	}
	
	private void showInfo() {// �������ݴ���Ĺ������ͣ���ʾ��Ӧ����Ϣ
	    String[] strInfos = null;// �����ַ������飬�����洢������Ϣ
	    ArrayAdapter<String> arrayAdapter = null;// ����ArrayAdapter����
	    FlagDAO flag = new FlagDAO(MainActivity.this);// ����InaccountDAO����
	    // ��ȡ����������Ϣ�����洢��List���ͼ�����
	    List<Tb_flag> listinfos = flag.getScrollData(0, (int) flag.getCount());
	    strInfos = new String[listinfos.size()];// �����ַ�������ĳ���
	    int m = 0;// ����һ����ʼ��ʶ
	    for (Tb_flag tb_flag : listinfos) {// ����List���ͼ���
	        // �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
	        strInfos[m] = tb_flag.getTitle();
	        mNotes.add(tb_flag.getTitle());
	        m++;// ��ʶ��1
	    }
	    // ʹ���ַ��������ʼ��ArrayAdapter����
	    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strInfos);
	    mNoteListView.setAdapter(arrayAdapter);// ΪListView�б���������Դ
	}
}
