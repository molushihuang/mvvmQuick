package com.xqd.mvvmquick.javatest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: XieQD
 * @Date: 2022/9/20 16:36
 * @Description: Powered by GWM
 */

public class TreeSetDemo {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Set<Person> list = new TreeSet<>(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                int name = o1.getName().compareTo(o2.getName());
                if (name == 0) {
                    int age = o1.getAge().compareTo(o2.getAge());
                    if (age == 0) {
                        if (o2.getPhoto() == null ) {
                            return age;
                        }
                        return o1.getPhoto().compareTo(o2.getPhoto());
                    }
                    return age;
                }
                return name;
            }
        });

//        Set<Person> list = new TreeSet<>(Comparator.comparing(o -> o.getName() + "" +
//            (o.getPhoto() == null ? "" : o.getPhoto())
//        ));
        List<Person> ts = new ArrayList<Person>();
        ts.add(new Person("zhangsan", 20));
        ts.add(new Person("lisi", 22));
        ts.add(new Person("wangwu", 21));
        ts.add(new Person("zhouqi", 29));
        ts.add(new Person("zhaoliu", 35));
        ts.add(new Person("zhaoliu", 35, "photo"));

        list.addAll(ts);

        List<Person> ts3 = new ArrayList<>(list);

        Iterator it = ts3.iterator();
        while (it.hasNext()) {
            Person p = (Person) it.next();
            System.out.println(p.getName() + ":" + p.getAge() + ">" + p.getPhoto());
        }
    }

    public static class Person {
        private String name;
        private Integer age;
        private String photo;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person(String name, int age, String photo) {
            this.name = name;
            this.age = age;
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
