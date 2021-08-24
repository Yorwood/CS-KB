**快速排序算法，主要就是选定一个基准pivot，然后将数组分为大于和小于该基准的两部分，此为一轮操作，然后依次递归这两部分数组作为新的输入，继续重复上述操作**

```java
输入: [3,2,1,5,6,4] 和 k = 2
输出: 5
class Solution {
    //Method-1:快速排序，取最后第k个元素
    public int findKthLargest(int[] nums, int k) {
        qucikSort(nums,0,nums.length-1);
        return nums[nums.length-k];
    }
	//快速排序
    public void qucikSort(int[] nums, int l, int r){
        if(l >= r)return;//出口，当左下标不小于右下标时，只有一个元素或者非法区间
        int pivot = nums[l];//选定最左端元素为基准(也可以随机交换后再选取，防止退化)
        int i = l;
        int j = r;
        //由于是选择左端元素为基准，因此左端元素位置被空出，先从右端开始寻找第一个小于基准元素
        while(i < j){
            while(i < j && nums[j] >= pivot){
                j--;
            }
            nums[i] = nums[j];//将当前小于基准元素放入i的空位中
            while(i < j && nums[i] <= pivot){
                i++;
            }
            nums[j] = nums[i];//将当前小于基准元素放入j的空位中
        }

        nums[i] = pivot;//将基准元素放入i=j的空位中，该位置即为其最终位置
        qucikSort(nums,l,i-1);
        qucikSort(nums,i+1,r);
    }
    
    //Method-2:快速选择，由于题目只要求kth-E,因此我们可以根据当前k和大分区的元素个数关系，
    //选择一个子区间进行排序，而不需要全排序
    public int findKthLargest(int[] nums, int k) {
        int l = 0;
        int r = nums.length-1;
        
        int p = l;
        int topK;

        while(true){
            p = Patition(nums,l,r);
            ind = r-p+1;//较大分区的元素个数
            if(k > p){//当前需要取得元素次序>分区点次序，则该元素一定在较大分区中
                l = p+1;
            }
            else if(k < p){//当前需要取得元素次序<分区点次序,则该元素一定在较小分区中
                r = p-1;
            }
            else{
                break;
            }  
        }
        return nums[p];//当前需要取得元素次序=分区点次序，返回分区基准元素
    }

    public int Patition(int[] nums, int l, int r){
        if(l >= r)return l;
        int pivot = nums[l];
        int i = l;
        int j = r;
        while(i < j){
            while(i < j && nums[j] >= pivot){
                j--;
            }
            nums[i] = nums[j];
            while(i < j && nums[i] <= pivot){
                i++;
            }
            nums[j] = nums[i];
        }
        nums[i] = pivot;
        return i;
    }
}
```

