package BlockMap;

import Main.GamePanel;
import java.awt.*;
import java.io.*;

public class BlockMap
{
  //position
  private double x;
  private double y;
  private double tween;
  
  //bounds
  private int xMin, yMin, xMax, yMax;
  
  //map
  private int blockSize;
  private int rows;
  private int columns;
  private Block[][] blockMap;

  
  private int width;
  private int height;
  
  //drawing
  private int rowOffset;
  private int colOffset;
  private int numRowsToDraw;
  private int numColsToDraw;

  public BlockMap () {
    init();
    loadBlocks();  
  }
  
  private void init() {
    blockSize = 32;
    rows = 30;
    columns = 40;
    width = columns * blockSize;
    height = rows * blockSize;
    blockMap = new Block[rows][columns];
    numRowsToDraw = GamePanel.HEIGHT / blockSize + 2;
    numColsToDraw = GamePanel.WIDTH / blockSize + 2;
    xMin = GamePanel.WIDTH - width;
    yMin = GamePanel.HEIGHT - height;
    xMax = 0;
    yMax = 0;
    tween = 0.07;
  }
  
  private void loadBlocks() {
    try {
      
      InputStream in = new FileInputStream("Resources/level.txt");
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      int xBlock = 0;
      int yBlock = 0;
      
      //for loop to load the blocks into blockMap
      for (int i = 0; i < rows; i++) {
        line = br.readLine();
        xBlock = 0;
        for (int j = 0; j < columns; j++) {
          if (line.charAt(j) == '1' || line.charAt(j) == '2') {
            try {
              blockMap[i][j] = new Block(Integer.parseInt(Character.toString(line.charAt(j))),
                                        xBlock, yBlock, blockSize, blockSize);
            }
            catch (Exception e) {
              e.printStackTrace();
            } 
          } else if (line.charAt(j) == '3' || line.charAt(j) == '4') {
            try {
              blockMap[i][j] = new PowerUp(Integer.parseInt(Character.toString(line.charAt(j))),
                                        xBlock, yBlock, blockSize, blockSize);
            }
            catch (Exception e) {
              e.printStackTrace();
            } 
          }
          else {
            blockMap[i][j] = new Block(Block.EMPTY_TILE,
                                      xBlock, yBlock, blockSize, blockSize);
          }
          xBlock += blockSize;
        }
        yBlock += blockSize;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
 
  public synchronized void draw(Graphics2D g) {
    try {
      
      //for loop to draw the blocks
      for (int row = rowOffset; row < (rowOffset + numRowsToDraw); row++) {
        if (row >= rows) {
          break;
        }
        
        for (int col = colOffset; col < (colOffset + numColsToDraw); col++) {
          if( col >= columns) {
            break;
          }
          if (blockMap[row][col].getType() == Block.EMPTY_TILE) {
            continue;
          }
          g.drawImage(blockMap[row][col].getImage(),
              (int)x + col * blockSize, (int)y + row * blockSize, null);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public Block[][] getBlockMapArray() {
    return blockMap;
  }
  
  public void setBlockMapArray(Block[][] bm) {
    blockMap = bm;
  }
  
  public int getBlockSize() {
    return blockSize;
  }
  
  public void printBlockMap() {
    System.out.println(("-------------------------------------"));
    for (int i = 0; i < rows; i++) {
      System.out.print("\n");
      for (int j = 0; j < columns; j++) {
        System.out.print(blockMap[i][j].getType());
      }
    }
  }
  
  public Block getBlock(int row, int col) {
    if (row >= rows) {
      row = rows - 1;
    }
    if (col >= columns) {
      col = columns - 1;
    }
    return blockMap[row][col];
  }
  
  public void setBlock(Block block, int row, int col) {
    blockMap[row][col] = block;
  }
  
  public void setBlockType(int type, int row, int col) {
    Block temp = blockMap[row][col];
    temp.setType(type);
    blockMap[row][col] = temp;
  }
  
  public int getType(int row, int col) {
    return blockMap[row][col].getType();
  }
  
  public double getX() {
    return x;
  }
  
  public double getY() {
    return y;
  }
  
  public int getRows() {
    return rows;
  }
  
  public int getColumns() {
    return columns;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
  
  public void setTween(double d) {
    tween = d;
  }
  
  public void setPosition (double x, double y) {
    this.x += (x - this.x) * tween;
    this.y += (y - this.y) * tween;

    fixBounds();
    
    colOffset = (int)-this.x / blockSize;
    rowOffset = (int)-this.y / blockSize;
  }
  
  private void fixBounds() {
    if (x < xMin) {
      x = xMin;
    }
    
    if (y < yMin) {
      y = yMin;
    }
    
    if (x > xMax) {
      x = xMax;
    }
    
    if (y > yMax) {
      y = yMax;
    }
  }
}
