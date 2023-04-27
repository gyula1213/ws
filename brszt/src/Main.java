import hu.bme.mit.brszta.data.Truck;

import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {


//        BufferedReader reader = null;
//        BufferedWriter writer = null;
//        try {
//            reader = new BufferedReader(
//                    new InputStreamReader(
//                            new FileInputStream("input.txt")));
//            String line = reader.readLine();
//            writer = new BufferedWriter(
//                    new OutputStreamWriter(
//                            new FileOutputStream("output.txt")));
//            writer.write(line);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        ObjectOutputStream oos = null;
        try {
            Truck truck = new Truck("BRSZT-01");
            truck.drive(100);
            oos = new ObjectOutputStream(new FileOutputStream("truck.txt"));
            oos.writeObject(truck);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("truck.txt"));
            Truck t = (Truck)ois.readObject();
            System.out.println(t.getOdometer());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
