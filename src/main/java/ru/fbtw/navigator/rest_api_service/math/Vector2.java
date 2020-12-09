package ru.fbtw.navigator.rest_api_service.math;

public class Vector2 {
	public static final double DEFAULT_NORMALIZE_X = 25;
	public static final double DEFAULT_NORMALIZE_Y = 25;
	private double x;
	private double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public static void normalizeLine(Vector2 origin,Vector2 end){
		if(Math.abs(origin.x-end.x)<= DEFAULT_NORMALIZE_X){
			end.x = origin.x;
		}
		if(Math.abs(origin.y-end.y)<= DEFAULT_NORMALIZE_Y){
			end.y = origin.y;
		}
	}
	public Vector2 subtract(Vector2 second){
		return new Vector2(second.x-this.x,second.y-this.y);
	}

	public Vector2 add(Vector2 second){
		return new Vector2(this.x+second.x,this.y+second.y);
	}

	public double sqrMaginitude(){
		return Math.sqrt(x*x+y*y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vector2 zero(){
		return new Vector2(0,0);
	}


}
