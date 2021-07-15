SPFA + 邻接表数组表示

```
class Solution {
    
    int h[];
    int u[];
    int v[];
    int w[];
    int ne[];
    int dis[][]; //i,时间:通行费
    int vis[][]; //判重
    Queue<int[]> q = new LinkedList(); //松弛队列
    int idx = 0;

    public int minCost(int maxTime, int[][] edges, int[] passingFees) {
        int n = passingFees.length;
        int m = edges.length *2;//无向图两倍边
        h = new int[n]; //顶点u,h[u]为其第一条(输入顺序最后一条邻接边)
        u = new int[m];
        v = new int[m];
        w = new int[m];
        ne = new int[m];//ne[i]表示第i条边的，下一条邻接边

        dis = new int[n][maxTime+1];
        vis = new int[n][maxTime+1];

        Arrays.fill(h,-1);

        for(int i =0 ; i < edges.length; i++){
            int from = edges[i][0];
            int to = edges[i][1];
            int time = edges[i][2];
            add(from,to,time);
            add(to,from,time);
        }

        for(int i = 0; i < n ;i++){
            Arrays.fill(dis[i], Integer.MAX_VALUE);
        }

        //spfa
        dis[0][0] = passingFees[0];
        vis[0][0] = 1;
        q.offer(new int[]{0,0});

        while(!q.isEmpty()){
            int[] cur = q.poll();
            vis[cur[0]][cur[1]] = 0;
            
            for(int i = h[cur[0]]; i != -1 ; i = ne[i]){
                if(w[i] + cur[1] > maxTime) continue;
                if(dis[v[i]][w[i]+cur[1]] > dis[cur[0]][cur[1]] + passingFees[v[i]]){
                    dis[v[i]][w[i]+cur[1]] = dis[cur[0]][cur[1]] + passingFees[v[i]];
                    if(vis[v[i]][w[i]+cur[1]] == 1) continue;
                     q.offer(new int[]{v[i],w[i]+cur[1]});
                     vis[v[i]][w[i]+cur[1]] = 1;
                }
            }
        }

        int minCost = Integer.MAX_VALUE;
        for(int i = 0; i< maxTime+1; i++){
            if(minCost > dis[n-1][i]) minCost = dis[n-1][i];
        }
        
        return minCost == Integer.MAX_VALUE ? -1 : minCost; 
    }

    public void add(int _u, int _v, int time){
            u[idx] = _u;
            v[idx] = _v;
            w[idx] = time;
            ne[idx] = h[_u]; 
            h[_u] = idx++;
    }
}
```

