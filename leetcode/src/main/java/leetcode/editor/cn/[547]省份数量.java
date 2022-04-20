package leetcode.editor.cn;
//
// 
// 有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连
//。 
//
// 省份 是一组直接或间接相连的城市，组内不含其他没有相连的城市。 
//
// 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，而 
//isConnected[i][j] = 0 表示二者不直接相连。 
//
// 返回矩阵中 省份 的数量。 
//
// 
//
// 示例 1： 
//
// 
//输入：isConnected = [[1,1,0],[1,1,0],[0,0,1]]
//输出：2
// 
//
// 示例 2： 
//
// 
//输入：isConnected = [[1,0,0],[0,1,0],[0,0,1]]
//输出：3
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 200 
// n == isConnected.length 
// n == isConnected[i].length 
// isConnected[i][j] 为 1 或 0 
// isConnected[i][i] == 1 
// isConnected[i][j] == isConnected[j][i] 
// 
// 
// 
// Related Topics 深度优先搜索 广度优先搜索 并查集 图 👍 622 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    /**
     * 图：两种存储结构，分别是邻接矩阵和邻接表
     * 邻接矩阵：
     * 边：
     *
     * @param isConnected
     * @return
     */
    public int findCircleNum(int[][] isConnected) {
        // 从0开始搜索，从左往右，从上往下，如果存在1,则存在一个城市，同时记录已经组成城市的集合，将相连的放入一个队列，
        // 出队列相连的城市，搜索是否与没有相连的可能城市是否存在相连，存在则加入队列和组成城市的集合，直到遍历完成

        // 将城市看作是图的系欸但，连接关系是便，给定的矩阵是图的邻接矩阵，省份即为图的连通分量。
        // 利用深度优先搜索和广度优先搜索或者并查集实现。

        int provinces = isConnected.length;
        boolean[] visit = new boolean[provinces];
        int circles = 0;
        for (int i = 0; i < provinces; i++) {
            if (!visit[i]) {
                visit[i] = true;
                dfs(isConnected, visit, provinces, i);
                circles++;
            }
        }
        return circles;
    }

    private void dfs(int[][] isConnected, boolean[] visit, int provinces, int i) {
        for (int j = 0; j < provinces; j++) {
            if (isConnected[i][j] == 1 && !visit[j]) {
                visit[j] = true;
                dfs(isConnected, visit, provinces, j);
            }
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
