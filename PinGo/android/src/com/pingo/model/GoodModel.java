package com.pingo.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class GoodModel implements Serializable 
{
	private String name;
	private String weight;
	private float newPrice;
	private float oldPrice;
	private Bitmap pic;
	public GoodModel(String name,String weight,float newPrice,float oldPrice,Bitmap pic){
		this.name=name;
		this.weight=weight;
		this.newPrice=newPrice;
		this.oldPrice=oldPrice;
		this.pic=pic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public float getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}
	public float getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}
	public Bitmap getPic() {
		return pic;
	}
	public void setPic(Bitmap pic) {
		this.pic = pic;
	}
}