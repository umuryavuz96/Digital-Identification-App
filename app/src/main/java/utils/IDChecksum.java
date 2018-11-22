package utils;


import java.util.Arrays;

public class IDChecksum {

    public IDChecksum(){

    }

    public boolean validify(String id){
        //validify

        String[] chars2=id.split("");
        String[] chars = Arrays.copyOfRange(chars2,1,chars2.length);

        boolean isElevenDigits = (chars.length==11);
        boolean isFirstDigitZero =(chars[0]=="0");

        int x = 7*(Integer.parseInt(chars[0])+ Integer.parseInt(chars[2])+Integer.parseInt(chars[4])+Integer.parseInt(chars[6])+Integer.parseInt(chars[8]));
        int y = (Integer.parseInt(chars[1])+ Integer.parseInt(chars[3])+Integer.parseInt(chars[5])+Integer.parseInt(chars[7])+Integer.parseInt(chars[9]));

        boolean canGetDigitTen  = (((x-y)%10) == Integer.parseInt(chars[9]));
        int sum =0;
        for (int i =0; i<chars.length-1; i++){
            sum=sum+ Integer.parseInt(chars[i]);
        }
        boolean canGetDigitEleven= (sum%10 == Integer.parseInt(chars[10]));
        boolean isLastDigitEven = (Integer.parseInt(chars[10])%2==0);
        if(isElevenDigits&&!isFirstDigitZero&&isLastDigitEven&&canGetDigitTen&&canGetDigitEleven){
            return true;
        }
        return false;
    }
}
