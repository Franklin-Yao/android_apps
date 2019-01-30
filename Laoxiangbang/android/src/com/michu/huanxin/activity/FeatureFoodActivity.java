package com.michu.huanxin.activity;

import com.michu.pingju.activity.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FeatureFoodActivity extends BaseActivity implements OnClickListener{
	
	//widget
	private LinearLayout featureIntroLin,featureOneLin,featureTwoLin
	,featureThreeLin,featureForeLin,featureRightLin;
	private ImageView featureOneImg,featureTwoImg,featureThreeImg,featureFourImg;
	private ListView nearFeatureLv;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_featurefood);
		initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		featureIntroLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_featureintro);
		featureOneLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_featureone);
		featureTwoLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_featuretwo);
		featureThreeLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_featurethree);
		featureForeLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_featurefour);
		featureRightLin = (LinearLayout)findViewById(R.id.activity_featurefood_ll_rightarrow);
		 
		featureIntroLin.setOnClickListener(this);
		featureOneLin.setOnClickListener(this);
		featureTwoLin.setOnClickListener(this);
		featureThreeLin.setOnClickListener(this);
		featureForeLin.setOnClickListener(this);
		featureRightLin.setOnClickListener(this);
		
		featureOneImg = (ImageView)findViewById(R.id.activity_featurefood_img_featureone);
		featureTwoImg = (ImageView)findViewById(R.id.activity_featurefood_img_featuretwo);
		featureThreeImg = (ImageView)findViewById(R.id.activity_featurefood_img_featurethree);
		featureFourImg = (ImageView)findViewById(R.id.activity_featurefood_img_featurefour);
		
		nearFeatureLv = (ListView)findViewById(R.id.activity_featurefood_lst_nearfeature);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.activity_featurefood_ll_featureintro:
			break;
		case R.id.activity_featurefood_ll_featureone:
			break;
		case R.id.activity_featurefood_ll_featuretwo:
			break;
		case R.id.activity_featurefood_ll_featurethree:
			break;
		case R.id.activity_featurefood_ll_featurefour:
			break;
		case R.id.activity_featurefood_ll_rightarrow:
			break;
		default:
			break;
		}
		
	}

}
