package models;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Item {
	
	private static final int SIZE_ITEM = 60;
	private int id;
	private Image imgItem;
	private Point pointItem;
	private int size;
	private Rectangle recItem;
	
	public Item(int id, Image imgItem, Point pointItem) {
		this.id = id;
		this.imgItem = imgItem;
		this.pointItem = pointItem;
		this.size = SIZE_ITEM;
		this.recItem = new Rectangle(pointItem.x, pointItem.y, SIZE_ITEM, SIZE_ITEM);
	}
	
	public int getId() {
		return id;
	}

	public Image getImgItem() {
		return imgItem;
	}

	public Point getPointItem() {
		return pointItem;
	}

	public int getSize() {
		return size;
	}

	public Rectangle getRecItem() {
		return recItem;
	}
}