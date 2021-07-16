package ir.hadinemati.steelmarketing.Lib;

import java.util.ArrayList;
import java.util.List;

public class StringSplitHelpera {

    public static List<String> SplitString(String data  , int partsCount){
        if(partsCount > data.length())
            throw new RuntimeException("invalid parts count");
        int splitLength = (int) Math.floor(data.length() / partsCount);
       // System.out.println(splitLength);
        List<String> parts = new ArrayList<>();
            for( int i=0;i<(int)Math.floor(data.length() / splitLength);i++){
                if((i+1)*splitLength <= data.length())
                    parts.add(data.substring(i*splitLength , (i+1)*splitLength));
            }

            // remaining
            int remainingLength = data.length() % splitLength;

            if(remainingLength > 0)
                parts.add(data.substring(data.length()-remainingLength , data.length() ));


        return parts;
    }

}
