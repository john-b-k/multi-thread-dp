package com.multi.ch4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
/**
 * 
 * @author doyoon
 * - Data는 상태(content), 상태변경여부 플래그 변수가있다
 * - Saver는 규칙적으로 저장시도
 * - Changer는 Data stateChaingMethod로 데이터 변경시도함
 * - Balking조건 만족시(이미 저장되었다면) save는 바로리턴됨 
 * 
 * - doSave가 정말 실행되는 부분 주의깊게 봄
 */
public class BalkingExam {
	public static void main(String [] args){
		Data data = new Data("data.txt","");
		new ChangerThread("Changer",data).start();
		new SaverThread("Saver",data).start();
	}
}

//Guarded Object
class Data{
	private final String filename;	//저장하는 파일명
	private String content;
	private boolean changed;
	
	public Data(String filename, String content){
		this.filename = filename;
		this.content = content;
		this.changed = true;
	}
	//stateChnagingMethod
	public synchronized void change(String newContent){
		content = newContent;
		changed = true;
	}
	//guardedMethod
	public synchronized void save() {
		if(changed == false){
			return;
		}
		try {
			doSave();
		} catch (IOException e) {
		}
		changed = false;
	}
	
	private void doSave() throws IOException{
		System.out.println(Thread.currentThread().getName()+" calls doSave, content = "+content);
		Writer writer = new FileWriter(filename);
		writer.write(content);
		writer.close();
	}
}

class SaverThread extends Thread{
	private final Data data;
	
	public SaverThread(String name, Data data){
		super(name);
		this.data = data;
	}
	
	public void run(){
		while(true){
			try {
				Thread.currentThread().sleep((int)Math.random()*1000);
			} catch (InterruptedException e) {
			}
			data.save();
		}
	}
}

class ChangerThread extends Thread {
	
	private final Data data;
	
	public ChangerThread(String name, Data data){
		super(name);
		this.data = data;
	}
	
	public void run(){
		int content = 0;
		while(true){
			data.change(""+content++);
			try {
				Thread.currentThread().sleep((int)Math.random()*1000);
			} catch (InterruptedException e) {
			}
			data.save();
		}
	}
}