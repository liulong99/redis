
/** 
* Java中将一个字符与对应Ascii码互转 
* 1 byte = 8bit 可以表示 0-127 
*/ 
public class GetCharAscii { 
 
/*0-9对应Ascii 48-57 
*A-Z 65-90 
*a-z 97-122 
*第33～126号(共94个)是字符，其中第48～57号为0～9十个阿拉伯数字 
*/ 
    public static void main(String[] args) {
        toBinary("abc");
    }



    /**
     * 将指定byte数组以16进制的形式打印到控制台
     *
     * @param hint
     *            String
     * @param b
     *            byte[]
     * @return void
     */
    public static void printHexString(String hint, byte[] b) {
        System.out.print(hint);
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
        }
        System.out.println("");
    }

    /**
     *
     * @param b
     *            byte[]
     * @return String
     */
    public static String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += " 0x" + hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"–> 0xEF
     *
     * @param src0
     *            byte
     * @param src1
     *            byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF,
     * 0xD9}
     *
     * @param src
     *            String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }


    public static void toBinary(String str){
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }
        System.out.println(result);
    }
 
/** 
* 方法一：将char 强制转换为byte 
* @param ch 
* @return 
*/ 
public static byte charToByteAscii(char ch){ 
byte byteAscii = (byte)ch; 
 
return byteAscii; 
} 
/** 
* 方法二：将char直接转化为int，其值就是字符的ascii 
* @param ch 
* @return 
*/ 
public static byte charToByteAscii2(char ch){ 
byte byteAscii = (byte)ch; 
 
return byteAscii; 
} 
/** 
* 同理，ascii转换为char 直接int强制转换为char 
* @param ascii 
* @return 
*/ 
public static char byteAsciiToChar(int ascii){ 
char ch = (char)ascii; 
return ch; 
} 
/** 
* 求出字符串的ASCII值和 
* 注意，如果有中文的话，会把一个汉字用两个byte来表示，其值是负数 
*/ 
public static int SumStrAscii(String str){ 
byte[] bytestr = str.getBytes(); 
int sum = 0; 
for(int i=0;i<bytestr.length;i++){ 
sum += bytestr[i]; 
} 
return sum; 
} 
}