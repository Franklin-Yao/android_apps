package com.pingo.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.pingo.activity.R;
import com.pingo.widget.PickerView.onSelectListener;

public class TimePickerPopWindow implements OnDismissListener, OnClickListener {

	// TODO Auto-generated constructor stub
	PickerView data_pv;
	PickerView time_pv;
	int hour;
	int minute;
	List<String> data = new ArrayList<String>();
	List<String> seconds = new ArrayList<String>();
	private PopupWindow popupWindow;
	private OnItemClickListener listener;

	public TimePickerPopWindow(Context context) {
		// TODO Auto-generated constructor stub

		View view = LayoutInflater.from(context).inflate(
				R.layout.timepicker_popwindow, null);

		data_pv = (PickerView) view.findViewById(R.id.data_pv);
		time_pv = (PickerView) view.findViewById(R.id.time_pv);

		Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		if (hour > 20) {
			data.clear();
			data.add("明天");
		} else {
			data.clear();
			data.add("今天");
			data.add("明天");
		}

		for (int i = 9; i < 22; i++) {
			String time = String.format("%d:00-%d:00", i, i + 1);
			seconds.add(time);
		}
		data_pv.setData(data);
		data_pv.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				// Toast.makeText(MainActivity.this, "选择了 " + text +
				// " 分",Toast.LENGTH_SHORT).show();
				if (text == "今天") {
					// try {
					// URL url = new
					// URL("http://open.baidu.com/special/time/");// 取得资源对象
					// URLConnection uc = url.openConnection();// 生成连接对象
					// uc.connect(); // 发出连接
					// long ld = uc.getDate(); // 取得网站日期时间
					// Date date = new Date(ld); // 转换为标准时间对象
					// int minute = date.getMinutes();
					// int hour = date.getHours();
					// if (minute < 30) {
					// seconds.clear();
					// for (int i = hour; i < 21; i++) {
					// String time = String.format("%d:30-%d:30",i,i+1);
					// seconds.add(time);
					// }
					// }
					// else {
					// seconds.clear();
					// for (int i = hour + 1; i < 22; i++) {
					// String time = String.format("%d:00-%d:00",i,i+1);
					// seconds.add(time);
					// }
					// }
					// } catch (Exception e) {
					// // TODO: handle exception
					// Log.e("GETTIME", "gettime error");
					// }
					//
					// Time time = new Time("GMT+8");
					// time.setToNow();
					if (hour < 9) {
						for (int i = 9; i < 22; i++) {
							String time = String
									.format("%d:00-%d:00", i, i + 1);
							seconds.add(time);
						}
					}

					else {
						if (minute < 30) {

							seconds.clear();
							for (int i = hour; i < 21; i++) {
								String timearea = String.format("%d:30-%d:30",
										i, i + 1);
								seconds.add(timearea);

							}

						} else {
							seconds.clear();
							for (int i = hour + 1; i < 22; i++) {
								String timearea = String.format("%d:00-%d:00",
										i, i + 1);
								seconds.add(timearea);

							}
							Log.e("SETTIME", "settime 2");
						}
						Log.e("SETTIME", String.format("hour = %d,minute = %d",
								hour, minute));
					}
				}

				else {
					seconds.clear();
					for (int i = 9; i < 22; i++) {
						String time = String.format("%d:00-%d:00", i, i + 1);
						seconds.add(time);
					}
				}

			}
		});
		time_pv.setData(seconds);
		time_pv.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
			}
		});
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub

	}

	/** 设置监听 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	public interface OnItemClickListener {
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop();
	}

	/** 弹窗显示的位置 */
	public void showAsDropDown(View parent) {
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();

	}

	/** 消除弹窗 */
	public void dissmiss() {
		popupWindow.dismiss();
	}

}
