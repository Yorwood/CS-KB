```java
/*牛顿迭代法求近似数，原理是将开根号表示成函数 f(x) = x^2 -c = 0;我们就是要求函数f(x)的曲线
*在x轴的截距值xc，先随机取初值x0 = c，则我们取曲线上的点(x0,f(x0))的切线，设其截距值位x1,根据点斜
*式子可知切线为y(x) = 2x0(x-x0)+f(x0),则x1 = (x0+c/x0)/2,我们可知x1是更加接近截距xc的值，
*依次迭代，不断求得xn，当xn-1 - xn <= gap时，即可返回。
*/
float gap = 0.001f;//精度
int c = 2;//待求开放数
public double newtonSqrt(double x){ //牛顿迭代法求根号
            double x1 = (x+c/x)/2;
            if(x - x1 <= gap) {
                DecimalFormat df = new DecimalFormat("0.00");
                return x1;//保留小数点后2位
            }
            return newtonSqrt(x1); 
 }
```

