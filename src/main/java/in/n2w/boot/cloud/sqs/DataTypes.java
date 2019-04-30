package in.n2w.boot.cloud.sqs;

/**
 * Created by Karanbir Singh on 4/30/2019.
 **/
public enum DataTypes {

    STRING("String"),
    NUMBER("Number"),
    BINARY("Binary");

    private String dataType;

    DataTypes(String dataType){
        this.dataType = dataType;
    }

    public String getDataType(){
        return this.dataType;
    }

}
