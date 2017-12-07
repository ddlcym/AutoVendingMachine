package com.doing.flat.coffee.utils;

/**
 * Created by cym on 2017/12/4.
 */

public class UnionPayUtils {

//Asicc与字符互转

    //String转ascii
    public String strsToAscii(String str){

        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }
        return hex.toString();
    }

//    public String convertHexToString(String hex){
//
//        StringBuilder sb = new StringBuilder();
//        StringBuilder temp = new StringBuilder();
//
//        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
//        for( int i=0; i<hex.length()-1; i+=2 ){
//
//            //grab the hex in pairs
//            String output = hex.substring(i, (i + 2));
//            //convert hex to decimal
//            int decimal = Integer.parseInt(output, 16);
//            //convert the decimal to character
//            sb.append((char)decimal);
//
//            temp.append(decimal);
//        }
//
//        return sb.toString();
//    }

    //解析收到的数据转为String
    public static String asciiToString(String value)
    {
        if(null==value) return null;
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(" ");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}
