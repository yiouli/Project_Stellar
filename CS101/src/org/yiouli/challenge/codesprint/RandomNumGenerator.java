package org.yiouli.challenge.codesprint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RandomNumGenerator {

	//handle x >=y, both positive
	static int gcd(int x, int y) {
		if(x<y)
			return gcd(y,x);
		if(x%y==0)
			return y;
		return gcd(x%y, y);
	}
	
	static int[] probability(int a, int b, int c) {
		if(a>b)
			return probability(b, a, c);
		int[] ret = new int[2];
		int x=1, y=1;
		if(c>a+b) {
			x=1;
			y=1;
		}
		else if(a<=c && b<=c) {
			x = 2*a*b - (a+b-c)*(a+b-c);
			y = 2*a*b;
		}
		else if(a<c && b>=c) {
			x = a*(2*c - a);
			y = 2*a*b;
		}
		else {	//c<=a && a<=b
			x = c*c;
			y = 2*a*b;
		}
		int gcd = gcd(y,x);
		ret[0] = x/gcd;
		ret[1] = y/gcd;
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
    	InputStreamReader in = null;
    	if(args.length > 0)
    		in = new FileReader(args[0]);
    	else
    		in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        int t = Integer.parseInt(br.readLine());
        for(int ti=0;ti<t;ti++) {
            String[] tmp = br.readLine().split(" ");
            int a = Integer.parseInt(tmp[0]);
            int b = Integer.parseInt(tmp[1]);
            int c = Integer.parseInt(tmp[2]);
            int[] res = probability(a,b,c);
            System.out.println(res[0]+"/"+res[1]);
        }
    }
}
