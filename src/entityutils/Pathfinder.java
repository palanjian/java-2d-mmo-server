package entityutils;

import java.util.ArrayList;

import entityutils.Node;
import packets.TileMap;

public class Pathfinder {
    Node[][] nodes;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    TileMap tileMap;

    public Pathfinder(GamePanel gamePanel) {
        tileHandler = gamePanel.tileHandler;
        instantiateNodes();
    }

    public void instantiateNodes() {
        tileMap = tileHandler.getTileMap();
        nodes = new Node[tileMap.getColumns()][tileMap.getRows()];
        for(int col = 0; col < tileMap.getColumns(); ++col) {
            for(int row = 0; row < tileMap.getRows(); ++row) {
                nodes[col][row] = new Node(col, row);
            }
        }
    }
    public void resetNodes() {
        for(int col = 0; col < tileMap.getColumns(); ++col) {
            for(int row = 0; row < tileMap.getRows(); ++row) {
                nodes[col][row].open = false;
                nodes[col][row].checked = false;
                nodes[col][row].solid = false;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = nodes[startCol][startRow];
        currentNode = startNode;
        goalNode = nodes[goalCol][goalRow];
        openList.add(currentNode);

        for(int row = 0; row < tileMap.getRows(); ++row) {
            for(int col = 0; col < tileMap.getColumns(); ++col) {
                boolean existsCollision = tileMap.getCollisionMap()[row][col];
                if(existsCollision) {
                    nodes[col][row].solid = true;
                }
                getCost(nodes[col][row]);
            }
        }
    }
    public void getCost(Node node) {
        //G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //F cost
        node.fCost = node.gCost + node.hCost;
    }
    public boolean search() {
        while(goalReached == false && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            //checks current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //open the "up" node
            if(row - 1 >= 0) {
                openNode(nodes[col][row-1]);
            }
            //open the "left" node
            if(col - 1 >= 0) {
                openNode(nodes[col-1][row]);
            }
            //open the "down" node
            if(row + 1 < tileMap.getRows()) {
                openNode(nodes[col][row+1]);
            }
            //open the "right" node
            if(col + 1 < tileMap.getColumns()) {
                openNode(nodes[col+1][row]);
            }
            //find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i=0; i<openList.size(); i++) {
                //checks if this node's f cost is better
                if(openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //if f cost is equal, check the g cost
                else if(openList.get(i).fCost == bestNodefCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            //if there is no node in openList, end the loop
            if(openList.size() == 0) break;

            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    public void openNode(Node node) {
        if(node.open == false && node.checked == false && node.solid == false) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackThePath() {
        Node current = goalNode;
        while(current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}