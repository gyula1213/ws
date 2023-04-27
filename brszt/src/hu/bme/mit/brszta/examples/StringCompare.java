package hu.bme.mit.brszta.examples;

public class StringCompare {

    public static void main(String[] args) {
        String s1 = "alma";
        String s2 = "alma";
        String s3 = new String("alma");
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1.equals(s3));
    }
}
