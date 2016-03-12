package com.example.tangyangkai.ebear.utils;

public class Calculate {
	public static float Average(float[] num){//平均值的计算
		float average=0.0f;
		for(int i=0;i<num.length;i++){
			average=average+num[i];
		}
		return average/num.length;
		
	}
	
	public static float Variance(float[] num){ //方差的计算
		float variance=0.0f;
		for(int i=0;i<num.length;i++){
			variance=variance+(num[i]-75)*(num[i]-75);
		}
		return variance/num.length;
		
	}
	
	public static int calculateAge(String birthday,int month,int year){
		int age=0;
		System.out.println("Tmonth:"+month+"birthday.substring(5,7):"+birthday.substring(5,7)+"Tyear:"+year+"birthday.substring(0,4):"+birthday.substring(0,4));
		if(month+1-Integer.parseInt(birthday.substring(5,7))>0){
			age=year-Integer.parseInt(birthday.substring(0,4))+1;
		}else{
			age=year-Integer.parseInt(birthday.substring(0,4));
		}
		return age;
		
	}

}
