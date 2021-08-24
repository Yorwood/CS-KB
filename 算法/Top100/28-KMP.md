**KMP**

```java
 public int[] nextCaculate(String tarStr){
     int[] next = new int[tarStr.length()];
     next[0] = 0;
     int j = 0;
     int i = 1;
     while(i < tarStr.length()){
         if(tarStr.charAt(j) == tarStr.charAt(i)){
             j++;
             next[i] = j;
             i++;
         }
         else{
             if(j != 0){
                 j = next[j-1];//相当于递归，找当前匹配前后缀中各自的最大前后缀匹配串
             }
             else{
                 next[i] = 0;//0匹配则直接赋值
                 i++;
             }
         } 
     }
     return next; 
 }


public int KMP(String pStr , String tStr){
    int[] next = nextCaculate(tStr);
    int p = 0;
    int t = 0;
    while(p < pStr.length()){
        if(pStr.charAt(p) == tStr.charAt(t)){ //相等则继续向后匹配
            p++;
            t++;
        }

        if(t == tStr.length()){//全匹配返回
            return p - t; 
        }
        
		//预检测下一次是否匹配,不匹配则利用next
        if(p < pStr.length() && pStr.charAt(p) != tStr.charAt(t)){
            if(t > 0)
                t = next[t-1];//跳到前面匹配串[0,t-1]的最大前后缀位置
            else //前面无匹配串,则从下一个开始匹配
                p++;  
        }
    }
    return -1;
} 
```

