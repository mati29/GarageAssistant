package main.java.CarPackage;

/**
 * Created by Mati on 2016-12-28.
 */
public class SinglePart {
    String value;
    int index;

    public SinglePart(){}

    public SinglePart(String value,int index){
        this.value = value;
        this.index = index;
    }
    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public int getIndex(){
        return this.index;
    }

    public void setIndex(int index){
        this.index = index;
    }
}
