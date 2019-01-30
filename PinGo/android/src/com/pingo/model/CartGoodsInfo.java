package com.pingo.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class CartGoodsInfo implements Parcelable{
	private int id;
	private int goods_id;
	private int product_id;
	private String product_name;
	private String pdt_desc;
	private int product_num;
	private double price;
	private Bitmap goods_img;

	public CartGoodsInfo() {

	}

	public CartGoodsInfo(int id, int goods_id, int product_id,String product_name, String pdt_desc, int product_num,
			float price,Bitmap goods_img) {
		this.id = id;
		this.goods_id = goods_id;
		this.product_id = product_id;
		this.product_name = product_name;
		this.pdt_desc = pdt_desc;
		this.product_num = product_num;
		this.price = price;
		this.goods_img = goods_img;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGoodsId(int goods_id) {
		this.goods_id = goods_id;
	}

	public void setProductId(int product_id) {
		this.product_id = product_id;
	}

	public void setProductName(String product_name) {
		this.product_name = product_name;
	}

	public void setProductDsec(String pdt_desc) {
		this.pdt_desc = pdt_desc;
	}

	public void setProductNum(int product_num) {
		this.product_num = product_num;
	}
	public void setGoodsImg(Bitmap goods_img) {
		this.goods_img = goods_img;
	}


	public void setPrice(Double price) {
		this.price = price;
	}
	
	public int getId() {
		return id;
	}

	public int getGoodsId() {
		return goods_id;
	}

	public int getProductId() {
		return product_id;
	}

	public String getProductName() {
		return product_name;
	}

	public String getProductDsec() {
		return pdt_desc;
	}

	public int getProductNum() {
		return product_num;
	}

	public double getPrice() {
		return price;
	}
	public Bitmap getGoodsImg() {
		return goods_img;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<CartGoodsInfo> CREATOR = new Creator<CartGoodsInfo>() { 
        public CartGoodsInfo createFromParcel(Parcel source) { 
        	CartGoodsInfo goodsInfo = new CartGoodsInfo(); 
        	goodsInfo.id = source.readInt(); 
        	goodsInfo.goods_id = source.readInt(); 
        	goodsInfo.product_id = source.readInt(); 
        	goodsInfo.product_name = source.readString(); 
        	goodsInfo.pdt_desc = source.readString(); 
        	goodsInfo.product_num = source.readInt(); 
        	goodsInfo.price = source.readDouble(); 
        	
        	goodsInfo.goods_img = Bitmap.CREATOR.createFromParcel(source);
            return goodsInfo; 
        } 
        public CartGoodsInfo[] newArray(int size) { 
            return new CartGoodsInfo[size]; 
        } 
    }; 
    
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(goods_id);
		dest.writeInt(product_id);
		dest.writeString(product_name);
		dest.writeString(pdt_desc);
		dest.writeInt(product_num);
		dest.writeDouble(price);
		goods_img.writeToParcel(dest, 0);
	}

}
