package com.yao.activity;
import com.example.yao.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class note extends Activity 
{
	private EditText mContentEdit;
	private EditText mTitleEdit;
	private OnClickListener mOnClickListener=new OnClickListener()
	{
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.no_delete:
					setResult(RESULT_CANCELED,getIntent());
					finish();
				case R.id.no_save:
					String content=mContentEdit.getText()+"";
					String title=mTitleEdit.getText()+"";
					if((!TextUtils.isEmpty(content))&&(!TextUtils.isEmpty(content)))
					{
						Intent intent=getIntent();
						intent.putExtra("content",content);
						intent.putExtra("title",title);
						setResult(RESULT_OK,intent);
						finish();
					}
					else
						Toast.makeText(note.this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
					break;
				default:
						break;
			}
		}
	};
	private void init()
	{
		Button mSaveBtn=(Button)findViewById(R.id.no_save);	
		Button mDelBtn=(Button)findViewById(R.id.no_delete);	
		mContentEdit=(EditText)findViewById(R.id.no_content);
		mTitleEdit=(EditText)findViewById(R.id.no_title);
		Intent intent=getIntent();
		String content=intent.getStringExtra("content");
		String title=intent.getStringExtra("title");
		mSaveBtn.setOnClickListener(mOnClickListener);
		mDelBtn.setOnClickListener(mOnClickListener);
		if(!TextUtils.isEmpty(content))
		{
			mContentEdit.setText(content);
		}
		if(!TextUtils.isEmpty(title))
		{
			mTitleEdit.setText(title);
		}
	}
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemnote);
		init();
	}
}
