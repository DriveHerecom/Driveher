package com.yukti.facerecognization.localdatabase;

import java.io.Serializable;

public class TaskDetails implements Serializable {
	
	private String tid ;
	private String type ;
	
//	public TaskDetails(JSONArray taskArray)
//	{
//		
//		try {
//			 for (int i = 0; i < taskArray.length(); i++) {
//				 
//				 try {
//					JSONObject obj = taskArray.getJSONObject(i);
//					
//					try {
//						setTid(obj.getString(LoginJsonParams.tid));
//					} catch (Exception e) {
//						setTid(""); 
//					}
//					
//					try {
//						setType(obj.getString(LoginJsonParams.type)); 
//					} catch (Exception e) {
//						setType("");
//					}
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				 
//			 }
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
	
	
	public String getTid() {
		return tid;
	}

	public String getType() {
		return type;
	}
	
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public void setType(String type) { 
		this.type = type;
	}

}
