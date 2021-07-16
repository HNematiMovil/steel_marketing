package ir.hadinemati.steelmarketing.Lib;

public class Constants {
    public final static String sharedPreferenceName = "SteelMarketingSharedData";
    public final static  String phoneNumber = "phoneNumber";
    public final static  String userName = "userName";
    public final static  String DateTime = "DateTime";
    public final static  String DatabaseName = "MarketingDatabase";

    public final static String isCallIncoming = "isCallIncoming";
    public final static String IgnoredContacts = "IgnoredContacts";
    public final static String ProductsList = "ProductsList";
    public final static String product = "product";

    public final static String ShowDialog = "ShowDialog";
    public final static String OrderList = "OrderList";
















    public final static String pKey = "78650279118cdf5054981adcd2c45c4983d59a211ed816dc63353b02138d695a0dbb1cddc71f6698";
    public final static String pSecret = "6cff479b4805eafba7c7aaa937eb67b280a38bda5130f3553d854856d687d04e8b93d143148235f2";
    public final static String CypherKey= "q323k0c5438OO5nM";
    public final static String CypherIV = "C4zv6XcU7O0Mxh27";




    public final static String BaseUrl="https://marketing.steel-man.ir/api/";

    public final static String keySecret = String.format("/key/%s/secret/%s/username/Farhad",pKey,pSecret);


    public static String getPostUrl(String method){
        return BaseUrl+method+keySecret;
    }

}
