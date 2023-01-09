import java.awt.Color;

public class SeamCarver {

    private class EnergyMatrix
    {

//        Integer[][] energyArray;

        private EnergyMatrix()
        {
//            energyArray = new Integer[SeamCarver.this.width()][SeamCarver.this.height()];
//            for (int j = 0; j < SeamCarver.this.height(); j++)
//            {
//                for (int i = 0; i < SeamCarver.this.width(); i++)
//                    energyArray[i][j] = (int) SeamCarver.this.energy(i, j);
//            }
        }

        private Iterable<int[]> adj(int[] elementAxis)
        {
            int x = elementAxis[0];
            int y = elementAxis[1];
            int[] downLeftElement = getElement(x-1, y+1);
            int[] downElement = getElement(x, y+1);
            int[] downRightElement = getElement(x+1, y+1);
            Queue<int[]> adj = new Queue<int[]>();
            if(downLeftElement!=null) adj.enqueue(downLeftElement);
            if(downElement!=null) adj.enqueue(downElement);
            if(downRightElement!=null) adj.enqueue(downRightElement);
            return adj;
        }

        private double getElementEnergy(int[] elementAxis)
        {
            int x = elementAxis[0];
            int y = elementAxis[1];
            if( x>SeamCarver.this.width()-1 || x<0 || y>SeamCarver.this.height()-1 || y<0)
                throw  new IndexOutOfBoundsException("pixels out of range");
//            return energyArray[x][y];
            return (int) SeamCarver.this.energy(x, y);
        }

        private int[] getElement(int x, int y)
        {
            if(x<0 || x>SeamCarver.this.width()-1 || y<0 || y>SeamCarver.this.height()-1) return null;
            int[] elementAxis = new int[2];
            elementAxis[0] = x;
            elementAxis[1] = y;
            return elementAxis;
        }
    }

    private class PictureMatrix
    {
        private  int[][] pictureArray;

        private PictureMatrix(Picture picture)
        {
            pictureArray = new int[picture.width()][picture.height()];
            for (int y = 0; y < picture.height(); y++) {
                for (int x = 0; x < picture.width(); x++) {
                    pictureArray[x][y] = picture.get(x, y).getRGB();
                }
            }
        }

        private PictureMatrix(int width, int height)
        {
            pictureArray = new int[width][height];
        }

        private void set(int x, int y, int RGB)
        {
            pictureArray[x][y] = RGB;
        }

        private int width(){ return pictureArray.length; }

        private int height(){ return pictureArray[0].length; }

        private int getRGB(int x, int y) { return  pictureArray[x][y]; }

        private int getAlpha(int x, int y) { return  (pictureArray[x][y] & 0xFF000000) >> 24 ; }

        private int getRed(int x, int y) { return  (pictureArray[x][y] & 0x00FF0000) >> 16 ; }

        private int getGreen(int x, int y) { return  (pictureArray[x][y] & 0x0000FF00) >> 8 ; }

        private int getBlue(int x, int y) { return  pictureArray[x][y] & 0x000000FF ; }

        private Picture picture()
        {
            Picture picture = new Picture(pictureArray.length,pictureArray[0].length);
            Color color;
            for (int x = 0; x < pictureArray.length; x++) {
                for (int y = 0; y < pictureArray[x].length; y++) {
                    color = new Color(getRed(x, y), getGreen(x, y), getBlue(x, y));
                    picture.set(x,y, color);
                }
            }
            return picture;
        }
    }


    private double[] distTo;
    private int[] edgeTo;
    private int lastColumnMinimunEnergyElement;
    private double lastColumnMinimunEnergy;
    private EnergyMatrix energyMatrix;
    private PictureMatrix pictureMatrix;
    private boolean isPictureMatrixTransposed = false;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        pictureMatrix = new PictureMatrix(picture);
    }

    // current picture
    public Picture picture() { return  pictureMatrix.picture(); }

    // width of current picture
    public int width() { return pictureMatrix.width(); }

    // height of current picture
    public int height() { return pictureMatrix.height(); }

    // energy of pixel at column x and row y
    public  double energy(int x, int y)
    {
        if( x>this.width()-1 || x<0 || y>this.height()-1 || y<0) throw  new IndexOutOfBoundsException("pixels out of range");
        if(x==0 || x==this.width()-1) return 195075.0;
        if(y==0 || y==this.height()-1) return 195075.0;
        double Dx, Dy;
        Dx = Math.pow(Math.abs(pictureMatrix.getRed(x+1, y) - pictureMatrix.getRed(x-1, y)),2) +
                Math.pow(Math.abs(pictureMatrix.getGreen(x+1, y) - pictureMatrix.getGreen(x-1, y)), 2) +
                Math.pow(Math.abs(pictureMatrix.getBlue(x+1,y) - pictureMatrix.getBlue(x-1,y)),2);
        Dy = Math.pow(Math.abs(pictureMatrix.getRed(x, y+1) - pictureMatrix.getRed(x,y-1)),2) +
                Math.pow(Math.abs(pictureMatrix.getGreen(x, y+1) - pictureMatrix.getGreen(x, y - 1)),2) +
                Math.pow(Math.abs(pictureMatrix.getBlue(x,y+1) - pictureMatrix.getBlue(x,y-1)),2);
        return Dx + Dy;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        if (isPictureMatrixTransposed) transposePictureMatrix();
        return findSeam();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        if (!isPictureMatrixTransposed) transposePictureMatrix();
        int[] horizontalSeam = findSeam();
        if (isPictureMatrixTransposed) transposePictureMatrix();
        return horizontalSeam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (isPictureMatrixTransposed) transposePictureMatrix();
        removeSeamFromPicture(seam);
    }

    public void removeHorizontalSeam(int[] seam)
    {
        if (!isPictureMatrixTransposed) transposePictureMatrix();
        removeSeamFromPicture(seam);
        if (isPictureMatrixTransposed) transposePictureMatrix();
    }

    /*************************************************************************
     *  Helper Functions
     *************************************************************************/
    private void initialize_ds()
    {
        edgeTo = new int[pictureMatrix.width()*pictureMatrix.height()];
        for (int xy = 0; xy < pictureMatrix.width()*pictureMatrix.height(); xy++)
            edgeTo[xy] = -1;
        distTo = new double[pictureMatrix.width()*pictureMatrix.height()];
        for (int xy = 0; xy < pictureMatrix.width()*pictureMatrix.height(); xy++)
            distTo[xy] = Double.POSITIVE_INFINITY;
        for(int x = 0 ; x < pictureMatrix.width(); x++)
            distTo[x] = this.energy(x,0);
        lastColumnMinimunEnergy = Double.POSITIVE_INFINITY;
        energyMatrix = new EnergyMatrix();
    }

    private int[] findSeam()
    {
        initialize_ds();
        int[] elementAxis = new int[2];
        for (int y = 0; y < this.height(); y++)
        {
            for (int x = 0; x < this.width(); x++)
            {
                elementAxis[0] = x;
                elementAxis[1] = y;
                relax(elementAxis);
            }
        }
        return pathTo(lastColumnMinimunEnergyElement);
    }

    private void relax(int[] parent_elementAxis)
    {
        for (int[] adj_elementAxis : energyMatrix.adj(parent_elementAxis))
        {
            if(distTo[convert_2D_to_1D(adj_elementAxis)] > distTo[convert_2D_to_1D(parent_elementAxis)] + energyMatrix.getElementEnergy(adj_elementAxis)){
                distTo[convert_2D_to_1D(adj_elementAxis)] = distTo[convert_2D_to_1D(parent_elementAxis)] + energyMatrix.getElementEnergy(adj_elementAxis);
                edgeTo[convert_2D_to_1D(adj_elementAxis)] = convert_2D_to_1D(parent_elementAxis);
                if(adj_elementAxis[1] == this.height() - 1 && distTo[convert_2D_to_1D(adj_elementAxis)]<lastColumnMinimunEnergy){
                    lastColumnMinimunEnergy = distTo[convert_2D_to_1D(adj_elementAxis)];
                    lastColumnMinimunEnergyElement = convert_2D_to_1D(adj_elementAxis);
                }
            }
        }
    }

    private void removeSeamFromPicture(int[] seam)
    {
        if( seam == null) throw new NullPointerException("Null seam provided");
        if(seam.length != pictureMatrix.height()) throw new IllegalArgumentException("Wrong seam length.");
        if(pictureMatrix.height() <= 1 || pictureMatrix.width() <= 1) throw new IllegalArgumentException("Picture less than a pixel width or length.");
        PictureMatrix removedSeamPictureMatrix = new PictureMatrix(pictureMatrix.width()-1,pictureMatrix.height());
        for (int x = 0; x < pictureMatrix.width(); x++) {
            for (int y = 0; y < pictureMatrix.height(); y++) {
                if(x>seam[y]){
                    removedSeamPictureMatrix.set(x - 1, y, pictureMatrix.getRGB(x, y));
                } else {
                    if(x==seam[y] && seam[y]==pictureMatrix.width()-1) continue;
                    removedSeamPictureMatrix.set(x, y, pictureMatrix.getRGB(x, y));
                }
            }
        }
        pictureMatrix=removedSeamPictureMatrix;
    }

    private void transposePictureMatrix()
    {
        PictureMatrix transposedPictureMatrix = new PictureMatrix(pictureMatrix.height() ,pictureMatrix.width());
        // transpose in-place
        for (int i = 0; i < pictureMatrix.width(); i++) {
            for (int j = 0; j < pictureMatrix.height(); j++) {
                transposedPictureMatrix.set(j,i,pictureMatrix.getRGB(i,j));
            }
        }
        pictureMatrix = transposedPictureMatrix;
        isPictureMatrixTransposed = !isPictureMatrixTransposed;
    }





    private int[] pathTo(int sourceElement)
    {
        int[] path = new int[this.height()];
        int[] eAxis =  new int[2];
        eAxis = convert_1D_to_2D(sourceElement);
        int reverseCounter = this.height()-1;
        path[this.height()-1] = eAxis[0];
        reverseCounter--;
        for (int e = edgeTo[sourceElement]; e != -1; e = edgeTo[e]) {
            eAxis = convert_1D_to_2D(e);
            path[reverseCounter] = eAxis[0];
            reverseCounter--;
        }
        return path;
    }


    /*************************************************************************
     *  Utility Functions
     *************************************************************************/
    private int convert_2D_to_1D(int[] elementAxis)
    {
        return elementAxis[1]*this.width()+elementAxis[0];
    }

    private int[] convert_1D_to_2D(int element)
    {
        int[] elementAxis = new int[2];
        int y = element / this.width();
        int x = element % this.width();
        elementAxis[0] = x;
        elementAxis[1] = y;
        return elementAxis;
    }

}