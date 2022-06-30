import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int a = Integer.parseInt(br.readLine());
        int start = a;
        String stringA="" ;
        String back = "";
        int count = 0;
        int sum = 0;
        while(true){

            if(a<10){
                stringA = "0"+a;
            }else{stringA = Integer.toString(a);}
            String[] array = stringA.split("");
            back = array[1];
            sum = Integer.parseInt(array[0]) + Integer.parseInt(array[1]);
            String sumback = Integer.toString(sum%10);
            a = Integer.parseInt(back+sumback);
            count++;
            if(start== a)break;

        }
        System.out.println(count);

    }
}





// int 여러개 받을 떄
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    StringTokenizer st = new StringTokenizer(br.readLine());
//
//    int M = Integer.parseInt(st.nextToken());
//    int N = Integer.parseInt(st.nextToken());

//int 하나만 받을때
//    int koko = Integer.parseInt(br.readLine());