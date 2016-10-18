package com.example.septaa2;

public class Train {
	private String dpTime;
	private String avTime;
	private String trainno;
	
	//creating an empty construtor for train
	public Train(){
		dpTime=null;
		avTime=null;
		trainno=null;
	}
	
	//creating a construtor that contains destination and surce
	public Train(String dpTime, String avTime, String trainno){
		this.dpTime=dpTime;
		this.avTime = avTime;
		this.trainno=trainno;
	}

	public String getDpTime() {
		return dpTime;
	}

	public String getAvTime() {
		return avTime;
	}
	public String getTrainno(){
		return trainno;
	}


	
	//get the class variables..


}
