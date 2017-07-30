package com.java.xdd.test.call;

/**
 * Created by Administrator on 2017/7/30.
 */
public class CallbackMainTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Student student=new Student();
        student.setCallback(new Teacher());
        student.doTask();
    }
}

class Student {

    Callback callback=null;

    //将老师的联系信息给学生
    public void setCallback(Callback callback) {
        this.callback=callback;
    }

    public void doTask() {
        for(int m=1;m<6;m++) {
            callback.taskResult(m+"是张三");
        }
        System.out.println("OK");
    }
}

/**
 * 学生必须指导老师的信息，才能回报任务情况，因此它必须实现回调接口
 *
 */
class Teacher implements Callback{

    @Override
    public void taskResult(String name) {
        // TODO Auto-generated method stub
        System.out.println("任务:"+name+"完成");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

interface Callback {
    void taskResult(String name);
}
