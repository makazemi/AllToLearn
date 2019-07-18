package com.parsclass.android.alltolearn.Utils;

import java.util.ArrayList;

public class CalculatePosition {

    private ArrayList<Integer> count;
    private ArrayList<Integer> start=new ArrayList<>();
    private ArrayList<Integer> end=new ArrayList<>();
    private int index;
    private int childIndex;
    private int parentIndex;

    public CalculatePosition(ArrayList<Integer> count, int index) {
        this.count = count;
        this.index = index;
        initStartEnd();
        setIndex();
    }

    private void initStartEnd(){
        int sum=0;

        for (int i=0;i<count.size();i++){
            for (int j=0;j<i;j++){
                sum+=count.get(j);

            }
            //start.set(i,sum);
            start.add(sum);
            end.add(start.get(i)+count.get(i)-1);
            sum=0;
            //end.set(i,start.get(i)+count.get(i)-1);

        }
    }

    private void setIndex(){
        int sum=0;
        for (int i=0;i<start.size();i++){
            if( index>= start.get(i) && index<= end.get(i)){
                for (int j=0;j<i;j++){
                    sum+=count.get(j);
                }
                this.parentIndex=i;
                this.childIndex=index-sum;
            }
        }
    }

    public int getChildIndex() {
        return childIndex;
    }

    public int getParentIndex() {
        return parentIndex;
    }
}
