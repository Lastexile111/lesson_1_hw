package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        FileReader fr= new FileReader("Persons.txt");
        Scanner scan = new Scanner(fr);
       // Class person = Person.class;

        int i = 1;

        while (scan.hasNextLine()) {
//          System.out.println(i + " : " + scan.nextLine());
            Person pers = new Person();
            Class person = pers.getClass();

            String line = scan.nextLine();
            char[] chArray = line.toCharArray();

            if(chArray.length != 0){

                int firstIndex = 0;
                int finalIndex = 0;
                int count = 0;
                for(int j = 0; j < chArray.length; j++) {
                    //находим пробел
                    if ((chArray[j] == ' ')||(j == chArray.length-1 )) {
                        finalIndex = j;

                        if(count == 0) {
                            Field arg1Field = person.getDeclaredField("arg1");
                            arg1Field.setAccessible(true);
                            arg1Field.set(pers, line.substring(firstIndex, finalIndex) );

                        }

                        if(count == 1) {
                            Field arg2Field = person.getDeclaredField("arg2");
                            arg2Field.setAccessible(true);
                            arg2Field.set(pers, line.substring(firstIndex, finalIndex));
                        }

                        if(count == 2) {
                            Field arg3Field = person.getDeclaredField("arg3");
                            arg3Field.setAccessible(true);
                            arg3Field.set(pers, line.substring(firstIndex, chArray.length));
                        }
                        count += 1;
                        firstIndex = finalIndex;
                    }

                }
                Person newPerson = (Person) person.newInstance();
                final Constructor constructor = person.getDeclaredConstructor();
                final Object  o = constructor.newInstance();
                System.out.println(i +" > " + pers.toString());
            }



            i++;
        }


        fr.close();



    }
}
