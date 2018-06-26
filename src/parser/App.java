package parser;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/*
У вас есть файл с данными. Допустим persons.txt. В этом файле содержится информация о каком-то персонаже.
Допустим, в виде:
 One Two Three
 Four Five Six
Формат строк одинаковый. Также у вас есть класс Person с тремя полями (поля private) и переопределенным toString().
Вам нужно, считать файл и с помощью reflection создать объект этого класса и установить значия в поля.
Разделитель между данными в файле - любой.
Предусмотреть, что  в файле могут быть пустые строки - тогда объект создавать не нужно.
 */
public class App {

    public static void main(String[] args) throws IllegalAccessException,  IOException{
        FileReader fr= new FileReader("Persons.txt");
        Scanner scan = new Scanner(fr);


        while (scan.hasNextLine()) {

            Person newPerson = new Person();
            Class classOfPerson = newPerson.getClass();


            String line = scan.nextLine();
            char[] chArray = line.toCharArray();

            if(chArray.length != 0){

                int firstIndex = 0;
                int finalIndex = 0;

                int j;
                Field[] fields = classOfPerson.getDeclaredFields();
                for (Field field : fields) {
                    for( j = finalIndex ; j < chArray.length; j++) {
                        //находим пробел
                        if ((chArray[j] == ' ')||(j == chArray.length-1 )) {
                            j++;
                            finalIndex = j;

                            field.setAccessible(true);
                            field.set(newPerson, line.substring(firstIndex, finalIndex) );

                            firstIndex = finalIndex;

                            break;
                        }

                    }

                }
                System.out.println(newPerson);
                System.out.println(toString(newPerson));

            }
        }


        fr.close();



    }
/*
У вас есть статический метод toString(Object object). Он принимает в качестве параметра любой объект и отдает вам строку.
Т.е. смысл у него такой же как у обычного toString. Но, вам нужно, чтобы этот метод получал все поля переданного объекта,
проверял, какой тип у этого поля и если это примитивный тип, строка или массив, то тогда добавлял в результирующую строку,
иначе - уходил бы в рекурсию.
Т.е. вы проверяете тип каждого поля. И если примитив, строка, массив или коллекция, то добавляете его в result.
Иначе, если это какой-то другой объект, вы должны его передать в рекурсивный вызов метода toString(Object obj).
Так мы обойдем все объекты, из которых состоит первоначальный и выведем все поля на экран.
 */
    public static String toString(Object object) throws IllegalAccessException {
        Class classOfObject = object.getClass();
        String result = "> " + object.getClass().getName() + ": ";



        Field[] fields = classOfObject.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            result += field.getName() + " = ";
            if (field.getType().isPrimitive() == true) {

//                if (field.get(object) != null){
//                    continue;
//                }
                result += field.get(object);

            }else if(field.getType().isArray() == true){

                int length = Array.getLength(field.get(object));

                for (int j=0; j < length; j++) {
                    result += "[ "+ Array.get(field.get(object), j) + " ]";
                }

            }else if(field.getType().getInterfaces().equals(List.class)){
                result += "[ "+ field.get(object)  + " ]";
            }else if(field.getType().getInterfaces().equals(Queue.class)){
                result += "[ "+ field.get(object)  + " ]";
            }else if(field.getType().getInterfaces().equals(Set.class)){
                result += "[ "+ field.get(object)  + " ]";
            }else if(field.getType().getInterfaces().equals(Map.class)){
                Map objMap = (Map)field.get(object);
                for (Object key : objMap.keySet()){

                    result += "[ Key: " + key + ", value: " + objMap.get(key)  + " ]";

                }

            }else{
                result += "[ "+ field.get(object).toString() + " ]";
            }

            result += "; ";
        }


        return result;
    }
}
