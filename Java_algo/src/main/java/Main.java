import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while(true){
            int count =0;
            int n= sc.nextInt();
            if(n==0)break;
            boolean[] isPrime = new boolean[2*n+1];
            Arrays.fill(isPrime , true);

            // 소수는 true
            // 0, 1은 소수가 아니므로 false
            isPrime [0] = isPrime [1] = false;
            for(int i=2; i*i<=2*n; i++){
                // 만약 i가 소수 혹은 아직 지워지지 않았다면
                if(isPrime[i]){
                    // i의 배수 j들에 대해 isPrime[j] = false; 로 둔다.
                    // i*i미만의 배수는 이미 지워졌으므로 신경쓰지 않는다.
                    for(int j=i*i; j<=2*n; j+=i) {
                        isPrime[j] = false;
                    }
                }
            }
            // 1 ~ 120 사이의 소수 출력
            for(int i=n+1; i<=2*n ;i++){
                if(isPrime[i]) count++;
            }
            System.out.println(count);
        }
    }
}
