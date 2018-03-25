package org.stathry.commons.utils;

public class DictUtils {

    public static final String CHARS_0_9 = conn('0', '9');
    public static final String CHARS_a_z = conn('a', 'z');
    public static final String CHARS_A_Z = conn('A', 'Z');

    public static void main(String[] args) {
        System.out.println(CHARS_0_9);
        System.out.println(CHARS_a_z);
        System.out.println(CHARS_A_Z);
    }

    private static String conn(int a, int b) {
        StringBuilder builder = new StringBuilder();
        for (; a <= b; a++) {
            builder.append((char) a);
        }
        return builder.toString();
    }

}
