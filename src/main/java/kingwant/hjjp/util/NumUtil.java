package kingwant.hjjp.util;

import java.util.ArrayList;
import java.util.List;
public class NumUtil {
	/**
	 * 将一个数分解成2的n次方数相加
	 * @param num  一个2的n次方数相加的和
	 * @return	分解出的2的n次方的数的集合
	 */
	public static List<Integer> getAllTwoPow(int num){
		List<Integer> numList = new ArrayList<Integer>();
		int x = 1;
		while(x <= num){
		   if((num & x) > 0){
		       numList.add(num & x);
		  }
		  x = x << 1;
		}
		return numList;
	}
	
	public static List<Long> getAllTwoPow(Long num){
		List<Long> numList = new ArrayList<Long>();
		int x = 1;
		while(x <= num){
		   if((num & x) > 0){
		       numList.add(num & x);
		  }
		  x = x << 1;
		}
		return numList;
	}
	/**
	 * 求所有的2的n次方数的集合
	 * @param list 所有的元素为2的n次方数，切不能重复
	 * @param value	所求的和当中的一个加数
	 * @return	所有可能的和的集合
	 */
	public static List<Integer> getAllTypeSum(List<Integer> list, int value){
		List<Integer> listSum = new ArrayList<Integer>();
		int max = 0;
		for(int num : list){
			if(num > max){
				max = num;
			}
		}
		int num = max * 2 - 1;
		while(num > 0){
			if(getAllTwoPow(num).contains(value)){
				listSum.add(num);
			}
			--num;
		}
		return listSum;
	}
	
	public static String getListToString(List<Integer> list){
		String str = "(";
		for(int i = 0; i < list.size() - 1; ++i){
			str += list.get(i) + ",";
		}
		str += list.get(list.size() - 1) + ")";
		return str;
	}
	
	public static int getCubeOf2(int x){
		int number=1;
		for (int i = 0; i < x; i++) {
			number*=2;
		}
		
		return  number;
	}
	
	public static int log2(int n){
	    int a=0;
	    while(n%2==0){
	        n=n/2;
	        a++;
	    }
	    //System.out.println(a);
	    return a;
	}
	
	/**
	 * 获取一个数的所有2次方的集合
	 * @param num 具体数
	 * @return
	 */
	public static List<Integer> getPowerOfNumber(int num){
		List<Integer> list=getAllTwoPow(num);
		List<Integer> res=new ArrayList<>();
		if (list!=null&&list.size()!=0) {
			for (int i = 0; i < list.size(); i++) {
				res.add(log2(list.get(i)));
			}
		}
		
		return res;
	}
	
	
	/**
	 * 判断数中有无该2次方的幂
	 * @param sum 具体数
	 * @param weight 2次方的幂
	 * @return
	 */
	public static boolean hasPower(int sum,int weight){
		boolean f=false;
		try {
			f=getPowerOfNumber(sum).contains(weight);
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return f;
	}
	
	/**
	 * 生成n位随机数
	 * @param n
	 * @return
	 */
	public static long generateRandomNumber(int n){
        if(n<1){
            n = 1;
        }
        return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
    }

	   /**
     * 生成六位数字验证码
     * 
     * @Title:getRandNum
     * @Description:
     */
    public static String getRandNum() {
  	    int randNum = 100000 + (int)(Math.random() * ((999999 - 100000) + 1));
  	    return String.valueOf(randNum);
  	}
}
