package com.parsclass.android.alltolearn.Utils;


import java.util.ArrayList;

public class MapPosition {

    private ArrayList<Integer> countEachSection=new ArrayList<>();
    private ArrayList<Integer> start;
    private ArrayList<Integer> end=new ArrayList<>();
    private int totalSize;
    private int position;
    public String TAG="MapPosition";

    public MapPosition(ArrayList<Integer> start,int totalSize) {
        this.start = start;
        this.totalSize=totalSize;
        setCountEachSection();
        setEnd();
    }

    public MapPosition() {
    }

    public void setStart(ArrayList<Integer> start) {
        this.start = start;
        this.totalSize=start.size();
        setCountEachSection();
        setEnd();
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    private void setEnd(){
        for (int i=0;i<start.size();i++){
            end.add(start.get(i)+countEachSection.get(i));
        }
    }

    private void setCountEachSection(){
        for (int i=0;i<start.size();i++){
            if(i==start.size()-1){
                countEachSection.add(totalSize-start.get(i)-1);
            }
            else if (i<start.size() && i!=start.size()-1){
                int j=i+1;
                countEachSection.add(start.get(j)-start.get(i)-1);
            }

        }
    }

    private void calculatePosition(int currentWindow){

        for (int i=0;i<start.size();i++){
            if( currentWindow>=start.get(i)-i && currentWindow<=end.get(i)-i-1 ){
                this.position=currentWindow+i+1;
            }
        }
    }

    public int getPosition(int currentWindow) {
        calculatePosition(currentWindow);
        return position;
    }
}
