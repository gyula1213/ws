package hu.bme.mit.brszta.examples;

public class IntegerCompare {

    public static void main(String[] args) {
        Integer a = new Integer(2);
        Integer b = a;
        System.out.println(b == a);
        a++;
        a--;
        System.out.println(b == a);
    }
}
